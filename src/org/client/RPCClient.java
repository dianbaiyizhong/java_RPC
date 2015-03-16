package org.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.protocol.RPCMethod;
import org.protocol.InvokerBean;

public class RPCClient {

	public static Object getProxy(String name, String host, int port)
			throws ClassNotFoundException, UnknownHostException {
		Class<?> interfaceClass = Class.forName(name);
		InvocationHandler handler = new ClientInvocationHandler(name,
				InetAddress.getByName(host), port);
		// 调用代理类的方法，实际上是调用handler的invoke方法
		return Proxy.newProxyInstance(RPCClient.class.getClassLoader(),
				new Class<?>[] { interfaceClass }, handler);
	}

}

class ClientInvocationHandler implements InvocationHandler {

	private InetAddress address;

	private int port;

	private String name;

	public ClientInvocationHandler(String name, InetAddress address, int port) {
		super();
		this.address = address;
		this.port = port;
		this.name = name;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// 把调用服务所需的信息封装到bean中
		InvokerBean invokerBean = new InvokerBean(name, new RPCMethod(method
				.getName(), method.getParameterTypes()), args);

		// 启动socket
		Socket client = new Socket(address, port);

		// 把序列化对象转换成流，使用JDK自带的ObjectOutputStream
		ObjectOutputStream oos = new ObjectOutputStream(client
				.getOutputStream());
		oos.writeObject(invokerBean);
		oos.flush();

		// 读取服务端返回的结果
		ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
		invokerBean = (InvokerBean) ois.readObject();

		oos.close();
		ois.close();
		client.close();

		return invokerBean.getResult();
	}

}