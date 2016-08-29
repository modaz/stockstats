package com.stockstats.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Stats Request is Bad")
public class BadStatsRequestException extends Exception {

	private static final long serialVersionUID = -3622347161140753809L;
	private String error = "Missing Parameters: ";

	public BadStatsRequestException(List<FieldError> list) {
		for (FieldError objectError : list) {
			error += objectError.getField() + ", ";
		}
		error = error.trim();
		if (error.charAt(error.length() - 1) == ',') {
			error = error.substring(0, error.length() - 1);
		}
	}

	@Override
	public String getMessage() {
		return error;
	}

}
