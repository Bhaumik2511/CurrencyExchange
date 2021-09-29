package com.example.demo.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Currency;


public interface CurrencyDao extends JpaRepository<Currency, Long>{

	 @Query("select c from Currency c where c.date >= :from and c.date <= :to ")
	 List<Currency> findByCreatedDateBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}

 