package com.stockstats.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

	private static final String KIBOT_HISTORY_URL = "http://api.kibot.com/?action=history&symbol={symbol}&interval=daily&startdate={startdate}&enddate={enddate}";

	private final RestTemplate restTemplate;

	public KibotService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public StatsResponse callAPI(Map<String, String> apiParams) {
		String apiResponse = restTemplate.getForObject(KIBOT_HISTORY_URL, String.class, apiParams);
		String symbol = apiParams.get("symbol");
		if (SYMBOL_NOT_FOUND.equals(apiResponse)) {
			return new StatsResponse(symbol, SYMBOL_NOT_FOUND);
		} else {
			return transform(symbol, apiResponse);
		}
	}

	public StatsResponse transform(String symbol, String apiResponse) {
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
		double sum = 0;

		for (Double closingPrice : closingPrices) {
			sum += closingPrice;
			if (closingPrice < minClosingPrice) {
				minClosingPrice = closingPrice;
			}
			if (closingPrice > maxClosingPrice) {
				maxClosingPrice = closingPrice;
			}
		}
		double averageClosingPrice = BigDecimal.valueOf(sum / closingPrices.size())
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
		return new StatsResponse(symbol, minClosingPrice, maxClosingPrice, averageClosingPrice);
	}

	private List<Double> buildClosingPriceList(String apiResponse) {
		List<Double> closingPrices = new ArrayList<>();
		String[] split = apiResponse.split("\n");
		for (String line : split) {
			String[] tokens = line.split(",");
			if (tokens.length == 6) {
				closingPrices.add(Double.parseDouble(tokens[4].trim()));
			}
		}
		return closingPrices;
	}
}
