package controller;

import static dataStructure.EmailAddresses.MAIL;
import static dataStructure.EmailAddresses.TARGET_ADDRESS;
import static dataStructure.EmailAddresses.USER_ADDRESS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import application.Application;
import services.db.MongoOperations;

@RunWith(SpringRunner.class)
@WebMvcTest(DataController.class)
@ContextConfiguration(classes = { Application.class })
public class DataControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean(name = "employeeOperations")
	private MongoOperations mockEmployeeOperations;

	@Before
	public void setUp() {

	}

	@Test
	public void testGetAllEmailAddresses() throws Exception {
		Set<Object> emails = new HashSet<>();
		String email = "a@b.c";
		String email2 = "a@b.d";
		emails.add(email);
		emails.add(email2);

		when(mockEmployeeOperations.getFieldValuesAsSet("emailAddresses", MAIL, TARGET_ADDRESS, USER_ADDRESS))
				.thenReturn(emails);

		mockMvc.perform(MockMvcRequestBuilders.get("/data/getAllEmailAddresses").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().string(equalTo("[\"a@b.d\",\"a@b.c\"]")));
	}

}
