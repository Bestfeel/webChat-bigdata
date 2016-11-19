package com.gizwits.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import jodd.props.Props;

import java.io.File;
import java.io.IOException;

/**
 * Created by feel on 15/11/14.
 * 获取conf 文件配置的工具类
 */
public class JsonConfig {

    public static Config config() {

        return ConfigFactory.load();

    }

    public static Config config(String configPath) {

        return ConfigFactory.load(configPath);

    }

    public static Props props(String path) {
        Props p = new Props();
        try {
            p.load(new File(JsonConfig.class.getResource("/" + path).getPath()));

            return p;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        Props props = JsonConfig.props("tuling.props");

        String encodingAESKey = props.getValue("touser");

        System.out.println(encodingAESKey);


    }

}