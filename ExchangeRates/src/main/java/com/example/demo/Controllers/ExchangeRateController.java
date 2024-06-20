package com.example.demo.Controllers;

import com.example.demo.DataOperations.GetXmlData;
import com.example.demo.DataOperations.Gson.ExchangeRateTypeAdapter;
import com.example.demo.DataOperations.Gson.LocalDateTypeAdapter;
import com.example.demo.Models.ExchangeRate;
import com.example.demo.Services.ExchangeRateService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public float getValueByCharCodeFromDocument(String charCode, Document document) throws IOException {
        String valuteId = GetXmlData.defaultCharCodeAndValuteIdDictionary.get(charCode);
        try {
            return Float.parseFloat(document.select("Valute#" + valuteId).get(0).select("Value").get(0).text().replace(",", "."));
        } catch (IndexOutOfBoundsException e) {
            valuteId = document.select("Valute").attr("ID");
            GetXmlData.defaultCharCodeAndValuteIdDictionary.put(charCode, valuteId);
            return Float.parseFloat(document.select("Valute#" + valuteId).get(0).select("Value").get(0).text().replace(",", "."));
        }
    }

    @PostMapping()
    @Scheduled(fixedRate = 86400000)
    public void job() throws IOException {
        LocalDate date = LocalDate.now();
        Document document = GetXmlData.getCurDateXML(formatter.format(date));
        Enumeration charCodes = GetXmlData.defaultCharCodeAndValuteIdDictionary.keys();
        while (charCodes.hasMoreElements()) {
            String charCode = charCodes.nextElement().toString();
            if (exchangeRateService.listExchangeRates(charCode, date).isEmpty()) {
                exchangeRateService.saveExchangeRate(new ExchangeRate(charCode, date, getValueByCharCodeFromDocument(charCode, document)));
            }
        }
    }

    @GetMapping()
    public String GetExchangeRates(@RequestParam String code, @RequestParam String string_date) throws IOException {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(string_date, formatter1);
        try {
            ExchangeRate exchangeRate = exchangeRateService.listExchangeRates(code, date).get(0);
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ExchangeRate.class, new ExchangeRateTypeAdapter()).registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
            System.out.println(gson.toJson(exchangeRate));
            return gson.toJson(exchangeRate);
        } catch (IndexOutOfBoundsException e) {
            Document document = GetXmlData.getCurDateXML(date);
            if (document == null) {
                throw new NullPointerException();
            } else if (document == GetXmlData.getCurDateXML("10/10/1000")) {
                    throw new IOException();
//                    я не знаю, что значит в ЦБ нет данных, допустим, что это будет если передать туда неправильную дату, или если метод вернёт null.
            }
            float value = getValueByCharCodeFromDocument(code, document);
            ExchangeRate exchangeRate = new ExchangeRate(code, date, value);
            exchangeRateService.saveExchangeRate(exchangeRate);
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ExchangeRate.class, new ExchangeRateTypeAdapter()).registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
            return gson.toJson(exchangeRate);
        }
    }
}