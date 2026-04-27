package com.poseidon.controllers;
import com.poseidon.domain.CurvePoint;
import com.poseidon.service.CurvePointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurveControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurvePointService curvePointService;

    // GET tests List
    @WithMockUser
    @Test
    void getCurvePointList() throws Exception{
        CurvePoint curve = new CurvePoint(1,12.22,45.5);
        when(curvePointService.findAll()).thenReturn(List.of(new CurvePoint()));
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints",hasSize(1)));
        verify(curvePointService).findAll();
    }

    //Get add
    @WithMockUser
    @Test
    void showAddForm() throws Exception{
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    // POST validate
    @Test
    @WithMockUser
    void validate_shouldRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "11")
                        .param("term", "111")
                        .param("value", "10.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).addCurvePoint(any(CurvePoint.class));
    }

    @Test
    @WithMockUser
    void validate_shouldReturnAddView_whenValidationFails() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasErrors("curvePoint"))
                .andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService, never()).addCurvePoint(any());
    }

    //Get update
    @WithMockUser
    @Test
    void showUpdateForm_shouldReturnUpdateView_whenIdExists() throws Exception{
        CurvePoint curve = new CurvePoint(1,12.54,11.12);
        when(curvePointService.findById(1)).thenReturn(curve);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));
        verify(curvePointService).findById(1);
    }

    @WithMockUser
    @Test
    void showUpdateForm_shouldRedirect_whenIdNotFound() throws Exception {
        when(curvePointService.findById(99)).thenThrow(new IllegalArgumentException("BidList not found"));

        mockMvc.perform(get("/curvePoint/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).findById(99);
    }

    //Post update
    @Test
    @WithMockUser
    void saveUpdateCurve_shouldRedirect_whenValidInput() throws Exception {

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", "1")
                        .param("term", "10")
                        .param("value", "100.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).addCurvePoint(any(CurvePoint.class));
    }

    @Test
    @WithMockUser
    void saveUpdateCurve_shouldReturnForm_whenInvalidInput() throws Exception {

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService, never()).addCurvePoint(any());
    }


    //Get delete
    @WithMockUser
    @Test
    void deleteCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).deleteById(1);

    }

    @WithMockUser
    @Test
    void deleteBidList_shouldRedirect_whenIdNotFound() throws Exception {
        doThrow(new IllegalArgumentException("BidList not found"))
                .when(curvePointService).deleteById(99);

        mockMvc.perform(get("/curvePoint/delete/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService).deleteById(99);
    }

}