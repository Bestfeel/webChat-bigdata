package com.gizwits.util;

import com.gizwits.domain.WxMemoryConfig;
import com.typesafe.config.Config;
import jodd.props.Props;

/**
 * Created by feel on 16/3/27.
 */

public class WxMemoryConfigUtil {

    private static final String prefix = "@";


    public static WxMemoryConfig getWxMemoryConfig(String path) {

        WxMemoryConfig wxMemoryConfig = new WxMemoryConfig();

        //  getinitConfig(path, wxMemoryConfig);

        getinitProps(path, wxMemoryConfig);
        return wxMemoryConfig;
    }

    private static void getinitProps(String path, WxMemoryConfig wxMemoryConfig) {
        Props props = JsonConfig.props(path);

        wxMemoryConfig.setTouser(props.getValue("touser"));
        wxMemoryConfig.setTotag(props.getValue("totag"));
        wxMemoryConfig.setToparty(props.getValue("toparty"));
        wxMemoryConfig.setMsgtype(props.getValue("msgtype"));
        wxMemoryConfig.setAgentid(props.getValue("agentid"));
        wxMemoryConfig.setSafe(props.getValue("safe"));
        wxMemoryConfig.setAesKey(props.getValue("encodingAESKey"));
        wxMemoryConfig.setCorpId(props.getValue("corpId"));
        wxMemoryConfig.setCorpSecret(props.getValue("corpSecret"));

        wxMemoryConfig.setToken(props.getValue("token"));
    }

    private static void getinitConfig(String path, WxMemoryConfig wxMemoryConfig) {
        Config config = JsonConfig.config(path);
        wxMemoryConfig.setTouser(prefix + config.getString("touser"));
        wxMemoryConfig.setTotag(prefix + config.getString("totag"));
        wxMemoryConfig.setToparty(prefix + config.getString("toparty"));
        wxMemoryConfig.setMsgtype(config.getString("msgtype"));
        wxMemoryConfig.setAgentid(config.getString("agentid"));
        wxMemoryConfig.setSafe(config.getString("safe"));
        wxMemoryConfig.setAesKey(config.getString("encodingAESKey"));
        wxMemoryConfig.setCorpId(config.getString("corpId"));
        wxMemoryConfig.setCorpSecret(config.getString("corpSecret"));

        wxMemoryConfig.setToken(config.getString("token"));
    }


    public static void main(String[] args) {

        WxMemoryConfigUtil wxMemoryConfigUtil = new WxMemoryConfigUtil();
        WxMemoryConfig wxMemoryConfig = wxMemoryConfigUtil.getWxMemoryConfig("tuling.props");

        System.out.println(wxMemoryConfig);
    }
}
