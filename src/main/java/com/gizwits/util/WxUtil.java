package com.gizwits.util;

import com.gizwits.domain.WxMemoryConfig;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.util.crypto.WxCpCryptUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by feel on 16/3/27.
 */
public class WxUtil {

    public static WxCpInMemoryConfigStorage getWxServiceConfig(String path) {

        WxMemoryConfig wxMemoryConfig = WxMemoryConfigUtil.getWxMemoryConfig(path);
        WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
        config.setCorpId(wxMemoryConfig.getCorpId());      // 设置微信企业号的appid
        config.setCorpSecret(wxMemoryConfig.getCorpSecret());  // 设置微信企业号的app corpSecret
        config.setAgentId(wxMemoryConfig.getCorpId());     // 设置微信企业号应用ID
        config.setToken(wxMemoryConfig.getToken());       // 设置微信企业号应用的token
        config.setAesKey(wxMemoryConfig.getAesKey());      // 设置微信企业号应用的EncodingAESKey
        return config;
    }

    public static boolean chechCryp(HttpServletResponse response, String msgSignature, String nonce, String timestamp, String echostr, WxCpInMemoryConfigStorage wxCpConfigStorage, WxCpServiceImpl wxCpService) {
        if (StringUtils.isNotBlank(echostr)) {
            if (!wxCpService.checkSignature(msgSignature, timestamp, nonce, echostr)) {
                // 消息签名不正确，说明不是公众平台发过来的消息
                try {
                    response.getWriter().println("非法请求");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            WxCpCryptUtil cryptUtil = new WxCpCryptUtil(wxCpConfigStorage);
            String plainText = cryptUtil.decrypt(echostr);
            // 说明是一个仅仅用来验证的请求，回显echostr
            try {
                response.getWriter().println(plainText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;

        }
        return false;
    }

}
