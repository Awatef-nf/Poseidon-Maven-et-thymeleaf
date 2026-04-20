package com.poseidon.service;

import com.poseidon.domain.RuleName;
import com.poseidon.repositories.RuleNameRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;
    public List<RuleName> findAll() {
        return  ruleNameRepository.findAll();
    }

    public void addRuleName(@Valid RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid rule Id: " + id));
    }

    public void deleteById(Integer id) {
        ruleNameRepository.deleteById(id);
    }
}
