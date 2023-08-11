package com.telcotek.uaservice.repository;

import com.telcotek.uaservice.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
}