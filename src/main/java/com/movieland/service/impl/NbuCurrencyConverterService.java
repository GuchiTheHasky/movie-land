package com.movieland.service.impl;

import com.movieland.common.Currency;
import com.movieland.service.CurrencyConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NbuCurrencyConverterService implements CurrencyConverterService {

    private final CurrencyRateCache currencyRateCache;


    @Override
    public double convert(double price, Currency currency) {
        double rate = currencyRateCache.getRate(currency);
        return price / rate;
    }
}
