package com.example.demo.DataOperations.Gson;

import com.example.demo.Models.ExchangeRate;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExchangeRateTypeAdapter implements JsonSerializer<ExchangeRate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public JsonObject serialize(ExchangeRate exchangeRate, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();// не нужно парсить самому классы простые, спец настройки не требуются, можно не переопределять сериализатор/десериализаторы

        result.addProperty("CharCode", exchangeRate.getCharCode());
        result.addProperty("Date", exchangeRate.getDate().toString());
        result.addProperty("Value", exchangeRate.getValue());
        return result;
    }
}
