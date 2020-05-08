package com.edu.bupt.pcs.consult.controller;

import com.edu.bupt.pcs.consult.dto.ConsultProcessDto;
import com.edu.bupt.pcs.consult.dto.QueryFeedbackDto;
import com.edu.bupt.pcs.consult.dto.QueryProcessDto;
import com.edu.bupt.pcs.consult.dto.QuestionDTO;
import cn.edu.bupt.pcsmavenjpa.entity.FeedbackEntity;
import cn.edu.bupt.pcsmavenjpa.entity.QuestionEntity;
import com.edu.bupt.pcs.consult.service.serviceImp.KnowledgeBaseImpl;
import com.edu.bupt.pcs.consult.service.serviceImp.CounselorServiceImpl;
import com.edu.bupt.pcs.consult.service.serviceImp.QuestionServiceImpl;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wzz
 * @date 19-6-4 下午8:17
 * @description
 */
@Api(value = "ClientConsultController", tags = "用户端咨询模块")
@Slf4j
@RestController
public class ClientConsultController {
    @Autowired
    private KnowledgeBaseImpl commonQuestion;
    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private CounselorServiceImpl counselorService;
    @ApiOperation(value = "获取已审核咨询师列表")
    @GetMapping(value = "/getCounselorList")
    public Object getCounselorList(Integer pageNum, Integer pageSize){
        return counselorService.getCounselorList(pageNum, pageSize);
    }

    @ApiOperation(value = "提问题")
    @PutMapping(value = "/raiseQuestions")
    public Object raiseQuestions(QuestionDTO questionDTO){

        log.info("提交信息： "+questionDTO.toString());
        return questionService.addQuestion(questionDTO);
    }
    @ApiOperation(value = "获取知识库")
    @GetMapping(value = "/getKB")
    public Object getCommonQuestion(Integer page, Integer size){
        return commonQuestion.getCommonQuestion(page,size);
    }
    @ApiOperation(value = "咨询者获取自己及公开问题")
    @GetMapping(value = "/getPublicQuestions")
    public CommonResult getPublicQuestions(Integer testeeId) {
        return questionService.getPublicQuestions(testeeId);
    }
    @ApiOperation(value = "添加意见反馈")
    @PostMapping(value = "/addFeedback")
    public CommonResult addFeedback(FeedbackEntity feedbackEntity) {
        return counselorService.addFeedback(feedbackEntity);
    }

    @ApiOperation(value = "获取意见反馈列表")
    @PostMapping(value = "/getFeedback")
    public CommonResult getFeedbackList(@RequestBody QueryFeedbackDto queryFeedbackDto){
        if (queryFeedbackDto.getPage() < 1)
            queryFeedbackDto.setPage(1);
        if(queryFeedbackDto.getSize() < 1)
            queryFeedbackDto.setSize(10);
        return new CommonResult().success(counselorService.getFeedbackList(queryFeedbackDto));
    }

    @ApiOperation(value = "获取咨询过程列表")
    @PostMapping(value = "/getProcessList")
    public CommonResult getProcessList(@RequestBody QueryProcessDto processDto){
        if (processDto.getPage() < 1)
            processDto.setPage(1);
        if(processDto.getSize() < 1)
            processDto.setSize(10);
        return new CommonResult().success(counselorService.getProcessList(processDto));
    }

    @ApiOperation(value = "获取咨询过程的聊天记录")
    @GetMapping(value = "/getChatRecord")
    public CommonResult getChatRecordDetails(Integer reserveRecordId){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userAgent = "";
        if(servletRequestAttributes != null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            userAgent = request.getHeader("user-agent");
        }
        String result = counselorService.getConsultChatRecord(reserveRecordId, userAgent);
        if(StringUtils.isNotBlank(result))
            return new CommonResult().success(result);
        return new CommonResult().failed("未找到咨询聊天记录");
    }

    @ApiOperation(value = "获取问题类型")
    @GetMapping(value = "/getQuestionType")
    public CommonResult getQuestionType(){
        return new CommonResult().success(questionService.getQuestionType());
    }

//    @ApiOperation(value = "获取所有反馈")
//    @GetMapping(value = "/getAllFeedback")
//    public CommonResult getAllFeedBack(Integer pageNum,Integer pageSize,String startTime,String endTime,String content){
//        return new CommonResult().success(counselorService.getFeedBackPage(pageNum,pageSize,startTime,endTime,content));
//    }
}
