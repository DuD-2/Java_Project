package com.example.demo.Models.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValuteDTO {
    @JacksonXmlProperty(localName = "ID", isAttribute = true)
    private String id;
    private LocalDate date;
    @JacksonXmlProperty(localName = "CharCode")
    private String charCode;
    @JacksonXmlProperty(localName = "Value")
    private String value;

    public ValuteDTO(ValCursDTO valCursDTO, int id) {
        ValuteDTO valuteDTO = valCursDTO.getValutes().get(id);
        this.id = valuteDTO.getId();
        this.date = valCursDTO.getDate();
        this.charCode = valuteDTO.getCharCode();
        this.value = valuteDTO.getValue();
    }

    public ValuteDTO(String id) {
        if (id != null) {
            this.id = id;
        }
    }

    public ValuteDTO(String charCode, String date) {
        this.charCode = charCode;
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}