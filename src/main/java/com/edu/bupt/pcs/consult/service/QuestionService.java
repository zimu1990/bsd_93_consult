package com.edu.bupt.pcs.consult.service;

import cn.edu.bupt.pcsmavenjpa.entity.AnswerEntity;
import cn.edu.bupt.pcsmavenjpa.entity.QuestionEntity;
import cn.edu.bupt.pcsmavenjpa.entity.ReplyEntity;
import com.edu.bupt.pcs.consult.dto.*;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import org.springframework.data.domain.Page;

/**
 * @author: wzz
 * @date: 19-6-13 下午9:01
 * @description
 */
public interface QuestionService {
    /**
     *提问题
     * @param queryQuestionDTO
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    Page<QuestionDTO2> getQuestionPage(QueryQuestionDTO queryQuestionDTO);
    CommonResult addQuestion(QuestionDTO questionDTO);
    CommonResult addReply(ReplyEntity replyEntity);
    CommonResult addAnswer(AnswerEntity answerEntity);
    CommonResult getAppointQuestions(Integer counselorId);
    CommonResult getAnswers(String questionId);
    CommonResult getReplies(String answerId);
    CommonResult getPublicQuestions(Integer testeeId);
    CommonResult getPublicQuestions2(Integer pageNum, Integer pageSize);
    CommonResult getQuestionType();
    CommonResult updateQuestionStatus(String questionId);
}
