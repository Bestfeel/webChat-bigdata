package com.gizwits.domain;

import java.io.Serializable;

/**
 * Created by feel on 16/3/26.
 */

public class HostContent implements Serializable {

    private String host;
    private String content;
    private String date;

    public HostContent() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
