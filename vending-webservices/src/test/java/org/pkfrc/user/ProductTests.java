package org.pkfrc.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.pkfrc.core.services.base.ServiceData;
import org.pkfrc.vending.entities.product.Product;
import org.pkfrc.vending.services.product.IProductService;
import org.pkfrc.vending.ws.product.ProductWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductWS.class)
public class ProductTests {

	@MockBean
	IProductService productService;

	@Autowired
	MockMvc mockMvc;

	@Test
	public void testfindAll() throws Exception {
		Product product = new Product();
		product.setCode("testing");
		product.setCost(50.00);
		product.setQuantity(50.00);
		ServiceData<Product> produits = new ServiceData<>();
		produits.getRecords().add(product);

		Mockito.when(productService.findAll()).thenReturn(produits);

//		mockMvc.perform(get("/employee")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
//				.andExpect(jsonPath("$[0].firstName", Matchers.is("Lokesh")));
	}
}