package com.poseidon.controllers;

import com.poseidon.domain.RuleName;
import com.poseidon.service.RuleNameService;
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
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RuleNameService ruleNameService;

    // GET list
    @WithMockUser
    @Test
    void getRuleNameList() throws Exception {
        RuleName rule = new RuleName("name", "description", "json");

        when(ruleNameService.findAll()).thenReturn(List.of(rule));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", hasSize(1)));

        verify(ruleNameService).findAll();
    }

    // GET add
    @WithMockUser
    @Test
    void showAddForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    // POST validate (create)

    @WithMockUser
    @Test
    void validate_shouldReturnAddView_whenInvalidInput() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", " ")
                        .param("description", " ")
                        .param("json", " ")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasErrors("ruleName"));

        verify(ruleNameService, never()).addRuleName(any());
    }

    // GET update form
    @WithMockUser
    @Test
    void showUpdateForm() throws Exception {
        RuleName rule = new RuleName("name", "desc", "json");
        rule.setId(1);

        when(ruleNameService.findById(1)).thenReturn(rule);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));

        verify(ruleNameService).findById(1);
    }

    @WithMockUser
    @Test
    void showUpdateForm_shouldRedirect_whenNotFound() throws Exception {
        when(ruleNameService.findById(99))
                .thenThrow(new IllegalArgumentException("RuleName not found"));

        mockMvc.perform(get("/ruleName/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @WithMockUser
    @Test
    void validate_shouldRedirect_whenValidInput() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "TestName")
                        .param("description", "TestDescription")
                        .param("json", "{}")
                        .param("template", "TestTemplate")
                        .param("sqlStr", "sqlStr")
                        .param("sqlPart", "sqlPart")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).addRuleName(any(RuleName.class));
    }

    @WithMockUser
    @Test
    void update_shouldRedirect_whenValid() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .param("name", "NewName")
                        .param("description", "NewDesc")
                        .param("json", "{}")
                        .param("template", "TestTemplate")
                        .param("sqlStr", "sqlStr")
                        .param("sqlPart", "sqlPart")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).addRuleName(any(RuleName.class));
    }

    @WithMockUser
    @Test
    void update_shouldReturnForm_whenInvalid() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .param("name", "")
                        .param("description", "")
                        .param("json", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeHasErrors("ruleName"));

        verify(ruleNameService, never()).addRuleName(any());
    }

    // DELETE
    @WithMockUser
    @Test
    void delete_shouldRedirect() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService).deleteById(1);
    }
}