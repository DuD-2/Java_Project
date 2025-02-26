package com.example.demo;

import com.example.demo.Models.DTO.ValuteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ExchangeRatesApplicationTests {

	@Test
	void getValuteByCharCodeAndDateTest() throws IOException {
		ValuteDTO valuteDTO = new ValuteDTO("AUD", "2002-03-02");
        System.out.println(new ObjectMapper().findAndRegisterModules().writeValueAsString(valuteDTO));
	}
}
