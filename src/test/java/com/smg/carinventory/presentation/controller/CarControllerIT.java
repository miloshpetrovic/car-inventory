package com.smg.carinventory.presentation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smg.carinventory.application.service.CarEventPublisherService;
import com.smg.carinventory.application.service.CarService;
import com.smg.carinventory.application.service.ManufacturerService;
import com.smg.carinventory.application.service.ModelService;
import com.smg.carinventory.presentation.dto.CarDataDTO;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=KafkaAutoConfiguration.class)
class CarControllerIT {

	private MockMvc mockMvc;
	
	@MockBean
	private CarEventPublisherService publisher;
	@MockBean
	private KafkaAdmin kafkaAdmin;
	
	@Autowired
	private ManufacturerService manufacturerService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CarService carService;
	
	static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres")
			.withUsername("postgres")
			.withPassword("postgres")
			.withDatabaseName("smg");
	
	
	@DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
	
	@BeforeAll
	static void before() {
		container.start();
	}
	
	@AfterAll
	static void after() {
		container.stop();
	}
	
	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(
				new ManufacturerController(manufacturerService),
				new ModelController(modelService), 
				new CarController(carService)).build();
	}
	
	@Test
	void doesManufacturersEndpointReturnsFivePredefinedEntries() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/smg/manufacturers")
				.accept("application/json")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(5));
	}

	@Test
	void doesManufacturersEndpointReturnsConcreeteManufacturer() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/smg/manufacturers/toyota")
				.accept("application/json")
				.contentType("application/json"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Toyota"));
	}
	
	@Test
	void doesModelsEndpointReturnsFivePredefinedEntries() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/smg/manufacturers/toyota/models")
				.accept("application/json")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(5));
	}

	@Test
	void doesModelsEndpointReturnsConcreeteModel() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/smg/manufacturers/toyota/models/yaris")
				.accept("application/json")
				.contentType("application/json"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name").value("Yaris"));
	}

	@Test
	void doesModelCarsEndpointSavesNewCarSuccessfully() throws Exception {
		
		CarDataDTO car = new CarDataDTO();
		car.setMileage(1234);
		car.setYear(2010);
		car.setPrice(3000.0);
		car.setVin("vin");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/smg/manufacturers/toyota/models/yaris/cars")
				.accept("application/json")
				.contentType("application/json")
				.content(jsonOf(car)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.year").value(2010));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/smg/manufacturers/toyota/models/yaris/cars")
				.accept("application/json")
				.contentType("application/json"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(1));
	}
	
	
	private String jsonOf(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
