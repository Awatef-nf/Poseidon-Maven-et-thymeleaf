package com.poseidon.repositories;

import com.poseidon.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
    List<RuleName> id(Integer id);
}
