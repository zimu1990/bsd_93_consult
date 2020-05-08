package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorUpdateApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: wzz
 * @date: 19-7-20 下午5:26
 * @description
 */
@Repository
public interface CounselorUpdateApplicationRepository extends JpaRepository<CounselorUpdateApplicationEntity,Integer> {
}
