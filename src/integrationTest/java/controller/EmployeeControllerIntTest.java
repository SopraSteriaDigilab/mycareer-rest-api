package controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import application.Application;
import dataStructure.Employee;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(Employee.class)
@ContextConfiguration(classes = { Application.class })
public class EmployeeControllerIntTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getWelcomePageTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Welcome to the MyCareer Project")));
	}

	//////////////////////////////////////////////////////////////////////////////
	////////// OBJECTIVES END POINT METHODS FOLLOW
	/////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetObjectivesTestValidID() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/getObjectives/666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].title", is("test obj 1")))
				.andExpect(jsonPath("$[0].description", is("test obj description 1")))
				.andExpect(jsonPath("$[0].proposedBy", is("Static TESTERSON")));
	}

	@Test
	public void testGetObjectivesTestInvalidID() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/getObjectives/111111").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(equalTo(
						"{\"error\":\"Employee not found based on the criteria: profile.employeeID: 111111\"}")));
	}

	@Ignore
	@Test
	public void testAddObjectiveValidEmployee() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.post("/addObjective/666666?title=Title&description=Description&dueDate=2099-09")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testAddObjectiveInvalidEmployee() throws Exception {
		mvc.perform(
				MockMvcRequestBuilders.post("/addObjective/111111?title=Title&description=Description&dueDate=2099-09")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(equalTo(
						"{\"error\":\"Employee not found based on the criteria: profile.employeeID: 111111\"}")));
	}

	//////////////////////////////////////////////////////////////////////////////
	///////////////// DEVELOPMENT NEEDS END POINT METHODS FOLLOW
	/////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetDevelopmentNeedsTestValidID() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/getDevelopmentNeeds/666").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
		.andExpect(jsonPath("$[0].title", is("test dev need 1")))
		.andExpect(jsonPath("$[0].description", is("test dev need description 1")))
		.andExpect(jsonPath("$[0].proposedBy", is("Static TESTERSON")));
	}

	@Test
	public void testGetDevelopmentNeedsTestInvalidID() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/getDevelopmentNeeds/111111").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(equalTo(
						"{\"error\":\"Employee not found based on the criteria: profile.employeeID: 111111\"}")));
	}
}
