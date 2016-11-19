package com.gizwits.domain;

import java.io.Serializable;

/**
 * Created by feel on 16/3/27.
 */
public class WxMemoryConfig implements Serializable {
    private String touser;
    private String toparty;
    private String totag;
    private String msgtype;
    private String agentid;
    private String safe;
    private String corpId;
    private String corpSecret;
    private String token;
    private String aesKey;

    public WxMemoryConfig() {
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    @Override
    public String toString() {
        return "WxMemoryConfig{" +
                "touser='" + touser + '\'' +
                ", toparty='" + toparty + '\'' +
                ", totag='" + totag + '\'' +
                ", msgtype='" + msgtype + '\'' +
                ", agentid='" + agentid + '\'' +
                ", safe='" + safe + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpSecret='" + corpSecret + '\'' +
                ", token='" + token + '\'' +
                ", aesKey='" + aesKey + '\'' +
                '}';
    }
}
