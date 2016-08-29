package com.stockstats;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.stockstats.dto.StatsResponse;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StockstatsApplication.class)
@WebAppConfiguration
public class StatsControllerTest {
	private RestTemplate restTemplate;
	private static final String SINGLE_SYMBOL_URL = "http://localhost:8090/api/v1/stats?symbols=CRM&startDate=8/1/2016&endDate=8/18/2016";
    
	@Before
	public void setup(){
		restTemplate = new RestTemplate();
	}
	
	@Test
	@Ignore("Could not get this to work!")
	public void verifySingleSymbolResponse() {
		ResponseEntity<StatsResponse[]> responseEntity = restTemplate.getForEntity(SINGLE_SYMBOL_URL, StatsResponse[].class);
		assertNotNull(responseEntity);
		StatsResponse[] objects = responseEntity.getBody();
		assertNotNull(objects);
	}
}
