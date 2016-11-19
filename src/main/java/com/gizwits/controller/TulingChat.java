package com.gizwits.controller;

import com.gizwits.util.MessageUtil;
import com.gizwits.util.TulingApiProcess;
import com.gizwits.util.WxUtil;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by feel on 16/3/25.
 */
@Controller
@RequestMapping("/")
public class TulingChat {
    private static Logger logger = LoggerFactory.getLogger(TulingChat.class.getName());

    @Value("#{webChatProperties['tapiUrl']}")
    private String apiUrl;
    // private static final String tuConfig = "tuling.conf";
    private static final String tuConfig = "tuling.props";


    @RequestMapping(value = "tuling")
    public void tuling(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String msgSignature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String echostr = request.getParameter("echostr");
        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(tuConfig);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);
      //  if (WxUtil.chechCryp(response, msgSignature, nonce, timestamp, echostr, wxCpConfigStorage, wxCpService)) return;


//        // 如果没有echostr，则说明传递过来的用户消息，在解密方法里会自动验证消息是否合法
        WxCpXmlMessage inMessage = null;
        try {
            inMessage = WxCpXmlMessage.fromEncryptedXml(request.getInputStream(), wxCpConfigStorage, timestamp, nonce, msgSignature);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String event = inMessage.getEvent();

        // List<String> events = Arrays.asList("enter_agent", "LOCATION");

        List<String> events = Arrays.asList("enter_agent");


        if (events.contains(event)) {

            String content = "我是机智云机器人小妹妹,欢迎您!!!";
            MessageUtil.sendXMLTextMessag(inMessage, wxCpConfigStorage, response, content);
        }
        String Wcontent = inMessage.getContent();


        if (StringUtils.isNotBlank(Wcontent)) {
            String tulingResult = TulingApiProcess.getTulingResult(Wcontent, apiUrl);
            MessageUtil.sendXMLTextMessag(inMessage, wxCpConfigStorage, response, tulingResult);
        }


        // end


    }
    //
}