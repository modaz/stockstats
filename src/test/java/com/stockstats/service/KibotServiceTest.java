package com.stockstats.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.stockstats.dto.StatsResponse;

class KibotServiceTest {

    private KibotService kibotService;
    private Map<String, String> apiParams;

    @BeforeEach
    void setup() throws URISyntaxException {
        doKibotLogin();
        kibotService = new KibotService(new RestTemplate());
        apiParams = new HashMap<>();
        apiParams.put("startdate", "8/14/2016");
        apiParams.put("enddate", "8/18/2016");
    }

    @Test
    void verifyValidSymbolResponse() {
        apiParams.put("symbol", "CRM");
        StatsResponse response = kibotService.callAPI(apiParams);
        assertNotNull(response);
        assertNull(response.getMessage());
        assertEquals("CRM", response.getSymbol());
    }

    @Test
    void verifyInvalidSymbolResponse() {
        apiParams.put("symbol", "Invalid");
        StatsResponse response = kibotService.callAPI(apiParams);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertEquals(KibotService.SYMBOL_NOT_FOUND, response.getMessage());
        assertEquals("Invalid", response.getSymbol());
    }

    @Test
    void verifyInvalidAPIStatsResponse() {
        String apiResponse = "";
        StatsResponse response = kibotService.transform("CRM", apiResponse);
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertEquals(KibotService.STATS_NOT_FOUND, response.getMessage());
    }

    @Test
    void verifyMinMaxAverageClosingPriceValues() {
        String apiResponse = String.format("%s\n%s\n%s", "08/16/2016,79,79,77.92,77.96,4330671",
                "08/17/2016,77.42,77.45,75.87,76.30,7586580", "08/18/2016,76.37,77.16,76.17,76.91,5647516");
        double expectedMin = 76.30;
        double expectedMax = 77.96;
        double expectedAverage = BigDecimal.valueOf((77.96 + 76.30 + 76.91) / 3)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        StatsResponse response = kibotService.transform("CRM", apiResponse);
        assertNotNull(response);
        assertNull(response.getMessage());
        assertEquals(BigDecimal.valueOf(expectedAverage), BigDecimal.valueOf(response.getAverageClosingPrice()));
        assertEquals(BigDecimal.valueOf(expectedMax), BigDecimal.valueOf(response.getMaxClosingPrice()));
        assertEquals(BigDecimal.valueOf(expectedMin), BigDecimal.valueOf(response.getMinClosingPrice()));
    }

    private void doKibotLogin() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI("http://api.kibot.com?action=login&user=guest&password=guest");
        restTemplate.execute(url, HttpMethod.GET, null, null);
    }
}
