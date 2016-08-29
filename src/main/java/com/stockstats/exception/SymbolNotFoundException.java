package com.stockstats.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Symbol Not Found")
public class SymbolNotFoundException extends Exception {

	private static final long serialVersionUID = 6931199914009375835L;

	public SymbolNotFoundException(String symbol){
		super("Symbol Not Found: "+symbol);
	}
}