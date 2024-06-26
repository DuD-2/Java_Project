package com.example.demo1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

class YandexSpellerTest {
    YandexSpeller yandexSpeller = new YandexSpeller();
    ArrayList<String> expected = new ArrayList<>();

    @Test
    void CorrectText() throws IOException {
        ArrayList<String> actual = yandexSpeller.checkText("Я завтра уезжаю");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void TextWithOneTypo() throws IOException {
        expected.add("зввтра");
        ArrayList<String> actual = yandexSpeller.checkText("Я зввтра уезжаю");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void IncorrectText() throws IOException {
        expected.add("зввтра");
        expected.add("уеежаю");
        ArrayList<String> actual = yandexSpeller.checkText("Я зввтра уеежаю");
        Assertions.assertEquals(expected, actual);
    }
}