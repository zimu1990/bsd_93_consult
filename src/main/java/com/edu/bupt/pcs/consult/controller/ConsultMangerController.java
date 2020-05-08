package com.edu.bupt.pcs.consult.controller;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorDictDetailEntity;
import cn.edu.bupt.pcsmavenjpa.entity.CounselorEntity;
import cn.edu.bupt.pcsmavenoauth2.pojo.UserPOJO;
import cn.edu.bupt.pcsmavenoauth2.util.Oauth2User;
import com.edu.bupt.pcs.consult.dto.ConselorDTO2;
import com.edu.bupt.pcs.consult.dto.QueryCounselorDTO;
import com.edu.bupt.pcs.consult.dto.SpecialtyDto;
import com.edu.bupt.pcs.consult.repository.CounselorDictDetailRepository;
import com.edu.bupt.pcs.consult.repository.CounselorRepository;
import com.edu.bupt.pcs.consult.service.CounselorService;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: wzz
 * @date: 19-6-5 下午4:42
 * @description
 */

@Api(value = "ConsultMangerController", tags = "咨询师管理模块")
@RestController
public class ConsultMangerController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private CounselorRepository counselorRepository;
    @Autowired
    private CounselorDictDetailRepository counselorDictDetailRepository;

    /**
     * dictId为数据库表dict中“擅长领域”的id
     */
    private static final int dictId = 4;

    @ApiOperation(value = "删除问题")
    @DeleteMapping(value = "/deleteQuestions")
    public Object deleteQuestions(@ApiParam(name = "questionId", value = "问题id") @RequestParam String questionId) {
        return new CommonResult().success("成功");
    }
    @ApiOperation(value = "删除回复")
    @DeleteMapping(value = "/deleteReply")
    public Object deleteReply(@ApiParam(name = "questionEntity", value = "回复Id") @RequestParam String replyId) {
        return new CommonResult().success("成功");
    }
    @ApiOperation(value = "废除咨询师")
    @DeleteMapping(value = "/deleteCounselor")
    public Object deleteCounselor(@ApiParam(name = "counselorId", value = "咨询师Id", required = true)@RequestParam Integer counselorId) {
        return counselorService.modifyCounselor(counselorId);
    }

    @ApiOperation(value = "添加咨询师")
    @PostMapping(value = "/addCounselor")
    public Object addCounselor(@RequestBody ConselorDTO2 conselorDTO2, OAuth2Authentication oAuth2Authentication) {

        UserPOJO user = (UserPOJO) Oauth2User.getUser(oAuth2Authentication);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        if (counselorRepository.findByPhoneNumber(conselorDTO2.getPhoneNumber()) != null) {
            return new CommonResult().failed("当前手机号码已注册！");
        }
        conselorDTO2.setCreateTime(timestamp);
        //conselorDTO2.setStatus("审核通过");
        conselorDTO2.setStatus("未提交");
//        conselorDTO2.setAuditorId(1);
        conselorDTO2.setAuditorId(user.getId());
        conselorDTO2.setAuditTime(timestamp);
        //电话号码赋给用户名
        conselorDTO2.setUsername(conselorDTO2.getPhoneNumber());
        //密码设置为电话号码
        conselorDTO2.setPassword(new BCryptPasswordEncoder().encode(conselorDTO2.getPhoneNumber()));
        conselorDTO2.setUnitId(user.getUnitId());
        CounselorEntity counselorEntity = new CounselorEntity();
        BeanUtils.copyProperties(conselorDTO2,counselorEntity);
        CounselorEntity counselor = counselorRepository.save(counselorEntity);
        conselorDTO2.getTypes().forEach(c->counselorDictDetailRepository.save(new CounselorDictDetailEntity(counselor.getId(),c)));
        return new CommonResult().success("成功");

    }

    /**
     * 审核咨询师，SUCCESS代表通过，FAIL代表不通过
     * @param oAuth2Authentication   登录用户
     * @param counselorId   咨询师ID
     * @param operation     SUCCESS 或者 FAIL
     * @param feedback      不通过的反馈原因
     * @return  操作信息
     */
    @ApiOperation(value = "审核咨询师")
    @PutMapping(value = "/audit")
    public CommonResult auditCounselor(OAuth2Authentication oAuth2Authentication,Integer counselorId, String operation, String feedback){
        UserPOJO user = (UserPOJO) Oauth2User.getUser(oAuth2Authentication);
        if(user == null)
            return new CommonResult().failed("用户未登录");
        Optional<CounselorEntity> optional = counselorRepository.findById(counselorId);
        CounselorEntity counselorEntity;
        if(optional.isPresent())
            counselorEntity = optional.get();
        else
            return new CommonResult().failed("咨询师不存在");
        if ("SUCCESS".equals(operation)){
            counselorEntity.setStatus("审核通过");
            counselorEntity.setAuditTime(new Timestamp(new Date().getTime()));
            counselorEntity.setAuditorId(user.getId());
            counselorRepository.save(counselorEntity);
        }else if ("FAIL".equals(operation)){
            String nowStatus = counselorEntity.getStatus();
            if ("待审核".equals(nowStatus) || "未提交".equals(nowStatus)){
                counselorEntity.setStatus("审核不通过");
            }else if ("待复审".equals(nowStatus)){
                counselorEntity.setStatus("复审不通过");
            }
            counselorEntity.setAuditFeedback(feedback);
            counselorEntity.setAuditTime(new Timestamp(new Date().getTime()));
            counselorEntity.setAuditorId(user.getId());
            counselorRepository.save(counselorEntity);
        }
        return new CommonResult().success("成功");
    }

    /*@ApiOperation(value = "审核不通过")
    @PutMapping(value = "/auditFail")
    public CommonResult auditFail(OAuth2Authentication oAuth2Authentication,Integer counselorId,String feedback){
        UserPOJO user = (UserPOJO) Oauth2User.getUser(oAuth2Authentication);
        Optional<CounselorEntity> optional = counselorRepository.findById(counselorId);
        CounselorEntity counselorEntity;
        if(optional.isPresent())
            counselorEntity = optional.get();
        else
            return new CommonResult().failed("咨询师不存在");
        String nowStatus = counselorEntity.getStatus();
        if ("待审核".equals(nowStatus) || "未提交".equals(nowStatus)){
            counselorEntity.setStatus("审核不通过");
        }else if ("待复审".equals(nowStatus)){
            counselorEntity.setStatus("复审不通过");
        }
        counselorEntity.setAuditFeedback(feedback);
        counselorEntity.setAuditTime(new Timestamp(new Date().getTime()));
        counselorEntity.setAuditorId(user.getId());
        counselorRepository.save(counselorEntity);
        return new CommonResult().success("成功");
    }*/

    /**
     * 启用废除的咨询师
     * @param oAuth2Authentication  管理账户
     * @param counselorId   废除咨询师ID
     * @return  启用信息
     */
    @ApiOperation(value = "启用咨询师")
    @PutMapping(value = "/enableCounselor")
    public CommonResult enableCounselor(OAuth2Authentication oAuth2Authentication,Integer counselorId){
        UserPOJO user = (UserPOJO) Oauth2User.getUser(oAuth2Authentication);
        Optional<CounselorEntity> optional = counselorRepository.findById(counselorId);
        CounselorEntity counselorEntity;
        if(optional.isPresent())
            counselorEntity = optional.get();
        else
            return new CommonResult().failed("咨询师不存在");
        String nowStatus = counselorEntity.getStatus();
        if("废除".equals(nowStatus)){
            counselorEntity.setStatus("未提交");
            counselorEntity.setAuditTime(new Timestamp(new Date().getTime()));
            counselorEntity.setAuditorId(user.getId());
            counselorRepository.save(counselorEntity);
        }else
            return new CommonResult().success("状态未废除");
        return new CommonResult().success("成功");
    }

    @ApiOperation(value = "修改咨询师")
    @PostMapping(value = "/updateCounselor")
    @Transactional
    public Object updateCounselor(@RequestBody CounselorEntity counselorEntity) {

        counselorRepository.save(counselorEntity);
        return new CommonResult().success("成功");
    }

    /**
     * 查询咨询师列表
     * @param queryCounselorDTO  查询咨询师参数Dto，包括姓名、擅长领域、分页的页数与大小
     * @return  咨询师信息
     */
    @ApiOperation(value = "查询咨询师列表")
    @PutMapping(value = "/list")
    public Object getCounselorList(OAuth2Authentication oAuth2Authentication, QueryCounselorDTO queryCounselorDTO) {
        UserPOJO user = (UserPOJO) Oauth2User.getUser(oAuth2Authentication);
        String  privilegeLevel = Oauth2User.getPrivilegeLevel(oAuth2Authentication, "/list", "PUT");
        if (queryCounselorDTO.getPage() < 1)
            queryCounselorDTO.setPage(1);
        if(queryCounselorDTO.getSize() < 1)
            queryCounselorDTO.setSize(10);
        return new CommonResult().success(counselorService.getCounselorPage(queryCounselorDTO, counselorService.getUnitIdList(user.getUnitId(), privilegeLevel)));
    }

    @ApiOperation(value = "查询咨询师修改审核信息")
    @PutMapping(value = "/getAuditInfo")
    public CommonResult getAuditInfo(Integer page, Integer size){
        return counselorService.getAuditInfo(page,size);
    }

    @ApiOperation("获取擅长领域列表")
    @GetMapping(value = "/getSpecialtyList")
    public CommonResult getSpecialtyList(){
        List<SpecialtyDto> list = counselorService.getSpecialtyList(dictId);
        return new CommonResult().success(list);
    }

    /**
     * 根据用户权限获取部门的ID列表
     * @param oAuth2Authentication  用户认证
     * @param url   操作行为的URL
     * @param method    操作行为的方法种类
     * @return  部门ID列表
     */
    @ApiOperation("依权限获取部门ID列表")
    @GetMapping(value = "/getUnitIdList")
    public CommonResult getUnitIdList(OAuth2Authentication oAuth2Authentication, String url, String method){
        UserPOJO user = (UserPOJO)Oauth2User.getUser(oAuth2Authentication);
        if(user == null)
            return new CommonResult().failed("用户未登录");
        String privilegeLevel = Oauth2User.getPrivilegeLevel(oAuth2Authentication, url, method);
        System.out.println(url);
        System.out.println(privilegeLevel);
        System.out.println(user.getUnitId());
        return new CommonResult().success(counselorService.getUnitIdList(user.getUnitId(), privilegeLevel));
    }

    @ApiOperation("依权限获取部门ID列表（外部服务调用使用）")
    @GetMapping(value = "/getUnitIdListExternal")
    public List<Integer> getUnitIdListExternal(Integer unitId, String privilegeLevel){
        return counselorService.getUnitIdList(unitId, privilegeLevel);
    }


}
