package com.edu.bupt.pcs.consult.controller;

import cn.edu.bupt.pcsmavenjpa.entity.AnswerEntity;
import cn.edu.bupt.pcsmavenjpa.entity.CounselorEntity;
import cn.edu.bupt.pcsmavenjpa.entity.CounselorUpdateApplicationEntity;
import cn.edu.bupt.pcsmavenjpa.entity.ReplyEntity;
import cn.edu.bupt.pcsmavenoauth2.pojo.CounselorPOJO;
import cn.edu.bupt.pcsmavenoauth2.pojo.TesteePOJO;
import cn.edu.bupt.pcsmavenoauth2.util.Oauth2User;
import cn.edu.bupt.pcsmavenoss.OSSUtils;
import com.edu.bupt.pcs.consult.dto.ConselorDTO2;
import com.edu.bupt.pcs.consult.dto.QueryQuestionDTO;
import com.edu.bupt.pcs.consult.repository.CounselorUpdateApplicationRepository;
import com.edu.bupt.pcs.consult.service.MessageService;
import com.edu.bupt.pcs.consult.service.serviceImp.CounselorServiceImpl;
import com.edu.bupt.pcs.consult.service.serviceImp.MessageServiceImpl;
import com.edu.bupt.pcs.consult.service.serviceImp.QuestionServiceImpl;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import com.edu.bupt.pcs.consult.utils.SmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

/**
 * @author: wzz
 * @date: 19-6-4 13:03
 * @description:
 */
@Api(value = "ConsultController", tags = "咨询师端模块")
@RestController
@Slf4j
public class ConsultController {

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private CounselorServiceImpl counselorService;
    @Autowired
    private CounselorUpdateApplicationRepository counselorUpdateApplicationRepository;

    @Autowired
    private MessageServiceImpl messageService;

    @ApiOperation(value = "咨询师注册")
    @PostMapping(value = "/registerCounselor")
    public CommonResult registerCounselor(String phoneNumber, String password, String inputCode) {
        return counselorService.registerCounselor(phoneNumber, password, inputCode);
    }

    @ApiOperation(value = "获取咨询师详情")
    @GetMapping(value = "/getCounselorInfo")
    public CommonResult getCounselorInfo(OAuth2Authentication oAuth2Authentication) {
        CounselorPOJO counselor = (CounselorPOJO) Oauth2User.getUser(oAuth2Authentication);
        Integer counselorId = counselor.getId();
        CounselorEntity counselorEntity = counselorService.getCounselorInformation(counselorId);
        counselorEntity.setAvatar(OSSUtils.getUrl(counselor.getAvatar(),8));
        return new CommonResult().success(counselorEntity);
    }

    /**
     * 完善信息提交审核
     * @param conselorDTO2
     * @return
     */
    @ApiOperation(value = "完善信息提交审核")
    @PutMapping(value = "/submitAudite")
    public Object submitAudite(ConselorDTO2 conselorDTO2) {

        return counselorService.submitAudite(conselorDTO2);
    }

    /**
     *
     * @param queryQuestionDTO
     * @return
     */
    @ApiOperation(value = "获取问题列表")
    @GetMapping(value = "/questionList")
    public CommonResult getQuestionList(QueryQuestionDTO queryQuestionDTO) {
        queryQuestionDTO.setPage(queryQuestionDTO.getPage() < 1 ? 1 : queryQuestionDTO.getPage());
        queryQuestionDTO.setSize(queryQuestionDTO.getSize() < 1 ? 10 : queryQuestionDTO.getSize());
        return new CommonResult().success(questionService.getQuestionPage(queryQuestionDTO));
    }

    @ApiOperation(value = "设置问题的下线")
    @PutMapping(value = "/questionOffline")
    public CommonResult updateQuestionStatus(String questionId){
        return questionService.updateQuestionStatus(questionId);
    }


