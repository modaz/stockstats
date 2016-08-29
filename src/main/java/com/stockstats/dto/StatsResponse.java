package com.stockstats.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsResponse {
    
	private String symbol;
	private double minClosingPrice;
	private double maxClosingPrice;
	private double averageClosingPrice;
	private String message;
	
	public StatsResponse(String symbol, double minClosingPrice, double maxClosingPrice, double averageClosingPrice) {
		this.symbol = symbol;
		this.minClosingPrice = minClosingPrice;
		this.maxClosingPrice = maxClosingPrice;
		this.averageClosingPrice = averageClosingPrice;
	}

	public StatsResponse(String symbol, String message) {
		this.symbol = symbol;
		this.setMessage(message);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getMinClosingPrice() {
		return minClosingPrice;
	}

	public void setMinClosingPrice(double minClosingPrice) {
		this.minClosingPrice = minClosingPrice;
	}

	public double getMaxClosingPrice() {
		return maxClosingPrice;
	}

	public void setMaxClosingPrice(double maxClosingPrice) {
		this.maxClosingPrice = maxClosingPrice;
	}

	public double getAverageClosingPrice() {
		return averageClosingPrice;
	}

	public void setAverageClosingPrice(double averageClosingPrice) {
		this.averageClosingPrice = averageClosingPrice;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}