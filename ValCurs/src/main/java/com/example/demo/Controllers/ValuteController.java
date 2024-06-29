package com.example.demo.Controllers;

import com.example.demo.DataOperations.GetXmlData;
import com.example.demo.Models.DTO.ValCursDTO;
import com.example.demo.Models.DTO.ValuteDTO;
import com.example.demo.Models.Valute;
import com.example.demo.Services.ValuteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ValuteController {
    private final ValuteService valuteService;
    private final GetXmlData getXmlData;

    @GetMapping("/")
    public Valute getValuteByCharCodeAndDate(@RequestParam(value = "personDTO") String stringValuteDTO) throws JsonProcessingException {
        ValuteDTO valuteDTO = new ObjectMapper().findAndRegisterModules().readValue(stringValuteDTO, ValuteDTO.class);
        List<Valute> valutes = valuteService.listValutes(valuteDTO.getCharCode(), valuteDTO.getDate());
        if (!valutes.isEmpty()) {
            return valutes.get(0);
        } else {
            try {
                ValCursDTO valCursDTO = getXmlData.getValCurs(valuteDTO.getDate());
                if (valCursDTO != null) {
                    valCursDTO.getValutes().forEach(valuteService::saveValute); // здесь я не знаю нужно сохранить только 1 запись или все за день, поэтому сохраню все
                    //но если надо 1 только то нужно просто добавить проверку на charCode и всё
                    return valuteService.listValutes(valuteDTO.getCharCode(), valuteDTO.getDate()).get(0);
                } else {
                    throw new NullPointerException();
                }
            } catch (IOException e) {
                throw new NullPointerException();
            }
        }
    }

    @PostMapping("/")
    @Scheduled(cron = "@daily") // "@daily" = "0 0 0 * * *"
    public void Job(){
        try {
            ValCursDTO valCursDTO = getXmlData.getValCurs(LocalDate.now());
            if (valCursDTO != null) {
                valCursDTO.getValutes().forEach(n -> {
                    if (valuteService.listValutes(n.getCharCode(), n.getDate()).isEmpty()) {
                        valuteService.saveValute(n);
                    }
                });
            } else {
                throw new NullPointerException();
            }
        } catch (IOException e) {
            throw new NullPointerException();
        }
    }
}