package com.stockstats;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.stockstats.dto.StatsResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatsControllerTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @Disabled("Integration test - requires running application and Kibot API")
    void verifySingleSymbolResponse() {
        String url = baseUrl + "/api/v1/stats?symbols=CRM&startDate=8/1/2016&endDate=8/18/2016";
        ResponseEntity<StatsResponse[]> responseEntity = restTemplate.getForEntity(url, StatsResponse[].class);
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
    }
}
