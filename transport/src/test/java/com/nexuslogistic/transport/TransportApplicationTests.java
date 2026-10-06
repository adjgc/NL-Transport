package com.nexuslogistic.transport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:transport-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class TransportApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void dashboardRendersDemoShipments() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Centro de operaciones")))
				.andExpect(content().string(containsString("NL-26041")))
				.andExpect(content().string(containsString("rel=\"icon\" type=\"image/png\" href=\"/img/nltransport.png\"")))
				.andExpect(content().string(containsString("class=\"brand-mark\" src=\"/img/nltransport.png\"")))
				.andExpect(content().string(containsString("class=\"avatar\" src=\"/img/nltransport.png\"")));
	}

	@Test
	void logoImageIsServedAsPng() throws Exception {
		mockMvc.perform(get("/img/nltransport.png"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG));
	}
}
