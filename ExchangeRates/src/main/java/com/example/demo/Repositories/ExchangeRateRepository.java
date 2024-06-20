package com.example.demo.Repositories;

import com.example.demo.Models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByCharCodeAndDate(String code, LocalDate date);
}