package com.poseidon.service;

import com.poseidon.domain.RuleName;
import com.poseidon.repositories.CurvePointRepository;
import com.poseidon.repositories.RuleNameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {
    @Mock
    private RuleNameRepository ruleNameRepository;
    @InjectMocks
    private RuleNameService ruleNameService;

    @Test
    void findAllTest(){
        RuleName rule1 = new RuleName("name","description","json");
        RuleName rule2 = new RuleName("name","description","json");

        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(rule1,rule2));

        assertEquals(2, ruleNameService.findAll().size());

    }

    @Test
    void findByIdTest(){
        RuleName rule = new RuleName("name","description","json");
        rule.setId(1);

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(rule));

        RuleName result = ruleNameService.findById(1);
        assertEquals(1, result.getId());
    }

    @Test
    void FindByIDNotFoundTest() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            ruleNameService.findById(1);
        });
    }

    @Test
    void addRuleTest(){
        RuleName rule = new RuleName("name","description","json");
        ruleNameService.addRuleName(rule);
        verify(ruleNameRepository,times(1)).save(rule);

    }

    @Test
    void deleteByIdTest(){
        ruleNameService.deleteById(1);
        verify(ruleNameRepository, times(1)).deleteById(1);
    }


}