package com.uesct.neil.yuyinmt.bean;

import java.util.Date;

/**
 * Created by Neil on 2016/2/22.
 */
public class ChattingMsgItem {

    /**
     * 定义此枚举类型
     */
    public enum ChattingMsgType {
        MT, Me
    }

    //消息内容
    private String content;

    // 此枚举类型用于判断词条消息的属性(MT/Me)
    private ChattingMsgType type;

    public ChattingMsgType getType() {
        return type;
    }

    private int picMTId;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPicMTId() {
        return picMTId;
    }

    public void setPicMTId(int picMTId) {
        this.picMTId = picMTId;
    }

    public ChattingMsgItem(String content, ChattingMsgType type) {
        this.content = content;
        this.type = type;
        picMTId = 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return (this.type == ChattingMsgType.Me ? "Me:" : "MT:") + this.getContent();
    }
}
