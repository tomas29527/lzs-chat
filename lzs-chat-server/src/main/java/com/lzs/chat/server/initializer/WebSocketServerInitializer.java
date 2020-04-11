package com.lzs.chat.server.initializer;

import com.lzs.chat.base.codec.WebSocketMessageDecoder;
import com.lzs.chat.base.codec.WebSocketMessageEncoder;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.server.handler.ChatServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocket服务初始化类
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<NioSocketChannel> {

    @Autowired
    private ChatServerHandler serverHandler;

    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 编解码 http 请求
        pipeline.addLast(new HttpServerCodec());
        // 写文件内容
        pipeline.addLast(new ChunkedWriteHandler());
        // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
        // 保证接收的 Http 请求的完整性
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        //Netty支持websocket
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat", null, true));
        //websocket消息帧处理看下面代码(这里需要把前台的消息分类，判断传过来的是websocket哪个帧，如果为二进制帧往下传值，让protobuf解码)
        pipeline.addLast(new WebSocketMessageDecoder());
        //半包处理
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        //protbuf解码
        pipeline.addLast(new ProtobufDecoder(Message.Protocol.getDefaultInstance()));
        // 协议包编码
        pipeline.addLast(new WebSocketMessageEncoder());
        // 处理 TextWebSocketFrame
        //pipeline.addLast(protoCodec);


        pipeline.addLast(serverHandler);
    }

}
