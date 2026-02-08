package com.stockstats.service;

import java.util.Map;
import java.util.concurrent.Callable;

import com.stockstats.dto.StatsResponse;

public class KitbotCallable implements Callable<StatsResponse> {

	private final KibotService kibotService;
	private final Map<String, String> params;

	public KitbotCallable(KibotService kibotService, Map<String, String> params) {
		this.kibotService = kibotService;
		this.params = params;
	}

	@Override
	public StatsResponse call() {
		return kibotService.callAPI(params);
	}
}
