package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselorScheduleRepository extends JpaRepository<CounselorScheduleEntity, Integer> {

    List<CounselorScheduleEntity> findByCounselorIdIn(List<Integer> collect);


}
