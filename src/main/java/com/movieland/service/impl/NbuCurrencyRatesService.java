package com.movieland.service.impl;

import com.movieland.dto.NbuRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class NbuCurrencyRatesService {

    private final RestClient restClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Value("${nbu.currency.url}")
    private String nbuRatesUrl;

    public List<NbuRateDto> getAllCurrencyRatesToday() {

        StringJoiner stringJoiner = new StringJoiner("");
        String dateString = LocalDate.now().format(formatter);
        String date = "?date=" + dateString + "&json";
        stringJoiner.add(nbuRatesUrl);
        stringJoiner.add(date);
        NbuRateDto[] nbuRates = restClient.get().uri(stringJoiner.toString()).accept(MediaType.APPLICATION_JSON).retrieve().body(NbuRateDto[].class);
        assert nbuRates != null;
        return Arrays.asList(nbuRates);
    }

    public List<NbuRateDto> getAllCurrencyRatesTomorrow() {

        StringJoiner stringJoiner = new StringJoiner("");
        String dateString = LocalDate.now().plusDays(1).format(formatter);
        String date = "?date=" + dateString + "&json";
        stringJoiner.add(nbuRatesUrl);
        stringJoiner.add(date);
        NbuRateDto[] nbuRates = restClient.get().uri(stringJoiner.toString()).accept(MediaType.APPLICATION_JSON).retrieve().body(NbuRateDto[].class);
        assert nbuRates != null;
        return Arrays.asList(nbuRates);
    }

}


