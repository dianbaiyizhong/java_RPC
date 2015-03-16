package org.server;

import org.business.IMethod;
import org.business.Method;

public class ServerMain {

	public static void main(String[] args) throws InterruptedException {
		RPCServer rpcServer = new RPCServer(8090);
		// 注册服务
		rpcServer.register(IMethod.class.getName(), new Method());
		// 启动服务器
		rpcServer.start();

		// Thread.sleep(10 * 1000);

		// 停掉服务器
		// rpcServer.stop();
	}

}
