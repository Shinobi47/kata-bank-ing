package com.ing.kata.bank.exception;

public class TechnicalException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -691410402229608980L;

	public TechnicalException() {
		super();
	}
	
	public TechnicalException(String message) {
		super(message);
	}

}
