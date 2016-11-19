package com.gizwits.controller;

import com.gizwits.domain.WxMemoryConfig;
import com.gizwits.util.WxMemoryConfigUtil;
import com.gizwits.util.WxUtil;
import com.google.gson.Gson;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.util.crypto.WxCpCryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by feel on 16/3/25.
 */
@Controller
@RequestMapping("/")
public class HelloWorld {
    private static Logger logger = LoggerFactory.getLogger(HelloWorld.class.getName());


    private Gson gson = new Gson();

    // private static final String webChat = "webChat.conf";
    private static final String webChat = "webChat.props";


    @RequestMapping(value = "test1")
    public String test1() {

        logger.info("访问hello...");


        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(webChat);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);

        WxMemoryConfig wxMemoryConfig = WxMemoryConfigUtil.getWxMemoryConfig(webChat);

        WxCpMessage message = WxCpMessage
                .TEXT()
                .agentId(wxMemoryConfig.getAgentid())
                .toUser(wxMemoryConfig.getTouser())
                .toParty(wxMemoryConfig.getToparty())
                .toTag(wxMemoryConfig.getTotag())
                .content("测试 WxCpMessage  " + new Date())
                .build();

        try {
            wxCpService.messageSend(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    @RequestMapping(value = "menu")
    public String menu() {


// url  :http://qydev.weixin.qq.com/wiki/index.php?title=创建应用菜单

        logger.info("获取 menu");

        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(webChat);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);

        WxMenu wxMenu = new WxMenu();

        List<WxMenu.WxMenuButton> buttons = new ArrayList<WxMenu.WxMenuButton>();

        WxMenu.WxMenuButton menuButton = new WxMenu.WxMenuButton();
        menuButton.setName("内存监控");

        menuButton.setType("click");

        menuButton.setKey("raw");

        buttons.add(menuButton);

        wxMenu.setButtons(buttons);

        // 设置菜单
        try {
            wxCpService.menuCreate(wxMenu);


        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "hello";

    }


    @RequestMapping(value = "test3")
    public void test3(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String msgSignature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String echostr = request.getParameter("echostr");
        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(webChat);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);
        if (StringUtils.isNotBlank(echostr)) {
            if (wxCpService.checkSignature(msgSignature, timestamp, nonce, echostr)) {
                WxCpCryptUtil cryptUtil = new WxCpCryptUtil(wxCpConfigStorage);
                String plainText = cryptUtil.decrypt(echostr);
                // 说明是一个仅仅用来验证的请求，回显echostr
                try {
                    response.getWriter().println(plainText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            } else {
                // 消息签名不正确，说明不是公众平台发过来的消息
                try {
                    response.getWriter().println("非法请求");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

        }

//        // 如果没有echostr，则说明传递过来的用户消息，在解密方法里会自动验证消息是否合法
        WxCpXmlMessage inMessage = null;
        try {
            inMessage = WxCpXmlMessage.fromEncryptedXml(request.getInputStream(), wxCpConfigStorage, timestamp, nonce, msgSignature);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String eventKey = inMessage.getEventKey();

        logger.info("点击key ....." + eventKey);
        logger.info(inMessage.toString());
//
//
//        try {

//            WxCpMessageRouter wxCpMessageRouter = new WxCpMessageRouter(wxCpService);
//            wxCpMessageRouter
//                    .rule()
//                    .msgType(WxConsts.XML_MSG_TEXT).content("点击key rout .....")
//                    .handler(new WxCpMessageHandler() {
//                        @Override
//                        public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService wxCpService, WxSessionManager sessionManager) throws WxErrorException {
//                            WxCpXmlOutTextMessage m = WxCpXmlOutTextMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUserName())
//                                    .toUser(wxMessage.getFromUserName()).build();
//                            return m;
//                        }
//                    })
//                    .end();
////
////            WxCpXmlOutMessage outMessage = wxCpMessageRouter.route(inMessage);
//
//
//           // logger.info(outMessage.toString());
//            WxCpXmlOutTextMessage m = WxCpXmlOutTextMessage.TEXT().content("测试加密消息").fromUser(inMessage.getToUserName())
//                    .toUser(inMessage.getFromUserName()).build();
//
//            if (m != null) {
//
//                logger.info("....................");
//                logger.info(m.toString());
//                logger.info("....................");
//                logger.info(m.toEncryptedXml(wxCpConfigStorage));
//
//
//                // 将需要同步回复的消息加密，然后再返回给微信平台
//                response.getWriter().write(m.toEncryptedXml(wxCpConfigStorage));
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        WxCpMessage message = WxCpMessage
//                .TEXT()
//                .agentId(agentid)
//                .toUser(touser)
//                .toParty(toparty)
//                .toTag(totag)
//                .content("测试 点击菜单  " + new Date())
//                .build();
//
//        try {
//            wxCpService.messageSend(message);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }


        // end


    }

    @RequestMapping(value = "webchat")
    public void webchat(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String msgSignature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String echostr = request.getParameter("echostr");
        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(webChat);

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


            // logger.info(echostr+".........");
            String plainText = cryptUtil.decrypt(echostr);
            //logger.info(plainText+"=================");
            // 说明是一个仅仅用来验证的请求，回显echostr
            try {
                response.getWriter().println(plainText);
                // response.getWriter().println("12345678901234567890123");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }


    }
    //
}