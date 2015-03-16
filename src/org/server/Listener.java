package org.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import org.protocol.InvokerBean;

public class Listener extends Thread {

	private RPCServer rpcServer;

	public Listener(RPCServer rpcServer) {
		this.rpcServer = rpcServer;
	}

	public void run() {
		ServerSocket server = null;
		try {
			System.out.println("server start...");
			server = new ServerSocket(rpcServer.getPort());

			while (rpcServer.isRun()) {
				// 监听客户端的请求，该方法会阻塞。即使调用了RPCServer的stop方法，也会在接收到一次请求之后才停止。
				Socket socket = server.accept();

				// 解析来自客户端的流
				ObjectInputStream ois = new ObjectInputStream(
						socket.getInputStream());
				InvokerBean invokerBean = (InvokerBean) ois.readObject();
				String serviceName = invokerBean.getServiceName();
				String methodName = invokerBean.getMethod().getMethodname();
				Class<?>[] parameterType = invokerBean.getMethod()
						.getParameterType();
				Object[] parameters = invokerBean.getParameters();
				// System.out.println("invoking... serviceName = " + serviceName
				// + ". methodName = " + methodName);

				// 从注册的服务列表中，获取服务，并利用反射进行调用
				Object service = rpcServer.getServiceEngine().get(serviceName);
				if (service == null) {
					System.err.println("service not found!");
				} else {
					Class<?> InterfaceClass = Class.forName(serviceName);
					Method method = InterfaceClass.getMethod(methodName,
							parameterType);
					Object result = method.invoke(service, parameters);
					invokerBean.setResult(result);
				}

				// 返回result
				ObjectOutputStream oos = new ObjectOutputStream(
						socket.getOutputStream());
				oos.writeObject(invokerBean);
				oos.flush();

				socket.close();
				// System.out.println("invoking over");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Server stoped.");
	}

}
