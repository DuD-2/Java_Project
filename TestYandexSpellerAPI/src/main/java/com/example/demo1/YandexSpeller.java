package com.example.demo1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YandexSpeller {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class YandexSpellerResponse {
        public int code;
        public int pos;
        public int row;
        public int col;
        public int len;
        public String word;
        public ArrayList<String> s;
    }

    public ArrayList<String> checkText(String str) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        String uri = "https://speller.yandex.net/services/spellservice.json/checkText?text=" + str.replace(" ", "%20");
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        String content = EntityUtils.toString(response.getEntity());
        System.out.println(content);
        if (!content.equals("[]")) {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<YandexSpellerResponse> yandexSpellerResponseList = objectMapper.readValue(content, typeFactory.constructCollectionType(List.class, YandexSpellerResponse.class));
            for (YandexSpellerResponse yandexSpellerResponse : yandexSpellerResponseList) {
                result.add(yandexSpellerResponse.word);
            }
        }
        return result;
    }
}