package com.gjp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class WebSocketHanndler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<String, Channel> channel_map = new ConcurrentHashMap<>();
    private static Map<String, ChannelHandlerContext> channel_map2 = new ConcurrentHashMap<>();;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("服务器收到信息：" + msg.text());
        for (String key : channel_map2.keySet()) {
            if(key.equals(channel.id().toString())){
                continue;
            }
            // 使用copy的原因是解决一下异常：
            // io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
            channel_map2.get(key).writeAndFlush(msg.copy());
            System.out.println("服务器转发信息：" + msg.text());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel_map.put(ctx.channel().id().toString(), ctx.channel());
        channel_map2.put(ctx.channel().id().toString(), ctx);
        System.out.println("用户上线---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channel_map2.remove(ctx.channel().id().toString());
        channel_map.remove(ctx.channel().id().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
