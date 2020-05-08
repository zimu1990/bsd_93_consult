package com.edu.bupt.pcs.consult.feign;

import com.edu.bupt.pcs.consult.dto.DictDetailDTO;
import cn.edu.bupt.pcsmavenjpa.entity.DictDetailEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: wzz
 * @date: 19-7-7 下午4:27
 * @description
 */
@FeignClient(name = "pcs-dict")
public interface CounselorTypeFeign {
    @RequestMapping(value = "/dict/getDetails",method = RequestMethod.GET)
    List<DictDetailEntity> getCounselorType(@RequestParam("dictName") String dictName);
}
