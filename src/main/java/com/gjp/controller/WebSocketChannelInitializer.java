package com.gjp.controller;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketChannelInitializer extends ChannelInitializer {

    @Autowired
    WebSocketHanndler webSocketHanndler;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());     // http协议的支持
        pipeline.addLast(new ChunkedWriteHandler());     // 大数据流的支持

        // post请求分三部分：request line / request header / message body
        // HttpObjectAggregator将多个信息转化成 request、response
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));     // http协议升级为ws协议，支持websocket
        pipeline.addLast(webSocketHanndler);     //

    }
}
