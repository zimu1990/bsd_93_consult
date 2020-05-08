package com.edu.bupt.pcs.consult.service.serviceImp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: wzz
 * @date: 19-7-7 下午3:34
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceImplTest {

    @Autowired
    private QuestionServiceImpl questionService;
    @Test
    public void getAnswers() {
        System.out.println(questionService.getAnswers("ff8081816bb69260016bb696f0aa0000"));
    }
}