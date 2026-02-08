package com.stockstats.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.stereotype.Service;

import com.stockstats.dto.StatsResponse;
import com.stockstats.exception.KitbotParallelProcessingException;

@Service
public class KibotExecutorService {

	private static final int THREAD_POOL_SIZE = 4;
	private final KibotService kibotService;

	public KibotExecutorService(KibotService kibotService) {
		this.kibotService = kibotService;
	}

	public List<StatsResponse> execute(Map<String, String> params) throws KitbotParallelProcessingException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		List<StatsResponse> resultList = new ArrayList<>();

		try {
			String[] symbols = params.get("symbols").split(",");
			Set<String> uniqueSymbols = new HashSet<>(Arrays.asList(symbols));
			for (String symbol : uniqueSymbols) {
				Map<String, String> symbolParams = new HashMap<>(params);
				symbolParams.put("symbol", symbol);
				KitbotCallable calculator = new KitbotCallable(kibotService, symbolParams);
				Future<StatsResponse> result = executor.submit(calculator);
				try {
					resultList.add(result.get());
				} catch (InterruptedException | ExecutionException e) {
					Thread.currentThread().interrupt();
					throw new KitbotParallelProcessingException(e.getMessage());
				}
			}
		} finally {
			executor.shutdown();
		}
		return resultList;
	}
}
