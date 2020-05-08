package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.QuestionCounselorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface QuestionCounselorRepository extends JpaRepository<QuestionCounselorEntity,Long> {


    public ArrayList<QuestionCounselorEntity> findByCounselorId(Integer counselorId);
}
