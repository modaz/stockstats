package com.stockstats.service;

import java.util.ArrayList;
import java.util.Arrays;
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

	public List<StatsResponse> execute(Map<String, String> params) throws KitbotParallelProcessingException {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
		List<StatsResponse> resultList = new ArrayList<>();

		String[] symbols = params.get("symbols").split(",");
		Set<String> uniqueSymbols = new HashSet<String>(Arrays.asList(symbols));
		for (String symbol : uniqueSymbols) {
			params.put("symbol", symbol);
			KitbotCallable calculator = new KitbotCallable(params);
			Future<StatsResponse> result = executor.submit(calculator);
			try {
				resultList.add(result.get());
			} catch (InterruptedException | ExecutionException e) {
				throw new KitbotParallelProcessingException(e.getMessage());
			}
		}

		executor.shutdown();
		return resultList;
	}

}
