package com.stockstats.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stockstats.dto.StatsRequest;
import com.stockstats.dto.StatsResponse;
import com.stockstats.exception.BadStatsRequestException;
import com.stockstats.exception.KitbotParallelProcessingException;
import com.stockstats.exception.StatsNotFoundException;
import com.stockstats.exception.SymbolNotFoundException;
import com.stockstats.service.KibotExecutorService;
import com.stockstats.service.KibotService;

@RestController
@RequestMapping("/api/v1/")
public class StatsController {

	@Autowired
	private KibotService kibotService;
	@Autowired
	private KibotExecutorService kibotExecutorService;
	
	@RequestMapping(value="/stats", method = RequestMethod.GET)
	public List<StatsResponse> processStats(@Valid @ModelAttribute StatsRequest statsRequest, BindingResult result)
			throws SymbolNotFoundException, StatsNotFoundException, BadStatsRequestException {
		if (result.hasErrors()) {
	        throw new BadStatsRequestException(result.getFieldErrors());
	    }
		
		List<StatsResponse> stats = new ArrayList<>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("startdate", statsRequest.getStartDate());
		params.put("enddate", statsRequest.getEndDate());

		String[] symbolList = statsRequest.getSymbols().split(",");
		Set<String> uniqueSymbols = new HashSet<String>(Arrays.asList(symbolList));
		for (String symbol : uniqueSymbols) {
			params.put("symbol", symbol);
			StatsResponse stat = kibotService.callAPI(params);
			stats.add(stat);
		}
		return stats;
	}
	
	@RequestMapping(value="/parallelstats", method = RequestMethod.GET)
	public List<StatsResponse> processParallelStats(@Valid @ModelAttribute StatsRequest statsRequest, BindingResult result)
			throws SymbolNotFoundException, StatsNotFoundException, BadStatsRequestException, KitbotParallelProcessingException {
		if (result.hasErrors()) {
	        throw new BadStatsRequestException(result.getFieldErrors());
	    }
		
		List<StatsResponse> stats = new ArrayList<>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("startdate", statsRequest.getStartDate());
		params.put("enddate", statsRequest.getEndDate());
		params.put("symbols", statsRequest.getSymbols());
		
		stats = kibotExecutorService.execute(params);
		return stats;
	}
}
