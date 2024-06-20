package com.example.demo.Services;


import com.example.demo.Models.ExchangeRate;
import com.example.demo.Repositories.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRate> listExchangeRates(String code, LocalDate date) {
        if (code != null & code != "") return exchangeRateRepository.findByCharCodeAndDate(code, date);
        return exchangeRateRepository.findAll();
    }

    public void saveExchangeRate(ExchangeRate exchangeRate) {
        log.info("Saving new exchangeRate. Code: {}; date: {}; value: {}", exchangeRate.getCharCode(), exchangeRate.getDate(), exchangeRate.getValue());
        exchangeRateRepository.save(exchangeRate);
    }

    public Boolean deleteExchangeRate(ExchangeRate exchangeRate) {
        if (listExchangeRates(exchangeRate.getCharCode(), exchangeRate.getDate()) == exchangeRate) {
            log.info("exchangeRate with id = {} was deleted", exchangeRate.getId());
            exchangeRateRepository.delete(exchangeRate);
            return true;
        } else {
            log.info("exchangeRate with id = {} does not exist", exchangeRate.getId());
            return false;
        }
    }

    public Boolean deleteExchangeRate(Long id) {
        return deleteExchangeRate(exchangeRateRepository.getById(id));
    }
    // можно также сделать перегрузку этого метода для удаления по коду и дате, но я думаю незачем, скорее всего я итак уже много лишнего написал

    public ExchangeRate getExchangeRateById(Long id) {
        return exchangeRateRepository.findById(id).orElse(null);
    }
}

