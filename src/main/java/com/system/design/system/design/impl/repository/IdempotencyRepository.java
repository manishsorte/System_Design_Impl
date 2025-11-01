package com.system.design.system.design.impl.repository;

import com.system.design.system.design.impl.entity.IdempotentRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyRepository extends JpaRepository<IdempotentRecords, String> {
}
