package com.lzs.chat.base.codec;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

/**
 * <一句话说明功能>
 * <功能详细描述>
 *
 * @author fugui
 * @title <一句话说明功能>
 * @date 2020/4/8 17:11
 * @since <版本号>
 */
public class WebSocketMessageEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
        ByteBuf result = null;
        if (msg instanceof MessageLite) {
            result = wrappedBuffer(((MessageLite) msg).toByteArray());
        }
        if (msg instanceof MessageLite.Builder) {
            result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
        }
        // ==== 上面代码片段是拷贝自TCP ProtobufEncoder 源码 ====
        // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的
        WebSocketFrame frame = new BinaryWebSocketFrame(result);
        out.add(frame);
    }
}
