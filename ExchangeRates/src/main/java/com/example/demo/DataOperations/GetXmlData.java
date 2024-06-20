package com.example.demo.DataOperations;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;

@Component
public class GetXmlData {

    private static final String href = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static Dictionary<String, String> defaultCharCodeAndValuteIdDictionary;

    static {
        try {
            defaultCharCodeAndValuteIdDictionary = createCharCodeAndValuteIdDictionary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GetXmlData() throws IOException {
    }

    public static Dictionary<String, String> createCharCodeAndValuteIdDictionary(String date) throws IOException {
        LocalDate localDate = LocalDate.parse(date, formatter);
        Document document = Jsoup.connect(href + formatter.format(localDate)).get();
        Dictionary<String, String> charCodeAndValuteIdDictionary = new Hashtable<>();
        String key = null;
        String value = null;
        for (Element e : document.select("Valute")) {
            key = e.select("CharCode").text();
            value = e.attr("ID");
            charCodeAndValuteIdDictionary.put(key, value);
        }
        return charCodeAndValuteIdDictionary;
    }

    public static Dictionary<String, String> createCharCodeAndValuteIdDictionary() throws IOException {
        return createCharCodeAndValuteIdDictionary(formatter.format(LocalDate.now()));
    }

//    Я решил, что вместо того, чтобы перебирать постоянно xml, чтобы найти подходящий по CharCode элемент, лучше сразу сделать словарь со связью между CharCode и Valute ID.
//    Его можно сохранить хоть в json, хоть просто как enum и потом снова сохранять файл, если нужно будет его изменить. (по этой же причине я сделал перегрузку этого метода)
//    Поэтому я решил сделать метод, в котором можно получить этот словарь и потом вызывать его при прогрузке этого класса. Хотя я и не уверен, что это правильное решение.

    public static Document getCurDateXML(LocalDate date) throws IOException {
        return Jsoup.connect(href + date.format(formatter)).get();
    }

    public static Document getCurDateXML(String date) throws IOException {
        LocalDate localDate = LocalDate.parse(date, formatter);
        return getCurDateXML(localDate);
    }
}
