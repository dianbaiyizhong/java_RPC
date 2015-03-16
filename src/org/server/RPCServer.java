package org.server;

import java.util.HashMap;
import java.util.Map;

public class RPCServer {

	private int port;

	private boolean isRun;

	/**
	 * 服务列表
	 */
	private Map<String, Object> serviceEngine = new HashMap<String, Object>();

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isRun() {
		return isRun;
	}

	public RPCServer(int port) {
		super();
		this.port = port;
	}

	public Map<String, Object> getServiceEngine() {
		return serviceEngine;
	}

	public void register(String name, Object impl) {
		serviceEngine.put(name, impl);
	}

	public void start() {
		isRun = true;
		new Listener(this).start();
	}

	public void stop() {
		isRun = false;
	}

}
