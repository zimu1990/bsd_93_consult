package com.edu.bupt.pcs.consult.service;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorConfigEntity;
import cn.edu.bupt.pcsmavenjpa.entity.CounselorEntity;
import cn.edu.bupt.pcsmavenjpa.entity.FeedbackEntity;
import com.edu.bupt.pcs.consult.dto.*;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: wzz
 * @date: 19-6-12 下午2:31
 * @description
 */
public interface CounselorService {


    /**
     *咨询师注册
     * @param phoneNumber
     * @param password
     * @param inputCode
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    CommonResult registerCounselor(String phoneNumber, String password, String inputCode);

    /**
     *获取咨询师信息
     * @param id
     * @return com.edu.bupt.pcs.consult.utils.CommonResult
     */
    CommonResult getCounselorInfo(Integer id);
    CommonResult getCounselorList(Integer pageNum, Integer pageSize);
    CommonResult getCounselorType();
    CommonResult getCounselorByType(Integer typeId);
    CommonResult addFeedback(FeedbackEntity feedbackEntity);
    Page<FeedbackDto> getFeedbackList(QueryFeedbackDto queryFeedbackDto);
    Object submitAudite(ConselorDTO2 conselorDTO2) ;
    Page<CounselorListDTO> getCounselorPage(QueryCounselorDTO counselorDTO, List<Integer> unitIds);
    CommonResult modifyCounselor(Integer id);
    CommonResult getAuditInfo(Integer page, Integer size);
    Object updateConselorPass(ConselorDTO2 conselorDTO2);
    Page getProcessList(QueryProcessDto processDto);
    String getConsultChatRecord(Integer reserveId, String userAgent);
    CounselorConfigEntity getConfiguration();
    int updateConfiguration(CounselorConfigEntity entity);
    List<SpecialtyDto> getSpecialtyList(int dictId);
    List<Integer> getUnitIdList(int unitId, String privilegeLevel);
    //Page<FeedbackDto> getFeedBackPage(int pageNum,Integer pageSize,String startTime,String endTime,String content);
    CommonResult resetNumber(String phoneNumber,String newPassword,String inputCode);
    CounselorEntity getCounselorInformation(Integer id);
}
