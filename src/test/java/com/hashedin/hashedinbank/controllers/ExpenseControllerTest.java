package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.services.ExpenseService;
import com.hashedin.hashedinbank.utils.JobScheduler;
import com.hashedin.hashedinbank.utils.JwtUtils;
import jakarta.servlet.http.Part;
import org.apache.poi.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @MockBean
    ExpenseService expenseService;
    @MockBean
    JobScheduler jobScheduler;
    @MockBean
    JwtUtils jwtUtils;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void uploadExpenseSheet() throws Exception {
        InputStream inputStream = new FileInputStream("src/test/java/com/hashedin/hashedinbank/resources/test.xlsx");

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.xlsx",
                "application/x-xls",
                IOUtils.toByteArray(inputStream));

        Mockito.when(jwtUtils.getUsernameFromToken(anyString())).thenReturn("test auth token");
        doNothing().when(expenseService).uploadExpense(any(MultipartFile.class), anyString());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/expense/upload")
                        .file(mockMultipartFile)
                        .with(csrf())
                        .header("Authorization","abccccccc")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully uploaded expense details")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void calculateMonthlyExpense() throws Exception {

        doNothing().when(expenseService).calculateMonthlyExpenseByCompany();
        mockMvc.perform(MockMvcRequestBuilders.post("/expense/monthly-expense")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully generated monthly expense file")));


    }

    @Test
    @WithMockUser(roles = "USER")
    void manualTriggerForScheduler() throws Exception {
        doNothing().when(jobScheduler).monthlyExpenseSheetScheduler();
        mockMvc.perform(MockMvcRequestBuilders.post("/expense/monthly-expense/scheduler")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully generated monthly expense file")));
    }
}