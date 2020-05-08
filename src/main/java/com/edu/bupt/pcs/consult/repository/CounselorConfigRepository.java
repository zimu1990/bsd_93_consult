package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselorConfigRepository extends JpaRepository<CounselorConfigEntity, Integer> {

}
