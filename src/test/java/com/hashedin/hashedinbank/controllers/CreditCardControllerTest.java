package com.hashedin.hashedinbank.controllers;

import com.hashedin.hashedinbank.entities.User;
import com.hashedin.hashedinbank.services.CreditCardService;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.hashedin.hashedinbank.utils.ConstructTestObjectUtil.constructCreditCard;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@WebMvcTest(CreditCardController.class)
class CreditCardControllerTest {

    @MockBean
    CreditCardService creditCardService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void fetchAllCards() throws Exception {
        Mockito.when(creditCardService.getAllCreditCards()).thenReturn(new PageImpl<>(List.of(constructCreditCard())));
        mockMvc.perform(MockMvcRequestBuilders.get("/card/all")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully fetched all Credit card details from HashedIn Bank : ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));
    }
}