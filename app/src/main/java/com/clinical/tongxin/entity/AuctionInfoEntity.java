package com.clinical.tongxin.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by linchao on 2017/1/22.
 */

public class AuctionInfoEntity implements Serializable {
    @SerializedName("ServerScore")
    private String serverScore;

    @SerializedName("CompleteCnt")
    private String completeCnt;

    public String getCompleteCnt() {
        return completeCnt;
    }

    public void setCompleteCnt(String completeCnt) {
        this.completeCnt = completeCnt;
    }

    public String getServerScore() {
        return serverScore;
    }

    public void setServerScore(String serverScore) {
        this.serverScore = serverScore;
    }

}
