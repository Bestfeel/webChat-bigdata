package com.gizwits.util;

import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutTextMessage;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by feel on 16/3/26.
 */
public class MessageUtil {


    public static void sendXMLTextMessag(WxCpXmlMessage inMessage, WxCpInMemoryConfigStorage wxCpConfigStorage, HttpServletResponse response, String content) {
        try {

            WxCpXmlOutTextMessage message = WxCpXmlOutTextMessage
                    .TEXT().content(content)
                    .fromUser(inMessage.getToUserName())
                    .toUser(inMessage.getFromUserName()).build();

            if (message != null) {
                // 将需要同步回复的消息加密，然后再返回给微信平台
                response.getWriter().write(message.toEncryptedXml(wxCpConfigStorage));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
