package com.movieland.service.impl;

import com.movieland.controller.validation.Currency;

public interface CurrencyConverterService {

    default double convertFromUah(double price, Currency toCurrency) {
        return convert(price, toCurrency);
    }

    double convert(double price, Currency currency);
}