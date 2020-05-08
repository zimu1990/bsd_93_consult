package com.edu.bupt.pcs.consult.repository;


import cn.edu.bupt.pcsmavenjpa.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午1:44
 * @description
 */
@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity,String> {

    List<QuestionEntity> findAllByIdInOrderByCreateTimeDesc(ArrayList<String> id);

    List<QuestionEntity> findByTesteeIdOrIsPublicOrderByCreateTimeDesc(Integer testeeId, String isPublic);

    @Query(value = "select q.* from question q where q.status = 1 and q.testee_id =:testeeId order by create_time desc",nativeQuery = true)
    List<QuestionEntity> findByTesteeIdOrderByCreateTimeDesc(Integer testeeId);

    List<QuestionEntity>  findByIsPublicOrderByCreateTimeDesc(String isPublic);

    Page<QuestionEntity> findAll(Pageable pageable);

    Page<QuestionEntity> findAll(Specification<QuestionEntity> specification, Pageable pageable);

    @Query(value="SELECT question.id,question.title,question.content,question.type,question.testee_id,true_name,img_url,question.is_anonymous,question.is_public,question.create_time,question.score,question.status,update_time,\n" +
            "\tCASE WHEN update_time IS NULL THEN create_time ELSE update_time\n" +
            "\tEND AS latest_time\n" +
            "FROM question LEFT JOIN \n" +
            "(SELECT res.question_id, MAX(update_time) AS update_time FROM\n" +
            "(SELECT a.id,a.question_id,a.content,a.create_time,rpy.r_update_time,\n" +
            "\tCASE WHEN rpy.r_update_time IS NULL THEN a.create_time\n" +
            "\tWHEN a.create_time>rpy.r_update_time THEN a.create_time ELSE rpy.r_update_time \n" +
            "\tEND AS update_time\n" +
            "FROM answer a LEFT JOIN\n" +
            "(SELECT r.answer_id, MAX(r.create_time) AS r_update_time FROM reply AS r GROUP BY r.answer_id) AS rpy ON\n" +
            "a.id=rpy.answer_id) AS res GROUP BY res.question_id) AS a ON \n" +
            "question.id=a.question_id LEFT JOIN testee ON question.testee_id=testee.id WHERE question.status=1 AND question.is_public='true' ORDER BY latest_time DESC;",nativeQuery =true)
    List<Object>  findAllOrderByCreateTimeDesc();


}
