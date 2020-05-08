package com.edu.bupt.pcs.consult.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;


/**
 * <dependency>
 *             <groupId>com.aliyun</groupId>
 *             <artifactId>aliyun-java-sdk-core</artifactId>
 *             <version>4.0.3</version>
 *         </dependency>
 */
public class SmsUtils {

    public static void sendSms(String code,String telephone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIj4d1WhBfoCMn", "ipZ6eu8rAvUmtv41AMvodk5RQWWaEH");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", telephone);
        request.putQueryParameter("SignName", "学能通");
        request.putQueryParameter("TemplateCode", "SMS_157689308");
        request.putQueryParameter("TemplateParam","{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            code="";
            e.printStackTrace();
        } catch (ClientException e) {
            code="";
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SmsUtils.sendSms("123456","13051617732");
    }
}
