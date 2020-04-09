package com.lzs.chat.base.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 协议类
 *  数据包长度 + headerLen + version + operation +seqId
 *               4        +  4      +  4        +4
 */
@Data
public class Proto implements Serializable {

    private static final long serialVersionUID = -6693247226295369370L;
    public static final short HEADER_LENGTH = 16;
    public static final short VERSION = 1;
    /**
     * 包长
     */
    private int packetLen;
    /**
     * 头信息长度
     */
    private short headerLen;
    /**
     * 版本
     */
    private short version;
    /**
     * 操作: 1,2 为认证  3,4 为 心跳  5,6 为消息
     */
    private int operation;
    /**
     *
     */
    private int seqId;

    private byte[] body;

    @Override
    public String toString() {
        String text;
        if (body == null) {
            text = "null";
        } else {
            text = new String(body);
        }
        return "Proto{" +
                "packetLen=" + packetLen +
                ", headerLen=" + headerLen +
                ", version=" + version +
                ", operation=" + operation +
                ", seqId=" + seqId +
                ", body=" + text +
                '}';
    }
}
