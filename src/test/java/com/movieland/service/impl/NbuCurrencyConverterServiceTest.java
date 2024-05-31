//package com.movieland.service.impl;
//
//import com.movieland.common.Currency;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//class NbuCurrencyConverterServiceTest {
//
//    @Mock
//    private CurrencyRateCache currencyRateCache;
//
//    @InjectMocks
//    private NbuCurrencyConverterService nbuCurrencyConverterService;
//
//    @Test
//    public void testGetRate() {
//        Currency currency = Currency.USD;
//        double rate = 38.76;
//        double price = 126.83;
//
//        when(currencyRateCache.getRate(currency)).thenReturn(rate);
//
//        double convertedPrice = nbuCurrencyConverterService.convert(price, currency);
//
//        assertEquals(3.27, convertedPrice);
//    }
//
//
//}