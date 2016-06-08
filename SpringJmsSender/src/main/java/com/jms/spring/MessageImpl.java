package com.jms.spring;

public class MessageImpl implements IMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object message;
	private int id;
	
	public MessageImpl(String message, int id) {
		super();
		this.message = message;
		this.id = id;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
