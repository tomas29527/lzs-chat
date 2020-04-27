package com.lzs.chat.server.initializer;

import com.lzs.chat.base.codec.WebSocketMessageDecoder;
import com.lzs.chat.base.codec.WebSocketMessageEncoder;
import com.lzs.chat.base.protobuf.Message;
import com.lzs.chat.server.handler.ChatHeartBeatHandler;
import com.lzs.chat.server.handler.ChatServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * WebSocket服务初始化类
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<NioSocketChannel> {
    @Autowired
    private ChatHeartBeatHandler heartBeatHandler;
    @Autowired
    private ChatServerHandler chatServerHandler;
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程.addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        // 编解码 http 请求
        pipeline.addLast("http-codec",new HttpServerCodec());
        // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
        // 保证接收的 Http 请求的完整性
        pipeline.addLast("aggregator",new HttpObjectAggregator(64 * 1024));
        // 写文件内容
      //  pipeline.addLast("http-chunked",new ChunkedWriteHandler());
       // pipeline.addLast(new WebSocketServerCompressionHandler());
        //Netty支持websocket
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat", null, true));
        //websocket消息帧处理看下面代码(这里需要把前台的消息分类，判断传过来的是websocket哪个帧，如果为二进制帧往下传值，让protobuf解码)
        pipeline.addLast(new WebSocketMessageDecoder());
        //protbuf解码
        pipeline.addLast(new ProtobufDecoder(Message.Protocol.getDefaultInstance()));
        //半包处理
       // pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        // 协议包编码
        pipeline.addLast(new WebSocketMessageEncoder());
        pipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
        pipeline.addLast(heartBeatHandler);
        pipeline.addLast(chatServerHandler);
    }

}
