package com.example.demo.Models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "ValCurs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValCursDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @JacksonXmlProperty(localName = "Date", isAttribute = true)
    private LocalDate date;
    @JacksonXmlProperty(localName = "Valute")
    @JacksonXmlElementWrapper(localName = "Valute", useWrapping = false)
    private ArrayList<ValuteDTO> valutes;

    public ArrayList<ValuteDTO> getValutes() {
        if (this.valutes != null) {
            this.valutes.forEach(n -> n.setDate(this.getDate()));
            return valutes;
        } else {
            return null;
        }
    }
}
