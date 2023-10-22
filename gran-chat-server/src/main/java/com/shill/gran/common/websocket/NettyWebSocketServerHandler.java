package com.shill.gran.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.shill.gran.common.websocket.enums.WSReqTypeEnum;
import com.shill.gran.common.websocket.service.WebSockService;
import com.shill.gran.common.websocket.vo.req.WsBaseReqVo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable//多路复用一个处理器。
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //channel连接事件，连接了就生效了。保存用户信息和连接。
    WebSockService webSockService;
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.webSockService = getService();
    }

    private WebSockService getService() {
        return SpringUtil.getBean(WebSockService.class);
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("握手完成！");
            this.webSockService.saveConnect(ctx.channel());
        }
        else if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲");
                userOffLine(ctx.channel());
                //可以用户下线。
                ctx.channel().close();
            }
        }
    }
    //关闭浏览器-客户端主动下线。
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        webSockService.userOffLine(ctx.channel());
    }

    /**
     * 用户下线
     * @param channel
     */
    private void userOffLine(Channel channel) {
        webSockService.userOffLine(channel);
        channel.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WsBaseReqVo wsBaseReqVo = JSONUtil.toBean(text, WsBaseReqVo.class);
        switch (WSReqTypeEnum.of(wsBaseReqVo.getType())){
            case LOGIN:
                webSockService.handleLoginReq(channelHandlerContext.channel());
                break;
            case AUTHORIZE:

                break;
            case HEARTBEAT:
                break;
        }
    }
}
