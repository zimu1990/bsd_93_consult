package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.FeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long>, JpaSpecificationExecutor<FeedbackEntity> {
//    List<FeedbackEntity> findAllByOrderByCreateTimeDesc(Specification<FeedbackEntity> specification);
    List<FeedbackEntity> findByCreateTimeBetweenAndContentLikeOrderByCreateTime(Timestamp startTime, Timestamp endTime, String content);
//    List<FeedbackEntity> findAllByOrdOrderByCreateTime(Specification<FeedbackEntity> specification);

    //参数构造不能为null，会报"java.lang.NullPointerException"，所以改为""
    //@Query(value = "select new com.edu.bupt.pcs.consult.dto.FeedbackDto(f.id, f.createrId, f.createType, '', f.content, f.createTime) from FeedbackEntity f ")
    //Query会使Specification失效，不能一起用
    Page<FeedbackEntity> findAll(Specification specification, Pageable pageable);


}
