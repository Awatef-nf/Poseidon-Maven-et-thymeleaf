package com.poseidon.controllers;

import com.poseidon.domain.BidList;
import com.poseidon.service.BidListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

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

    @WithMockUser
    @Test
    void getBidList() throws Exception {
        when(bidListService.findAll()).thenReturn(List.of(new BidList()));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));
    }


    @WithMockUser
    @Test
    void showAddForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }
    @WithMockUser
    @Test
    void showUpdateForm() throws Exception {
        BidList bid = new BidList("Acc", "Type", 10d);
        when(bidListService.findById(1)).thenReturn(bid);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }


    @WithMockUser
    @Test
    void deleteBidList() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).deleteById(1);
    }


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
                        .param("account", "") // champ vide = invalide si @NotBlank
                        .param("type", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasErrors("bidList"))
                .andExpect(model().attributeExists("bidList"));

        verify(bidListService, never()).addBidlist(any());
    }
    @Test
    @WithMockUser
    void saveUpdateCurve_shouldRedirect_whenValidInput() throws Exception {

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
    void saveUpdateCurve_shouldReturnForm_whenInvalidInput() throws Exception {

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


}




