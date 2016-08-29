package com.stockstats.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadStatsRequestException.class)
	public @ResponseBody Map<String, Object> handleBadStatsRequestException(BadStatsRequestException ex) {
		return buildErrorMap(400, ex);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(SymbolNotFoundException.class)
	public @ResponseBody Map<String, Object> handleSymbolNotFoundException(SymbolNotFoundException ex) {
		return buildErrorMap(404, ex);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(StatsNotFoundException.class)
	public @ResponseBody Map<String, Object> handleStatsNotFoundException(StatsNotFoundException ex) {
		return buildErrorMap(404, ex);
	}

	@ExceptionHandler(value = Exception.class)
	public @ResponseBody Map<String, Object> handleException(Exception ex) {
		return buildErrorMap(500, ex);
	}
	
	private Map<String, Object> buildErrorMap(int statusCode, Exception ex) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", statusCode);
		map.put("message", ex.getMessage());
		return map;
	}

}
