package com.gizwits.controller;

import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.util.crypto.WxCpCryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by feel on 16/3/28.
 */
@Controller
@RequestMapping("/")
public class Common {

    private static Logger logger = LoggerFactory.getLogger(WebChatToken.class.getName());

    @Value("#{webChatProperties['corpId']}")
    private String corpId;
    @Value("#{webChatProperties['corpSecret']}")
    private String corpSecret;
    @Value("#{webChatProperties['agentid']}")
    private String agentid;
    @Value("#{webChatProperties['token']}")
    private String token;
    @Value("#{webChatProperties['encodingAESKey']}")
    private String aesKey;

    @RequestMapping(value = "common")
    public void common(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String msgSignature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String echostr = request.getParameter("echostr");
        WxCpInMemoryConfigStorage wxCpConfigStorage = new WxCpInMemoryConfigStorage();
        wxCpConfigStorage.setCorpId(corpId);      // 设置微信企业号的appid
        wxCpConfigStorage.setCorpSecret(corpSecret);  // 设置微信企业号的app corpSecret
        wxCpConfigStorage.setAgentId(agentid);     // 设置微信企业号应用ID
        wxCpConfigStorage.setToken(token);       // 设置微信企业号应用的token
        wxCpConfigStorage.setAesKey(aesKey);      // 设置微信企业号应用的EncodingAESKey
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);
        if (StringUtils.isNotBlank(echostr)) {
            if (!wxCpService.checkSignature(msgSignature, timestamp, nonce, echostr)) {
                // 消息签名不正确，说明不是公众平台发过来的消息
                try {
                    response.getWriter().println("非法请求");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            WxCpCryptUtil cryptUtil = new WxCpCryptUtil(wxCpConfigStorage);
            String plainText = cryptUtil.decrypt(echostr);
            // 说明是一个仅仅用来验证的请求，回显echostr
            try {
                response.getWriter().println(plainText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

    }
}
