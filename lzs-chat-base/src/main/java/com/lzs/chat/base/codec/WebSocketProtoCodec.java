package com.lzs.chat.base.codec;

import com.lzs.chat.base.bean.Proto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * WebSocket 协议加解密
 */
@ChannelHandler.Sharable
@Slf4j
public class WebSocketProtoCodec extends MessageToMessageCodec<WebSocketFrame, Proto> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Proto proto, List<Object> list) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        if (proto.getBody() != null) {
            byteBuf.writeInt(Proto.HEADER_LENGTH + proto.getBody().length);
            byteBuf.writeShort(Proto.HEADER_LENGTH);
            byteBuf.writeShort(Proto.VERSION);
            byteBuf.writeInt(proto.getOperation());
            byteBuf.writeInt(proto.getSeqId());
            byteBuf.writeBytes(proto.getBody());
        } else {
            byteBuf.writeInt(Proto.HEADER_LENGTH);
            byteBuf.writeShort(Proto.HEADER_LENGTH);
            byteBuf.writeShort(Proto.VERSION);
            byteBuf.writeInt(proto.getOperation());
            byteBuf.writeInt(proto.getSeqId());
        }

        list.add(new BinaryWebSocketFrame(byteBuf));

        log.debug("encode: {}", proto);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame, List<Object> list) throws Exception {
        ByteBuf byteBuf = webSocketFrame.content();
        Proto proto = new Proto();
        proto.setPacketLen(byteBuf.readInt());
        proto.setHeaderLen(byteBuf.readShort());
        proto.setVersion(byteBuf.readShort());
        proto.setOperation(byteBuf.readInt());
        proto.setSeqId(byteBuf.readInt());
        if (proto.getPacketLen() > proto.getHeaderLen()) {
            byte[] bytes = new byte[proto.getPacketLen() - proto.getHeaderLen()];
            byteBuf.readBytes(bytes);
            proto.setBody(bytes);
        }

        list.add(proto);

        log.debug("decode: {}", proto);
    }
}
