package org.pkfrc.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.pkfrc.core.dto.security.AuthenticationRequest;
import org.pkfrc.core.dto.security.CreateUserDTO;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.projurise.repo.product.ProductRepository;
import org.pkfrc.vending.entities.product.Product;
import org.pkfrc.vending.services.purchase.BuyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class vendingEndPointTest {

	@Autowired
	private MockMvc mockMvc;

	public static String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aG9yaXRpZXMiOltdLCJleHAiOjE2NDYzMzAwMDZ9.cxsPileM5m3x1JoXXHjJ0kNXWJ4NaM5wwarwySxNUID26lPxSaY35YTTBVczC-76BbInHg-mzMAEjFv6-6Xs8w";

	@Autowired
	private ProductRepository productRepo;	

	@Test
	@Rollback(value = true)
	public void buy() throws Exception {
		Product product = new Product();
		product.setCode("00001");
		product.setDesignation("My test product");
		product.setCost(50.00);
		product.setQuantity(50.0);
		productRepo.save(product);
		BuyRequest request = new BuyRequest(product.getId(), 2);

		String requestJson = new ObjectMapper().writeValueAsString(request);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/purchase/buy").header("Authorization", token)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(requestJson);
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
	
	@Test
	public void login() throws Exception {
		AuthenticationRequest auth = new AuthenticationRequest("root", "123456");
		String authJson = new ObjectMapper().writeValueAsString(auth);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/login").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(authJson);
		MvcResult result = this.mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String response = result.getResponse().getContentAsString();
		response = response.replace("{\"token\": \"", "");
		System.out.println(token);
	}

	@Test
    public void deposit() throws Exception {			
		MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();		
		requestParams.add("coin", "50");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/deposit")
				.header("Authorization", token)
	            .accept(MediaType.APPLICATION_JSON)
	            .contentType(MediaType.APPLICATION_JSON).params(requestParams);
		this.mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk());
    }

	@Test
	@Order(2)
	public void register() throws Exception {
		CreateUserDTO buyer = new CreateUserDTO(50000.0, "buyer", "123456", EUserType.buyer);
		String buyerJson = new ObjectMapper().writeValueAsString(buyer);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/register").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(buyerJson);
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

}