package org.client;

import org.business.IMethod;

public class ClientMain {

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 8090;
		try {
			IMethod iMethod = (IMethod) RPCClient.getProxy(IMethod.class
					.getName(), host, port);

			System.out.println(iMethod.addprint("Tom"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
