package com.example.demo.Services;

import com.example.demo.Models.DTO.ValuteDTO;
import com.example.demo.Models.Mappers.ValuteMapper;
import com.example.demo.Models.Valute;
import com.example.demo.Repositories.ValuteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValuteService {
    private final ValuteRepository valuteRepository;
    private final ValuteMapper mapper;

    public List<Valute> listValutes(String code, LocalDate date) {
        if (code != null & !code.equals("")) return valuteRepository.findByCharCodeAndDate(code, date);
        return valuteRepository.findAll();
    }

    public void saveValute(ValuteDTO valuteDTO) {
        log.info("Saving new Valute. Code: {}; date: {}; value: {}", valuteDTO.getCharCode(), valuteDTO.getDate(), valuteDTO.getValue());
        valuteRepository.save(mapper.toValute(valuteDTO));
    }

    public Boolean deleteValute(ValuteDTO valuteDTO) {
        if (listValutes(valuteDTO.getCharCode(), valuteDTO.getDate()).get(0).equals(valuteDTO)) {
            log.info("Valute with id = {} was deleted", valuteDTO.getId());
            valuteRepository.delete(mapper.toValute(valuteDTO));
            return true;
        } else {
            log.info("Valute with id = {} does not exist", valuteDTO.getId());
            return false;
        }
    }

    public Boolean deleteValute(String id) {
        return deleteValute(new ValuteDTO(id));
    }

    public ValuteDTO getValuteDTOById(String id) {
        Valute valute = valuteRepository.findById(id).orElse(null);
        if (valute != null) {
            return mapper.toDto(valute);
        } else {
            log.info("Valute with id = {} does not exist", id);
            return null;
        }
    }
}

