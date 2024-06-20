package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "ExchangeRates")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String charCode;
    private LocalDate date;
    private float value;

    public ExchangeRate(String code, LocalDate date, float value){
        this.charCode = code;
        this.date = date;
        this.value = value;
    }
    public ExchangeRate(String code, String date, float value){
        this.charCode = code;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.date = LocalDate.parse(date, formatter);
        this.value = value;
    }

    public ExchangeRate() {

    }
}

