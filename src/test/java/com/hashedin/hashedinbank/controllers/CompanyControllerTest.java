package com.hashedin.hashedinbank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hashedin.hashedinbank.dto.request.CompanyRegistrationDto;
import com.hashedin.hashedinbank.dto.request.CompanyUpdateDto;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.services.CompanyService;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @MockBean
    CompanyService companyService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "SUPER_ADMIN")
    void registerCompanyTest() throws Exception {

        Mockito.when(companyService.createCompany(any(CompanyRegistrationDto.class))).thenReturn(ConstructTestObjectUtil.constructCompany());
        mockMvc.perform(MockMvcRequestBuilders.post("/company/register")
                        .with(csrf())
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(ConstructTestObjectUtil.constructCompanyRegistrationDto()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully registered company with HashedIn Bank for company code : ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));

    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCompanyTest() throws Exception {
        doNothing().when(companyService).expireCompany(anyInt());
        mockMvc.perform(MockMvcRequestBuilders.delete("/company/delete"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully deleted company from HashedIn Bank for company id : ")));

    }

    @Test
    @WithMockUser(roles = "USER")
    void updateCompanyTest() throws Exception {
        Mockito.when(companyService.updateCompany(any(CompanyUpdateDto.class))).thenReturn(ConstructTestObjectUtil.constructCompany());
        mockMvc.perform(MockMvcRequestBuilders.post("/company/update")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ConstructTestObjectUtil.constructCompanyUpdateDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully updated company details with HashedIn Bank for company id : ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));

    }

    @Test
    @WithMockUser(roles = "USER")
    void findAllCompaniesTest() throws Exception {
        List<Company> expectedCompanies = List.of(ConstructTestObjectUtil.constructCompany());

        Mockito.when(companyService.getAllCompanies()).thenReturn(expectedCompanies);
        mockMvc.perform(MockMvcRequestBuilders.get("/company/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully fetched all clients of HashedIn Bank : ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));
    }
}