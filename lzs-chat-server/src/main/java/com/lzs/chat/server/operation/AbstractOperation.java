package com.lzs.chat.server.operation;

import com.lzs.chat.base.constans.AppConstants;
import com.lzs.chat.server.exception.AuthFailException;
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

    /**
     * appkey 验证
     * @param clienToken
     * @param platformKey
     * @return
     */
    protected void authToken(String clienToken, String platformKey) throws AuthFailException {
        if (!platformKey.equals(clienToken)) {
            throw new AuthFailException();
        }
    }
}
