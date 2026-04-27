package com.poseidon.controllers;

import com.poseidon.domain.Rating;
import com.poseidon.repositories.RatingRepository;
import com.poseidon.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RatingIT {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    private RatingRepository ratingRepository;
    // GET //list

    @BeforeEach
    void setUp(){
        ratingRepository.deleteAll();;
    }

    @WithMockUser(roles = "USER")
    @Test
    void getRating() throws Exception {
        ratingRepository.save(new Rating("moody","sand","fitch",1));
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", hasSize(1)));

    }
    // GET /add

    @WithMockUser(roles = "USER")
    @Test
    void showAddForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    // POST /bidList/validate

    @Test
    @WithMockUser(roles = "USER")
    void validate_shouldPersistRateInDatabase_andRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Test")
                        .param("sandPRating", "TestSand")
                        .param("fitchRating", "fitch")
                        .param("orderNumber","1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        assertThat(ratingRepository.findAll()).hasSize(1);
        assertThat(ratingRepository.findAll().get(0).getOrderNumber()).isEqualTo(1);

    }

//    @Test
//    @WithMockUser(roles = "USER")
//    void validate_shouldReturnAddView_whenValidationFails() throws Exception {
//        mockMvc.perform(post("/rating/validate")
//                        .param("moodysRating", " ")
//                        .param("sandPRating", " ")
//                        .param("fitchRating", " ")
//                        .param("orderNumber"," ")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("rating/add"))
//                .andExpect(model().attributeHasErrors("rating"))
//                .andExpect(model().attributeExists("rating"));
//        verify(ratingService, never()).addRating(any());
//    }

    // GET /update/{id}
//
//    @WithMockUser(roles = "USER")
//    @Test
//    void showUpdateForm_shouldReturnUpdateView_whenIdExists() throws Exception {
//        Rating rate = new Rating("moody", "sand", "fitch", 1);
//        rate.setId(1);
//        when(ratingService.findById(1)).thenReturn(rate);
//        mockMvc.perform(get("/rating/update/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("rating/update"))
//                .andExpect(model().attributeExists("rating"))
//                .andExpect(model().attribute("rating", rate));
//
//        verify(ratingService).findById(1);
//    }
//
//    @WithMockUser(roles = "USER")
//    @Test
//    void showUpdateForm_shouldRedirect_whenIdNotFound() throws Exception {
//        when(ratingService.findById(99)).thenThrow(new IllegalArgumentException("Rating not found"));
//
//        mockMvc.perform(get("/rating/update/99"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/rating/list"));
//        verify(ratingService).findById(99);
//    }
//
//    // POST/update/{id}
//
//    @Test
//    @WithMockUser(roles = "USER")
//    void saveUpdateBidList_shouldRedirect_whenValidInput() throws Exception {
//        mockMvc.perform(post("/rating/validate")
//                        .param("moodysRating", "Test")
//                        .param("sandPRating", "TestSand")
//                        .param("fitchRating", "fitch")
//                        .param("orderNumber","1")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/rating/list"));
//        verify(ratingService, times(1)).addRating(any(Rating.class));
//    }
//
//    @Test
//    @WithMockUser
//    void saveUpdateBidList_shouldReturnUpdateView_whenInvalidInput() throws Exception {
//        mockMvc.perform(post("/rating/validate")
//                        .param("moodysRating", " ")
//                        .param("sandPRating", " ")
//                        .param("fitchRating", " ")
//                        .param("orderNumber"," ")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("rating/add"))
//                .andExpect(model().attributeHasErrors("rating"))
//                .andExpect(model().attributeExists("rating"));
//        verify(ratingService, never()).addRating(any());
//    }
//
//    // GET delete/{id}
//
//    @WithMockUser
//    @Test
//    void deleteBidList_shouldRedirect_whenIdExists() throws Exception {
//        mockMvc.perform(get("/rating/delete/1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/rating/list"));
//        verify(ratingService).deleteById(1);
//    }
//
//    @WithMockUser
//    @Test
//    void deleteBidList_shouldRedirect_whenIdNotFound() throws Exception {
//        doThrow(new IllegalArgumentException("Rating not found"))
//                .when(ratingService).deleteById(99);
//        mockMvc.perform(get("/rating/delete/99"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/rating/list"));
//        verify(ratingService).deleteById(99);
//
//    }
//
//    @Test
//    @WithMockUser
//    void updateRating_shouldRedirect_whenValidInput() throws Exception {
//        mockMvc.perform(post("/rating/update/1")
//                        .param("moodysRating", "NewMoodys")
//                        .param("sandPRating", "NewSP")
//                        .param("fitchRating", "NewFitch")
//                        .param("orderNumber", "5")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/rating/list"));
//        verify(ratingService, times(1)).addRating(any(Rating.class));
//    }
//
//    @Test
//    @WithMockUser
//    void updateRating_shouldReturnUpdateView_whenInvalidInput() throws Exception {
//        mockMvc.perform(post("/rating/update/1")
//                        .param("moodysRating", "")
//                        .param("sandPRating", "")
//                        .param("fitchRating", "")
//                        .param("orderNumber", "5")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("rating/update"))
//                .andExpect(model().attributeHasErrors("rating"));
//        verify(ratingService, never()).addRating(any());
//    }

}