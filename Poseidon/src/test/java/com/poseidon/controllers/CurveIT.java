package com.poseidon.controllers;
import com.poseidon.domain.CurvePoint;
import com.poseidon.repositories.CurvePointRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CurveIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @BeforeEach
    void setUp(){
        curvePointRepository.deleteAll();;
    }

    // GET
    @WithMockUser(roles = "USER")
    @Test
    void getCurvePointList_shouldReturnListView_withCurveFromDatabase() throws Exception{
        curvePointRepository.save(new CurvePoint(1,12.22,45.5));
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints",hasSize(1)));
    }

    //Get add
    @WithMockUser(roles = "USER")
    @Test
    void showAddForm_shouldReturnAddView() throws Exception{
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    // POST validate
    @Test
    @WithMockUser(roles = "USER")
    void validate_shouldPersistCurveInDatabase_andRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "11")
                        .param("term", "111")
                        .param("value", "10.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        assertThat(curvePointRepository.findAll()).hasSize(1);
        assertThat(curvePointRepository.findAll().get(0).getCurveId()).isEqualTo(11);


    }

    //Get update
    @WithMockUser(roles = "USER")
    @Test
    void showUpdateForm_shouldReturnUpdateView_withCurveFromDatabase() throws Exception{
        CurvePoint curve = curvePointRepository.save(new CurvePoint(1,12.54,11.12));
        mockMvc.perform(get("/curvePoint/update/"+ curve.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));

    }

    @Test
    @WithMockUser(roles = "USER")
    void validate_shouldNotPersist_andReturnAddView_whenValidationFails() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasErrors("curvePoint"))
                .andExpect(model().attributeExists("curvePoint"));
        assertThat(curvePointRepository.findAll().isEmpty());

    }

    //Post update
    @Test
    @WithMockUser(roles = "USER")
    void UpdateCurve_shouldUpdateInDatabase_andRedirect_whenValidInput() throws Exception {
        CurvePoint curve = curvePointRepository.save(new CurvePoint(1,10d,100d));
        mockMvc.perform(post("/curvePoint/update/"+curve.getId())
                        .param("curveId", "1")
                        .param("term", "10")
                        .param("value", "100.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        CurvePoint curveUpdated = curvePointRepository.findById((curve.getId())).orElseThrow();
        assertThat(curveUpdated.getTerm()).isEqualTo(10d);
        assertThat(curveUpdated.getValue()).isEqualTo(100d);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateCurve_shouldNotUpdate_andReturnUpdateForm_whenInvalidInput() throws Exception {
        CurvePoint curve = curvePointRepository.save(new CurvePoint(1,10d,100d));

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("curvePoint"));
        CurvePoint notchanged = curvePointRepository.findById(curve.getId()).orElseThrow();
        assertThat(notchanged.getTerm()).isEqualTo(10d);

    }

    @WithMockUser(roles = "USER")
    @Test
    void deleteCurve_shouldRemoveFromDatabase_andRedirect() throws Exception {
        // Arrange
        CurvePoint curve = curvePointRepository.save(new CurvePoint(1,10d,100d));
        // Act
        mockMvc.perform(get("/curvePoint/delete/" + curve.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        // Assert — le bid ne doit plus être en base
        assertThat(curvePointRepository.findById(curve.getId())).isEmpty();
    }


}