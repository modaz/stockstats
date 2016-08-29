package com.stockstats.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.stockstats.dto.StatsResponse;

@Service
public class KibotService {

	public static final String SYMBOL_NOT_FOUND = "404 Symbol Not Found";
	public static final String STATS_NOT_FOUND = "404 Stats Not Found";

	public StatsResponse callAPI(Map<String, String> apiParams) {
		RestTemplate restTemplate = new RestTemplate();
		String apiResponse = restTemplate.getForObject(
				"http://api.kibot.com/?action=history&symbol={symbol}&interval=daily&startdate={startdate}&enddate={enddate}",
				String.class, apiParams);
		String symbol = apiParams.get("symbol");
		if (SYMBOL_NOT_FOUND.equals(apiResponse)) {
			return new StatsResponse(symbol, SYMBOL_NOT_FOUND);
		} else {
			return transform(symbol, apiResponse);
		}
	}

	StatsResponse transform(String symbol, String apiResponse) {
		List<Double> closingPrices = buildClosingPriceList(apiResponse);
		if (closingPrices.isEmpty()) {
			return new StatsResponse(symbol, STATS_NOT_FOUND);
		} else {
			return buildStatistics(symbol, closingPrices);
		}
	}

	private StatsResponse buildStatistics(String symbol, List<Double> closingPrices) {
		double minClosingPrice = Double.MAX_VALUE;
		double maxClosingPrice = Double.MIN_VALUE;
		double averageClosingPrice = 0;
		double sum = 0;

		for (int j = 0; j < closingPrices.size(); j++) {
			double closingPrice = closingPrices.get(j);
			sum = sum + closingPrice;
			if (closingPrice < minClosingPrice) {
				minClosingPrice = closingPrice;
			}
			if (closingPrice > maxClosingPrice) {
				maxClosingPrice = closingPrice;
			}
		}
		averageClosingPrice = new BigDecimal(sum / closingPrices.size()).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		return new StatsResponse(symbol, minClosingPrice, maxClosingPrice, averageClosingPrice);
	}

	private List<Double> buildClosingPriceList(String apiResponse) {
		List<Double> closingPrices = new ArrayList<Double>();
		String[] split = apiResponse.split("\n");
		for (String string : split) {
			String[] tokens = string.split(",");
			if (tokens.length == 6) {
				closingPrices.add(Double.parseDouble(tokens[4].trim()));
			}
		}
		return closingPrices;
	}
}
