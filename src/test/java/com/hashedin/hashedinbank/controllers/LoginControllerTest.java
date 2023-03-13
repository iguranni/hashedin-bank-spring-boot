package com.hashedin.hashedinbank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hashedin.hashedinbank.common.ApiResponse;
import com.hashedin.hashedinbank.constants.SecurityConstants;
import com.hashedin.hashedinbank.dto.request.LoginRequestDto;
import com.hashedin.hashedinbank.dto.request.UserRegistrationDto;
import com.hashedin.hashedinbank.services.UserService;
import com.hashedin.hashedinbank.services.impl.UserDetailsImpl;
import com.hashedin.hashedinbank.utils.ConstructTestObjectUtil;
import com.hashedin.hashedinbank.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.hashedin.hashedinbank.utils.ConstructTestObjectUtil.constructLoginRequestDto;
import static com.hashedin.hashedinbank.utils.ConstructTestObjectUtil.constructUserDetails;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserService userService;

    @MockBean
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void authenticateUser() throws Exception {
        LoginRequestDto loginRequestDto = constructLoginRequestDto();
        UserDetailsImpl userDetails = constructUserDetails();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        MvcResult mvcResult = mockMvc.perform(post("/auth/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully authenticated the user")))
                .andReturn();

        assertNotNull(mvcResult.getResponse().getHeader(SecurityConstants.JWT_HEADER));
        assertEquals("jwtToken", mvcResult.getResponse().getHeader(SecurityConstants.JWT_HEADER));
    }

    @Test
    @WithMockUser(roles = "USER")
    void registerAdmin() throws Exception {
        Mockito.when(userService.createAdmin(any(UserRegistrationDto.class))).thenReturn(ConstructTestObjectUtil.constructUser());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(csrf())
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(ConstructTestObjectUtil.constructUserRegistrationDto()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Successfully registered [ROLE_USER] with HashedIn Bank for company Id : 1")));
    }
}