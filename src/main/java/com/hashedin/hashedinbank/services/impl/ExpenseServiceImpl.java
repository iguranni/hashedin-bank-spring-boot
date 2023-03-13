package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.dto.request.ExpenseRequestDto;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.entities.Expense;
import com.hashedin.hashedinbank.entities.ExpenseCategory;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.exception.DataTransformationException;
import com.hashedin.hashedinbank.exception.ExpenseCategoryNotFoundException;
import com.hashedin.hashedinbank.exception.UserNotFoundException;
import com.hashedin.hashedinbank.logic.CSVReaderLogic;
import com.hashedin.hashedinbank.repositories.CompanyRepository;
import com.hashedin.hashedinbank.repositories.ExpenseCategoryRepository;
import com.hashedin.hashedinbank.repositories.ExpenseRepository;
import com.hashedin.hashedinbank.repositories.UserRepository;
import com.hashedin.hashedinbank.services.ExpenseService;
import com.hashedin.hashedinbank.utils.CommonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hashedin.hashedinbank.constants.AppConstants.COMMA;

@Service
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {
    private final CSVReaderLogic csvReaderLogic;
    private final Validator validator;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ExpenseExportServiceHandler expenseExportServiceHandler;
    private final ExportToExcelService exportToExcelService;
    private String headerLine;

    @Autowired
    public ExpenseServiceImpl(CSVReaderLogic csvReaderLogic,
                              Validator validator, ExpenseRepository expenseRepository,
                              ExpenseCategoryRepository expenseCategoryRepository,
                              CompanyRepository companyRepository,
                              UserRepository userRepository,
                              ExpenseExportServiceHandler expenseExportServiceHandler, ExportToExcelService exportToExcelService) {
        this.csvReaderLogic = csvReaderLogic;
        this.validator = validator;
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.expenseExportServiceHandler = expenseExportServiceHandler;
        this.exportToExcelService = exportToExcelService;
    }

    private enum HeaderNames {
        COL1("BILL_ID"),
        COL2("EXPENSE_CD"),
        COL3("TOTAL_EXPENDITURE"),
        COL4("RECEIPT_DATE"),
        COL5("COMMENTS");

        public final String colName;

        HeaderNames(final String colName) {
            this.colName = colName;
        }

    }

    /**
     * @param multipartFile
     */
    @Override
    @Transactional
    public void uploadExpense(MultipartFile multipartFile, String username) throws IOException {
        User user = userRepository.findByEmailAndActiveCreditCard(username, Boolean.TRUE)
                .orElseThrow(() -> new UserNotFoundException("Active Credit Card does not exists for the user : " + username));
        InputStream inputStream = multipartFile.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        headerLine = reader.readLine();
        checkFlatFileHeader();
        reader.lines()
                .forEachOrdered(line -> handleLine(line, user));

    }

    /**
     * to calculate the monthly expense by company
     *
     * @Param username
     */
    @Override
    @Transactional
    public void calculateMonthlyExpenseByCompany() {

        LocalDateTime expenseExportLimit = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(30);
        List<Company> companies = companyRepository.findAllByIsActiveFlag(Boolean.TRUE);
        companies.forEach(company -> {
            List<Expense> expenses = expenseRepository.findMonthlyExpenseByCompany(company.getCompanyId(), Boolean.TRUE, expenseExportLimit);
            try {
                exportToExcelService.createExcel(expenseExportServiceHandler, expenses, company.getCompanyCode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    /**
     * converting the line text to object
     * and saving in database entity
     */
    public void handleLine(String line, User user) {
        ExpenseRequestDto expenseRequestDto = (ExpenseRequestDto) csvReaderLogic.convertLineTextToObject(ExpenseRequestDto.class, line, headerLine);
        ValidateDatesColumn(expenseRequestDto);
        validateColumns(expenseRequestDto);
        createExpenseEntry(expenseRequestDto, user);
    }


    /**
     * validating the date columns
     */
    private void ValidateDatesColumn(ExpenseRequestDto expenseRequestDto) {
        CommonUtils.parseableDate(expenseRequestDto.getReceiptDate());
    }

    /**
     * validating the columns if they are blank or null
     * and columns having valid integer values or not
     */
    protected <T> void validateColumns(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    /**
     * validating the file header
     */
    private boolean checkFlatFileHeader() {
        List<String> headers = Arrays.asList(headerLine.split(COMMA));
        AtomicInteger colNum = new AtomicInteger(1);

        if (headers.size() == 5 && (headers.stream().allMatch(header ->
                header.equals(HeaderNames.valueOf("COL" + colNum.getAndIncrement()).colName)))) {
            headerLine += "\n";
            return true;
        } else {
            throw new DataTransformationException("header error");
        }
    }

    /**
     * saving inside database entity
     */
    private void createExpenseEntry(ExpenseRequestDto expenseRequestDto, User user) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByExpenseCode(expenseRequestDto.getExpenseCode())
                .orElseThrow(() -> new ExpenseCategoryNotFoundException("Expense Category does not exists with expense code : " + expenseRequestDto.getExpenseCode()));

        Expense expense = Expense.builder()
                .billId(expenseRequestDto.getBillId())
                .expenseCategoryId(expenseCategory)
                .userId(user)
                .receiptDate(LocalDate.parse(expenseRequestDto.getReceiptDate()))
                .totalExpenditure(BigDecimal.valueOf(Double.parseDouble(expenseRequestDto.getTotalExpenditure())))
                .comments(expenseRequestDto.getComments())
                .build();
        expenseRepository.save(expense);
    }
}
