package com.poseidon.controllers;

import com.poseidon.domain.Trade;
import com.poseidon.service.TradeService;
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
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
class TradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;

    @WithMockUser
    @Test
    void getTradeList() throws Exception {
        Trade trade = new Trade("account","type");
        when(tradeService.findAll()).thenReturn(List.of(trade));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("trades", hasSize(1)));
        verify(tradeService).findAll();
    }
    // GET /add

    @WithMockUser
    @Test
    void showAddForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    // POST /validate

    @Test
    @WithMockUser
    void validate_shouldRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService, times(1)).addTrade(any(Trade.class));
    }

    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenValidationFails() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "")
                        .param("type", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasErrors("trade"))
                .andExpect(model().attributeExists("trade"));
        verify(tradeService, never()).addTrade(any());
    }

    // GET /update/{id}

    @WithMockUser
    @Test
    void showUpdateForm_shouldReturnUpdateView_whenIdExists() throws Exception {
        Trade trade = new Trade("account","type");
        trade.setTradeId(1);
        when(tradeService.findById(1)).thenReturn(trade);
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
        verify(tradeService).findById(1);
    }

    @WithMockUser
    @Test
    void showUpdateForm_shouldRedirect_whenIdNotFound() throws Exception {
        when(tradeService.findById(99)).thenThrow(new IllegalArgumentException("Trade not found"));
        mockMvc.perform(get("/trade/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService).findById(99);
    }

    // POST //update/{id}

    @Test
    @WithMockUser
    void saveUpdateTrade_shouldRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService, times(1)).addTrade(any(Trade.class));
    }

    @Test
    @WithMockUser
    void saveUpdateTrade_shouldReturnUpdateView_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .param("account", "")
                        .param("type", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("trade"));
        verify(tradeService, never()).addTrade(any());
    }

    // GET //delete/{id}

    @WithMockUser
    @Test
    void deleteTrade_shouldRedirect_whenIdExists() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService).deleteById(1);
    }

    @WithMockUser
    @Test
    void deleteTrade_shouldRedirect_whenIdNotFound() throws Exception {
        doThrow(new IllegalArgumentException("trade not found"))
                .when(tradeService).deleteById(99);
        mockMvc.perform(get("/trade/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).deleteById(99);
    }






}