package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Currency;
import com.example.demo.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currency-converter")
public class CurrencyExchangeController {
		
	@Autowired
	CurrencyExchangeService currencyExchangeService;
	@Value("${currency.exchange.url}")
    private String url;
	@Value("${currency.exchange.access_key}")
    private String access_key;
	
	private static final String DATE_MSG = "Only last 12 months are included (Restricted  to the rate as of the 1st day of the month )";
	private static final String SAVE_MSG = "Currency Exchange rate is saved for date ";
	
	@PostMapping("/{requestDate}")
	public ResponseEntity<String> convertcurrency(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate requestDate) {
		if(validateDate(requestDate))
			return  new ResponseEntity<String>(DATE_MSG,HttpStatus.NOT_ACCEPTABLE);
		Currency currency = new RestTemplate().getForObject(formatUrl(requestDate), Currency.class);
		  currency.getRate().setCurrency(currency);
		  currencyExchangeService.saveCurrency(currency);
		 return  new ResponseEntity<String>(SAVE_MSG+requestDate,HttpStatus.CREATED);
		
		
	}
	
		
	@GetMapping("/daterange")
	public List<Currency>  getCurrencyData(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
									@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
		return currencyExchangeService.findByDate(from,to);
	}
	
	
	
	
	private  boolean validateDate(LocalDate requestDate){
		LocalDate withinOneYearDate = LocalDate.now().minusYears(1);
		if(requestDate.getDayOfMonth()!=1) {
			return true;
		}
		if(requestDate.isBefore(withinOneYearDate)) {
			return true;
		}
		
		return LocalDate.now().isBefore(requestDate);
		
		
	}
	
	private String formatUrl(LocalDate requestDate){
		 return new StringBuilder(url)
		  .append(requestDate).append("?").append("access_key=")
		  .append(access_key)
		  .append("&base=EUR").append("&symbols=GBP,USD,HKD").toString();
	}

}
