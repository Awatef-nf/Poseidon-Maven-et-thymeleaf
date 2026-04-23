package com.poseidon.controllers;

import com.poseidon.domain.BidList;
import com.poseidon.service.BidListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BidListService bidListService;

    // GET /bidList/list

    @WithMockUser
    @Test
    void getBidList() throws Exception {
        BidList bid = new BidList("Acc", "Type", 10d);
        when(bidListService.findAll()).thenReturn(List.of(bid));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("bidLists", hasSize(1))); // vérifie le contenu du modèle
        verify(bidListService).findAll();
    }
    // GET /bidList/add

    @WithMockUser
    @Test
    void showAddForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    // POST /bidList/validate

    @Test
    @WithMockUser
    void validate_shouldRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("bidQuantity", "10.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        verify(bidListService, times(1)).addBidlist(any(BidList.class));
    }

    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenValidationFails() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "")
                        .param("type", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasErrors("bidList"))
                .andExpect(model().attributeExists("bidList"));
        verify(bidListService, never()).addBidlist(any());
    }

    // GET /bidList/update/{id}

    @WithMockUser
    @Test
    void showUpdateForm_shouldReturnUpdateView_whenIdExists() throws Exception {
        BidList bid = new BidList("Acc", "Type", 10d);
        bid.setBidListId(1);
        when(bidListService.findById(1)).thenReturn(bid);
        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
        verify(bidListService).findById(1);
    }

    @WithMockUser
    @Test
    void showUpdateForm_shouldRedirect_whenIdNotFound() throws Exception {
        when(bidListService.findById(99)).thenThrow(new IllegalArgumentException("BidList not found"));
        mockMvc.perform(get("/bidList/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        verify(bidListService).findById(99);
    }

    // POST /bidList/update/{id}

    @Test
    @WithMockUser
    void saveUpdateBidList_shouldRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("bidQuantity", "10.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        verify(bidListService, times(1)).addBidlist(any(BidList.class));
    }

    @Test
    @WithMockUser
    void saveUpdateBidList_shouldReturnUpdateView_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("bidList"));
        verify(bidListService, never()).addBidlist(any());
    }

    // GET /bidList/delete/{id}

    @WithMockUser
    @Test
    void deleteBidList_shouldRedirect_whenIdExists() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        verify(bidListService).deleteById(1);
    }

    @WithMockUser
    @Test
    void deleteBidList_shouldRedirect_whenIdNotFound() throws Exception {
        doThrow(new IllegalArgumentException("BidList not found"))
                .when(bidListService).deleteById(99);
        mockMvc.perform(get("/bidList/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).deleteById(99);
    }
}