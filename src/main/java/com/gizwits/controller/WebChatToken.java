package com.gizwits.controller;

import com.gizwits.domain.HostContent;
import com.gizwits.domain.WxMemoryConfig;
import com.gizwits.util.DateUtil;
import com.gizwits.util.MessageUtil;
import com.gizwits.util.WxMemoryConfigUtil;
import com.gizwits.util.WxUtil;
import com.google.gson.Gson;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.gizwits.domain.MenuEvent.valueOf;

/**
 * Created by feel on 16/3/25.
 */
@RestController
@RequestMapping("/")
public class WebChatToken {
    private static Logger logger = LoggerFactory.getLogger(WebChatToken.class.getName());


    // private static final String wxConfig = "webChat.conf";
    private static final String wxConfig = "webChat.props";
    private Gson gson = new Gson();

    /**
     * <p>
     * <p>
     * curl -XPOST -H 'Content-Type: application/json'  http://localhost:8080/json.action -d '[{
     * "host":"feel",
     * "content":"服务器正常"
     * },
     * {
     * "host":"ubuntu",
     * "content":"服务器异常"
     * }]
     * '
     * <p>
     * </p>
     *
     * @param content
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/json", consumes = "application/json;", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    public void sendJson(@RequestBody List<HostContent> content, HttpServletResponse response) {

        for (int i = 0; i < content.size(); i++) {
            content.get(i).setDate(DateUtil.getDateString());
        }

        logger.info(content.toString());
        WxMemoryConfig wxMemoryConfig = WxMemoryConfigUtil.getWxMemoryConfig(wxConfig);

        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(wxConfig);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);

        WxCpMessage message = WxCpMessage
                .TEXT()
                .agentId(wxMemoryConfig.getAgentid())
                .toUser(wxMemoryConfig.getTouser())
                .toParty(wxMemoryConfig.getToparty())
                .toTag(wxMemoryConfig.getTotag())
                .content(gson.toJson(content))
                .build();

        try {
            wxCpService.messageSend(message);
            response.getWriter().println(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "init")
    public void init(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String msgSignature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String echostr = request.getParameter("echostr");
        WxCpInMemoryConfigStorage wxCpConfigStorage = WxUtil.getWxServiceConfig(wxConfig);

        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(wxCpConfigStorage);

        logger.info(wxCpConfigStorage.toString());
        logger.info("-----init -----");

      //  if (WxUtil.chechCryp(response, msgSignature, nonce, timestamp, echostr, wxCpConfigStorage, wxCpService)) return;

        logger.info("-----init end -----");

        // 如果没有echostr，则说明传递过来的用户消息，在解密方法里会自动验证消息是否合法
        WxCpXmlMessage inMessage = null;
        try {
            logger.info(inMessage.toString());
            inMessage = WxCpXmlMessage.fromEncryptedXml(request.getInputStream(), wxCpConfigStorage, timestamp, nonce, msgSignature);
            logger.info(inMessage.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String event = inMessage.getEvent();
        logger.info(inMessage.toString());

        String eventKey = inMessage.getEventKey();

        if (StringUtils.isNotEmpty(eventKey) && event.equalsIgnoreCase(WxConsts.BUTTON_CLICK)) {
            switch (valueOf(eventKey)) {
                case raw: {
                    String content = "服务器内存监控" + DateUtil.getDateString();
                    MessageUtil.sendXMLTextMessag(inMessage, wxCpConfigStorage, response, content);
                    break;
                }
                case disk: {
                    String content = "服务器磁盘监控" + DateUtil.getDateString();
                    MessageUtil.sendXMLTextMessag(inMessage, wxCpConfigStorage, response, content);
                    break;
                }
                default:
                    break;

            }
        }


        // end


    }


    //
}