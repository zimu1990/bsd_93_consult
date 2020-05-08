package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.ReserveRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRecordRepository extends JpaRepository<ReserveRecordEntity, Integer> {

    Page<ReserveRecordEntity> findAll(Specification<ReserveRecordEntity> specification, Pageable pageable);
}
