package com.example.demo.DataOperations;

import com.example.demo.Models.DTO.ValCursDTO;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class GetXmlData {
    private String URL = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private CloseableHttpClient client = HttpClients.createDefault();

    public ValCursDTO getValCurs(String date) throws IOException {
        return getValCurs(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    public ValCursDTO getValCurs(LocalDate date) throws IOException {
        HttpUriRequest httpGet = new HttpGet(URL + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date));
        CloseableHttpResponse response = client.execute(httpGet);
        return new XmlMapper().findAndRegisterModules().readValue(EntityUtils.toString(response.getEntity()), ValCursDTO.class);
    }

}