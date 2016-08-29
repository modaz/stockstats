package com.stockstats.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Stats Not Found")
public class StatsNotFoundException extends Exception {

	private static final long serialVersionUID = 6931199914009375835L;

	public StatsNotFoundException(String symbol){
		super("Stats Not Found for Symbol: "+symbol);
	}
}