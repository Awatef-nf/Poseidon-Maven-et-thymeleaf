package com.poseidon.repositories;

import com.poseidon.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
    List<RuleName> id(Integer id);
}
