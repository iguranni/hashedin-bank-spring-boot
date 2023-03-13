package com.hashedin.hashedinbank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hashedin.hashedinbank.dto.request.CompanyRegistrationDto;
import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.services.CompanyService;
import com.hashedin.hashedinbank.services.UserService;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import com.hashedin.hashedinbank.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void registerUserTest() throws Exception {

        Mockito.when(userService.createUser(any(UserRegistrationDto.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ConstructTestObjectUtil.constructUserRegistrationDto())))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully registered user with HashedIn Bank for company Id : ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));

    }

    @Test
    @WithMockUser(roles = "USER")
    void approveUserTest() {
        doNothing().when(userService).approveUser(anyString());
    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void updateUserTest() {
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void deleteUserTest() {
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void fetchAllUsersByCompanyTest() {
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void fetchAllUsersTest() {
//    }
}