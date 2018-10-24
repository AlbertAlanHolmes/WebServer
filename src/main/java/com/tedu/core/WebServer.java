package com.tedu.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Web服务器
 * 
 * @author adminitartor
 *
 */
public class WebServer {
	static int num;
	private ServerSocket server;
	private ExecutorService threadPool;

	public WebServer() throws Exception {
		try {

			server = new ServerSocket(8888);
			threadPool = Executors.newFixedThreadPool(100);
		} catch (Exception e) {
			throw e;
		}
	}

	public void start() {
		try {
			System.out.println(111);
			while (true) {
				System.out.println("等待连接...");
				Socket socket = server.accept();
				num++;
				System.out.println("第" + num + "个客户端上线了");
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {

			WebServer server = new WebServer();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务端启动失败!");
		}
	}
}
