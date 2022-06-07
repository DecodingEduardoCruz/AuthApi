package br.coop.integrada.auth.domain.exception;

public class DefaultExceptions {
	private int code;
	private String message;	
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DefaultExceptions(int code, String message) {
		this.code = code;
		this.message = message;
	}

}
