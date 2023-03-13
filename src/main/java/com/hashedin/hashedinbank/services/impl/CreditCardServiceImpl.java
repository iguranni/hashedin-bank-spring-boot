package com.hashedin.hashedinbank.services.impl;

import com.hashedin.hashedinbank.entities.CreditCard;
import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.logic.CreditCardNumberGeneratorLogic;
import com.hashedin.hashedinbank.repositories.CreditCardRepository;
import com.hashedin.hashedinbank.services.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.hashedin.hashedinbank.constants.AppConstants.*;

@Service
@Slf4j
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardNumberGeneratorLogic creditCardNumberGenerator;

    @Autowired
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository,
                                 CreditCardNumberGeneratorLogic creditCardNumberGenerator) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardNumberGenerator = creditCardNumberGenerator;
    }


    /**
     * To generate credit card and assign to a user
     *
     * @param user -- to whom credit card has to be assigned
     */
    @Override
    @Transactional
    public void generateCreditCard(User user) {
        log.info("inside generate card method");
        if (user.isActiveFlag() && user.isApprovedFlag()) {
            log.info("hiiting query");
            creditCardRepository.save(constructCreditCard(user));
            log.info("successfull save for cc");
        }
    }

    /**
     * @return
     */
    @Override
    @Transactional
    public Page<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAllByIsActiveFlag(Boolean.TRUE, PageRequest.of(0, PAGE_LIMIT));
    }

    /**
     *to validate credit cards for all users
     */
    @Override
    @Transactional
    public void validateCreditCard() {
        Page<CreditCard> creditCard = creditCardRepository.findAllByIsActiveFlag(Boolean.TRUE, PageRequest.of(0, PAGE_LIMIT));
        for(int i=0; i<creditCard.getTotalElements(); i++) {
            if(creditCard.getContent().get(i).getExpiredDate() == LocalDate.now().minusDays(2)) {
                //send the email about card expiry
            } else if (Objects.equals(creditCard.getContent().get(i).getExpiredDate(), LocalDate.now())) {
                creditCard.getContent().get(i).setExpiredDate(LocalDate.now());
                creditCard.getContent().get(i).setActiveFlag(Boolean.FALSE);
                generateCreditCard(creditCard.getContent().get(i).getCardHolderId());
            }
        }

    }

    private CreditCard constructCreditCard(User user) {
        String ccNumber = creditCardNumberGenerator.generate(HASHEDIN_BIN, CC_LENGTH);
        return CreditCard.builder()
                .creditCardNumber(ccNumber)
                .cvv(creditCardNumberGenerator.generateCvvNumber(ccNumber))
                .issuedDate(LocalDate.now())
                .expiredDate(LocalDate.now().plusYears(4))
                .isActiveFlag(Boolean.TRUE)
                .cardHolderId(user)
                .build();
    }
}
