package org.protocol;

import java.io.Serializable;

public class RPCMethod implements Serializable {

	private static final long serialVersionUID = -1430761763297676014L;

	private String methodname;

	private Class<?>[] parameterType;

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public Class<?>[] getParameterType() {
		return parameterType;
	}

	public void setParameterType(Class<?>[] parameterType) {
		this.parameterType = parameterType;
	}

	public RPCMethod(String methodname, Class<?>[] parameterType) {
		this.methodname = methodname;
		this.parameterType = parameterType;
	}

}
