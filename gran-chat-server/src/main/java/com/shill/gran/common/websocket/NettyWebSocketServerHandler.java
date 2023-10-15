package com.shill.gran.common.websocket;

import cn.hutool.json.JSONUtil;
import com.shill.gran.common.websocket.enums.WSReqTypeEnum;
import com.shill.gran.common.websocket.vo.req.WsBaseReqVo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable//多路复用一个处理器。
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("握手完成！");
        }else if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲");
                //可以用户下线。
                ctx.channel().close();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WsBaseReqVo wsBaseReqVo = JSONUtil.toBean(text, WsBaseReqVo.class);
        switch (WSReqTypeEnum.of(wsBaseReqVo.getType())){
            case LOGIN:
                channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("返回数据"));
                break;
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
        }
    }
}
