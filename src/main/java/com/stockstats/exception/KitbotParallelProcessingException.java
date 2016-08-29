package com.stockstats.exception;

public class KitbotParallelProcessingException extends Exception {

	private static final long serialVersionUID = -6438041277442003475L;

	public KitbotParallelProcessingException(String symbol){
		super(symbol);
	}
}
