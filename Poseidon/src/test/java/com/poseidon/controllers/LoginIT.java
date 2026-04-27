package com.poseidon.controllers;

import com.poseidon.domain.CurvePoint;
import com.poseidon.domain.User;
import com.poseidon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    // --- /app/login ---

    @Test
    @WithMockUser
    void login_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @WithMockUser
    void getAllUserArticles_shouldReturnUserListView() throws Exception {

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }
    @Test
    @WithMockUser
    void getAllUserArticles_shouldReturnEmptyList_whenNoUsers() throws Exception {

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attribute("users", List.of()));
    }


    @Test
    void shouldSaveUserInDatabase() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setFullname("Test User");
        user.setRole("USER");
        userRepository.save(user);

        User saved = userRepository.findById(user.getId()).orElseThrow();

        assertThat(saved.getUsername()).isEqualTo("testUser");
    }



    //il manque les tests de authentification il faut les configurer avec Spring config

}