package com.example.demo.Repositories;

import com.example.demo.Models.Valute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ValuteRepository extends JpaRepository<Valute, String> {
    List<Valute> findByCharCodeAndDate (String CharCode, LocalDate date);
}