    @ApiOperation(value = "获取问题下的答案")
    @GetMapping(value = "/getAnswers")
    public CommonResult getAnswers(String questionId) {
        return questionService.getAnswers(questionId);
    }

    @ApiOperation(value = "获取答案下的回复")
    @GetMapping(value = "/getReplies")
    public CommonResult getReplies(String answerId) {
        return questionService.getReplies(answerId);
    }

    @ApiOperation(value = "每个答案下面添加回复")
    @PutMapping(value = "/addReply")
    public CommonResult addReply(ReplyEntity replyEntity) {
        return questionService.addReply(replyEntity);
    }

    @ApiOperation(value = "回答问题")
    @PutMapping(value = "/addAnswer")
    public CommonResult addAnswer(AnswerEntity answerEntity) {
        return questionService.addAnswer(answerEntity);
    }


    @ApiOperation(value = "咨询师获取公开问题")
    @GetMapping(value = "/getPublicQuestions2")
    public CommonResult getPublicQuestions2(Integer pageNum, Integer pageSize) {
        return questionService.getPublicQuestions2( pageNum,  pageSize);

    }

    @ApiOperation(value = "咨询师根据其id获取被指定问题")
    @GetMapping(value = "/getAppointQuestions")
    public CommonResult getAppointQuestions(Integer counselorId) {
        return questionService.getAppointQuestions(counselorId);
    }

    @ApiOperation(value = "获取咨询者个人信息")
    @GetMapping(value = "/getClientInfo")
    public Object getClientInfo(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam Integer userId) {
        return new CommonResult().success("咨询者信息详情");
    }

    @ApiOperation(value = "获取咨询者测评信息")
    @GetMapping(value = "/getClientEvalutionInfo")
    public Object getClientEvalutionInfo(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam Integer userId) {
        return new CommonResult().success("获取咨询者测评信息");
    }

    @ApiOperation(value = "获取咨询者领域类型")
    @GetMapping(value = "/getCounselorType")
    public CommonResult getCounselorType() {
        return counselorService.getCounselorType();
    }

    @ApiOperation(value = "根据咨询者领域类型获取咨询师列表")
    @GetMapping(value = "/getCounselorByType")
    public CommonResult getCounselorByType(Integer typeId) {
        return counselorService.getCounselorByType(typeId);
    }

    @ApiOperation(value = "修改信息审核提交")
    @PutMapping(value = "/updateCounselorInfo")
    public CommonResult updateCounselorInfo(OAuth2Authentication oAuth2Authentication, CounselorUpdateApplicationEntity counselorUpdateApplicationEntity) {
        TesteePOJO user = (TesteePOJO) Oauth2User.getUser(oAuth2Authentication);
        counselorUpdateApplicationEntity.setApplyTime(new Timestamp(new Date().getTime()));
        counselorUpdateApplicationEntity.setAuditorId(user.getId());
        counselorUpdateApplicationRepository.save(counselorUpdateApplicationEntity);
        return new CommonResult().success("成功");
    }




    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult uploadFile( MultipartFile fileUpload){
        String upload1 = OSSUtils.upload1(fileUpload);
        log.info("上传文件返回值"+upload1);
        return new CommonResult().success(upload1);
    }

    @ApiOperation(value = "发送短信")
    @PostMapping(value = "/sendMessage" )
    public CommonResult sendMessage(String phoneNumber){
        return messageService.sendVerificationCode(phoneNumber);
    }

    @ApiOperation(value = "修改咨询师密码")
    @PutMapping(value = "/updateConselorPass")
    public Object updateConselorPass(ConselorDTO2 conselorDTO2) {
        return counselorService.updateConselorPass(conselorDTO2);
    }

    @ApiOperation(value = "忘记密码")
    @PostMapping(value = "/forgetCounselorPassword")
    public Object forgetAndResetPassword(String phoneNumber, String newPassword,String inputCode){
        return counselorService.resetNumber(phoneNumber,newPassword,inputCode);
    }

}
