package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import io.netty.channel.Channel;

/**
 * 操作类抽象方法
 */
public abstract class AbstractOperation implements Operation {

    protected String getKey(Channel ch) {
        return ch.attr(AppConstants.KEY_CONN_ID).get();
    }

    protected void setKey(Channel ch, String key) {
        ch.attr(AppConstants.KEY_CONN_ID).set(key);
    }

}
