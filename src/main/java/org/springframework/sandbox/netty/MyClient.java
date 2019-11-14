package org.springframework.sandbox.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final  class MyClient {
	private final static Logger logger = LoggerFactory.getLogger(MyClient.class);
	private final static ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);

	public static void main(String[] args) throws Exception {
		ArrayList<serverandPort> serverandPortIList=new ArrayList<>();
		final serverandPort sp=new serverandPort();
		sp.serverName="127.0.0.1";
		sp.port=8081;
		serverandPortIList.add(sp);
		//serverandPortIList.add(sp);
		initClientPool(serverandPortIList);
	}
	static void initClientPool(ArrayList<serverandPort> serverandPortArrayList) throws Exception
	{
		EventLoopGroup group = new NioEventLoopGroup();
		final Bootstrap b = new Bootstrap();
		b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline p = ch.pipeline();
						p.addLast(new LoggingHandler());
						p.addLast(new TimeClientHandler());
						p.addLast(new ClientDispatcherServletChannelInitializer());
					}
				});

		List<ChannelFuture> futureList=new ArrayList<>();
		for(serverandPort sp :serverandPortArrayList)
		{
			try
			{

			final ChannelFuture future= b.connect(sp.serverName,sp.port).sync();//调用connect方法发起异步连接，然后调用同步方法等待连接成功。权威指南中的94页
			futureList.add(future);
			future.channel().closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					//如果这个channel失败的话，只关闭自己的channel，不能关闭group，然后后面进行重新连接
					channelFuture.cause().printStackTrace();
					channelFuture.channel().close();
				}
			});
		}catch (Exception ex)
			{}
			finally {
				// 所有资源释放完成之后，清空资源，再次发起重连操作
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							TimeUnit.SECONDS.sleep(1);
							try {
								//b.connect(sp.serverName,sp.port).sync();// 发起重连操作
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			}
			}
		//	future.channel().closeFuture().sync();
	}





}
