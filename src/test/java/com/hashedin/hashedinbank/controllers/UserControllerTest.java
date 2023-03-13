package com.hashedin.hashedinbank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hashedin.hashedinbank.dto.request.CompanyRegistrationDto;
import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.dto.request.UserUpdateDto;
import com.hashedin.hashedinbank.entities.Company;
import com.hashedin.hashedinbank.entities.User;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        mockMvc.perform(MockMvcRequestBuilders.post("/user/add")
                        .with(csrf())
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(ConstructTestObjectUtil.constructUserRegistrationDto()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully registered user with HashedIn Bank for company Id : 1")));

    }

    @Test
    @WithMockUser(roles = "USER")
    void approveUserTest() throws Exception {
      doNothing().when(userService).approveUser(anyString());

        mockMvc.perform(MockMvcRequestBuilders.put("/user/approve")
                        .with(csrf())
                        .param("email","test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully approved user with HashedIn Bank with email : test@gmail.com")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateUserTest() throws Exception {
        Mockito.when(userService.updateUser(any(UserUpdateDto.class),anyString())).thenReturn(ConstructTestObjectUtil.constructUser());
        Mockito.when(jwtUtils.getUsernameFromToken(anyString())).thenReturn("test auth token");
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                        .with(csrf())
                        .header("Authorization","abccccccc")
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(ConstructTestObjectUtil.constructUserUpdateDto()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully updated user details with HashedIn Bank for user id : 1")));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUserTest() throws Exception {

        doNothing().when(userService).deleteUser(anyLong(),anyString());
        Mockito.when(jwtUtils.getUsernameFromToken(anyString())).thenReturn("test auth token");
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete")
                        .with(csrf())
                        .header("Authorization","abccccccc")
                        .param("userId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully deleted user from HashedIn Bank with user id : 1")));

    }

    @Test
    @WithMockUser(roles = "USER")
    void fetchAllUsersByCompanyTest() throws Exception {
        List<User> expectedCompanies = List.of(ConstructTestObjectUtil.constructUser());

        Mockito.when(userService.getAllUserByCompany(anyLong())).thenReturn(new PageImpl<>(expectedCompanies));
        mockMvc.perform(MockMvcRequestBuilders.get("/user/all-by-company")
                        .with(csrf())
                        .header("Authorization","abccccccc")
                        .param("userId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully fetched all users from HashedIn Bank associated with company : INFOSYS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));

    }

    @Test
    @WithMockUser(roles = "USER")
    void fetchAllUsersTest() throws Exception {

        List<User> expectedCompanies = List.of(ConstructTestObjectUtil.constructUser());

        Mockito.when(userService.getAllUsers()).thenReturn(new PageImpl<>(expectedCompanies));
        mockMvc.perform(MockMvcRequestBuilders.get("/user/all")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully fetched all users from HashedIn Bank : ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultCount", is(1)));
    }
}