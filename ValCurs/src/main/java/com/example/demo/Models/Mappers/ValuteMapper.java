package com.example.demo.Models.Mappers;

import com.example.demo.Models.DTO.ValuteDTO;
import com.example.demo.Models.Valute;
import org.springframework.stereotype.Component;

@Component
public
class ValuteMapper {
    public ValuteDTO toDto(Valute valute) {
        return new ValuteDTO(valute.getId(), valute.getDate(), valute.getCharCode(), Float.toString(valute.getValue()));
    }
    public Valute toValute(ValuteDTO valuteDTO) {
        return new Valute(valuteDTO.getId(), valuteDTO.getCharCode(), valuteDTO.getDate(), Float.parseFloat(valuteDTO.getValue().replace(",", ".")));
    }
}