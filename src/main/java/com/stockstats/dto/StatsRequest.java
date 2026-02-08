package com.stockstats.dto;

import jakarta.validation.constraints.NotNull;

public class StatsRequest {

    @NotNull
    private String symbols;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    
	public String getSymbols() {
		return symbols;
	}
	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

    
}