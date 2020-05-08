package com.edu.bupt.pcs.consult.controller;

import cn.edu.bupt.pcsmavenjpa.entity.CounselorConfigEntity;
import com.edu.bupt.pcs.consult.service.CounselorService;
import com.edu.bupt.pcs.consult.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Api(value = "CounselorConfigController", tags = "咨询师预约参数配置模块")
@RestController
@RequestMapping("/config")
public class CounselorConfigController {

    @Autowired
    private CounselorService counselorService;

    /**
     * 获取咨询师预约配置信息
     * @return  咨询师预约配置信息实体类
     */
    @ApiOperation("获取咨询师预约配置信息")
    @GetMapping(value = "/getConfig")
    private CommonResult getCounselorConfig(){
        CounselorConfigEntity entity = counselorService.getConfiguration();
        return entity != null ? new CommonResult().success(entity) : new CommonResult().failed("无参数配置信息。");
    }

    /**
     * 更新咨询师预约配置信息
     * @param entity  咨询师预约配置实体类，属性包括每日允许预约次数，用户取消预约的次数，取消预约超限的惩罚天数和咨询爽约惩罚天数
     * @return  成功或失败信息
     */
    @ApiOperation("更新咨询师预约配置信息")
    @PostMapping(value = "/updateConfig")
    private CommonResult updateCounselorConfig(@ApiParam(value = "{\"id\": 0,\n" +
            "\"perdayNumber\": 0,\n" +
            "\"cancelLimit\": 0,\n" +
            "\"cancelPunish\": 0,\n" +
            "\"sanction\": 0\n" +
            "\"duration\": 0\n" +
            "}") @RequestBody CounselorConfigEntity entity){
        return counselorService.updateConfiguration(entity) == 0 ? new CommonResult().success("成功") : new CommonResult().failed("更新信息失败。");
    }
}
