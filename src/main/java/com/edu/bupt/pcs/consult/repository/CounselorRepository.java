package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface CounselorRepository extends JpaRepository<CounselorEntity,Integer> {
    @Query(value = "select * from counselor where counselor.phone_number=:phoneNumber",nativeQuery = true)
    CounselorEntity findByPhoneNumber(String phoneNumber) ;

    List<CounselorEntity> findByTrueName(String trueName);

    List<CounselorEntity> findByStatus(String status);

    List<CounselorEntity> findByIdInAndStatus(List<Integer> collect, String status);

    //List<CounselorEntity> findByAreasOfExpertiseAndStatus(String type, String status);

    List<CounselorEntity> findAll(Specification<CounselorEntity> specification);

    @Query(value = "select c.truename from counselor c where c.id =?1",nativeQuery = true)
    String findTrueNameById(Integer id);
}
