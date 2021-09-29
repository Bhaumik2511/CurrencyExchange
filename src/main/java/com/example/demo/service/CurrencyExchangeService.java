package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CurrencyDao;
import com.example.demo.dao.RateDao;
import com.example.demo.entity.Currency;

@Service
public class CurrencyExchangeService {

	@Autowired
	CurrencyDao currencyDao;
	@Autowired
	RateDao ratesDao;
	
	public 	Currency saveCurrency(Currency currency) {
		return currencyDao.save(currency);
	}
	
	public List<Currency> findByDate(LocalDate from, LocalDate to) {
		return currencyDao.findByCreatedDateBetween(from, to);
	}
	
	
}
