package com.edu.bupt.pcs.consult.utils;

public class StatusUtil {
    
    //咨询师预约状态类型转换
    public String getReserveStatus(short status){
        switch (status) {
            case 0 :
                return "已取消";
            case 1 :
                return "已预约";
            case 2:
                return "爽约";
            case 3:
                return "迟到完成评价";
            case 4:
                return "正常完成评价";
            default:
                return null;
        }
    }
}
