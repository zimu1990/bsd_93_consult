package com.edu.bupt.pcs.consult.repository;

import cn.edu.bupt.pcsmavenjpa.entity.QuestionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: wzz
 * @date: 19-6-13 下午6:47
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionRepositoryTestPage {
    @Autowired
    private QuestionRepository questionRepository;
    @Test
    public void test(){
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent("我经常失眠？");

//        questionEntity.setType("心理问题");
        questionRepository.save(questionEntity);
    }

}