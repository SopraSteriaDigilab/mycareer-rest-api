package controller;

import static dataStructure.EmployeeProfile.EMPLOYEE_ID;
import static dataStructure.EmployeeProfile.USERNAME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.management.InvalidAttributeValueException;

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
import model.TestModels;
import services.EmployeeNotFoundException;
import services.EmployeeProfileService;
import services.EmployeeService;
import services.db.MongoOperations;
import services.db.MorphiaOperations;
import services.ews.EmailService;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@ContextConfiguration(classes = { Application.class })
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeProfileService employeeProfileService;

	@Autowired
	private EmployeeService employeeService;

	@MockBean
	private MorphiaOperations mockMorphiaOperations;

	@MockBean
	private EmailService emailService;

	@MockBean(name = "employeeOperations")
	private MongoOperations mockEmployeeOperations;

	@MockBean(name = "objectivesHistoriesOperations")
	private MongoOperations mockObjectivesHistoriesOperations;

	@MockBean(name = "developmentNeedsHistoriesOperations")
	private MongoOperations mockDevelopmentNeedsHistoriesOperations;

	@MockBean(name = "competenciesHistoriesOperations")
	private MongoOperations mockCompetenciesHistoriesOperations;

	@Before
	public void setUp() throws Exception {
		when(mockMorphiaOperations.getEmployeeProfile(USERNAME, "shnagi")).thenReturn(TestModels.newEmployeeProfile());
		when(mockMorphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, 666666l)).thenReturn(TestModels.newEmployee());
		when(mockMorphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, 666667l)).thenThrow(new EmployeeNotFoundException(
				"Employee not found based on the criteria: " + "EmployeeID" + ": " + 666667l));
		when(mockMorphiaOperations.getEmployeeFromEmailAddressOrThrow("aaaaa@bbbb.com"))
				.thenReturn(TestModels.newEmployee());
	}

	@Test
	public void testWelcomePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Welcome to the MyCareer Project")));
	}

	@Test
	public void testPortal() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/portal").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is3xxRedirection()).andExpect(content().string(equalTo("")));
	}

	@Test
	public void testLogMeIn() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/logMeIn").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(equalTo(
						"{\"employeeID\":666666,\"employeeType\":\"EMP\",\"surname\":\"Last\",\"forename\":\"First\",\"username\":\"Username\",\"emailAddresses\":{\"mail\":\"a@b.c\",\"targetAddress\":\"a@b.c\",\"userAddress\":null,\"preferred\":\"a@b.c\"},\"isManager\":true,\"hasHRDash\":false,\"company\":\"Company\",\"steriaDepartment\":\"Steria Department\",\"sector\":\"Sector\",\"superSector\":\"Super Sector\",\"reporteeCNs\":[\"First\",\"First\",\"First\"],\"accountExpires\":4085593200000,\"fullName\":\"First Last\"}")));
	}

	@Test
	public void testLogMeInBadID() throws Exception {
		when(mockMorphiaOperations.getEmployeeOrThrow(EMPLOYEE_ID, 666666l)).thenThrow(new EmployeeNotFoundException(
				"Employee not found based on the criteria: " + "EmployeeID" + ": " + -666666l));
		mockMvc.perform(MockMvcRequestBuilders.get("/logMeIn").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	//////////////////////////////////////////////////////////////////////////////
	//////////////////// OBJECTIVES END POINT METHODS FOLLOW////////////////////
	//////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetObjectives() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getObjectives/666666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].title", is("Title")));
	}

	@Test
	public void testGetObjectivesBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getObjectives/666667").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testAddObjective() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/addObjective/666666").param("title", "Title")
				.param("description", "Description").param("dueDate", "2099-09")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"success\":\"Objective added\",\"objectiveID\":2}")));
	}

	@Test
	public void testAddObjectiveBadId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/addObjective/666667").param("title", "Title")
				.param("description", "Description").param("dueDate", "2099-09")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testEditObjective() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/editObjective/666666").param("objectiveId", "1")
				.param("title", "Title").param("description", "Description").param("dueDate", "2099-09"))
				.andExpect(status().isOk()).andExpect(content().string(equalTo("Objective updated correctly")));
	}

	@Test
	public void testEditObjectiveBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/editObjective/666667").param("objectiveId", "1")
				.param("title", "Title").param("description", "Description").param("dueDate", "2099-09"))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testArchiveAndDeleteObjective() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/toggleObjectiveArchive/666666").param("objectiveId", "1"))
				.andExpect(status().isOk()).andExpect(content().string(equalTo("Objective updated")));
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteObjective/666666").param("objectiveId", "1")
				.param("comment", "Comment")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"success\":\"Objective deleted\",\"autoGeneratedNoteID\":2}")));
	}

	@Test
	public void testDeleteObjectiveBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteObjective/666667").param("objectiveId", "1")
				.param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testDeleteObjectiveUnarchived() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteObjective/666666").param("objectiveId", "1")
				.param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("{\"error\":\"Objective must be archived before deleting.\"}")));
	}

	@Test
	public void testUpdateObjective() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateObjectiveProgress/666666").param("objectiveId", "1")
				.param("progress", "1").param("comment", "Comment")).andExpect(status().isOk())
				.andExpect(content()
						.string(equalTo("{\"success\":\"Objective progress updated\",\"autoGeneratedNoteID\":null}")));
	}

	@Test
	public void testUpdateObjectiveComplete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateObjectiveProgress/666666").param("objectiveId", "1")
				.param("progress", "2").param("comment", "Comment")).andExpect(status().isOk())
				.andExpect(content()
						.string(equalTo("{\"success\":\"Objective progress updated\",\"autoGeneratedNoteID\":2}")));
	}

	@Test
	public void testUpdateObjectiveBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateObjectiveProgress/666667").param("objectiveId", "1")
				.param("progress", "1").param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testToggleObjectiveArchivedBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/toggleObjectiveArchive/666667").param("objectiveId", "1")
				.param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testToggleObjectiveArchivedBadObjectiveID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/toggleObjectiveArchive/666666").param("objectiveId", "3")
				.param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("{\"error\":\"Objective not found.\"}")));
	}

	//////////////////////////////////////////////////////////////////////////////
	////////////////// DEVELOPMENT NEEDS END POINT METHODS FOLLOW//////////////
	//////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetDevelopmentNeeds() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getDevelopmentNeeds/666666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].title", is("Title")));
	}

	@Test
	public void testGetDevelopmentNeedsBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getDevelopmentNeeds/666667").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testAddDevelopmentNeed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/addDevelopmentNeed/666666").param("title", "Title")
				.param("description", "Description").param("dueDate", "2099-09").param("category", "4"))
				.andExpect(status().isOk()).andExpect(
						content().string(equalTo("{\"developmentNeedID\":2,\"success\":\"Development need added\"}")));
	}

	@Test
	public void testAddDevelopmentNeedBad() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/addDevelopmentNeed/666667").param("title", "Title")
				.param("description", "Description").param("dueDate", "2099-09").param("category", "4"))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testEditDevelopmentNeed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/editDevelopmentNeed/666666").param("developmentNeedId", "1")
				.param("title", "Title").param("description", "Description").param("dueDate", "2099-09")
				.param("category", "2")).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Development Need updated correctly")));
	}

	@Test
	public void testEditDevelopmentNeedBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/editDevelopmentNeed/666667").param("developmentNeedId", "1")
				.param("title", "Title").param("description", "Description").param("dueDate", "2099-09")
				.param("category", "2")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testArchiveAndDeleteDevelopmentNeed() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/toggleDevelopmentNeedArchive/666666").param("developmentNeedId", "1"))
				.andExpect(status().isOk()).andExpect(content().string(equalTo("Development Need updated")));
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteDevelopmentNeed/666666").param("developmentNeedId", "1")
				.param("comment", "Comment")).andExpect(status().isOk())
				.andExpect(content()
						.string(equalTo("{\"success\":\"Development Need deleted\",\"autoGeneratedNoteID\":2}")));
	}

	@Test
	public void testDeleteDevelopmentNeedBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteDevelopmentNeed/666667").param("developmentNeedId", "1")
				.param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testDeleteDevelopmentNeedUnarchived() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/deleteDevelopmentNeed/666666").param("developmentNeedId", "1")
				.param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content()
						.string(equalTo("{\"error\":\"Development Need must be archived before deleting.\"}")));
	}

	@Test
	public void testUpdateDevelopmentNeed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateDevelopmentNeedProgress/666666")
				.param("developmentNeedId", "1").param("progress", "1").param("comment", "Comment"))
				.andExpect(status().isOk()).andExpect(content().string(
						equalTo("{\"success\":\"Development Need progress updated\",\"autoGeneratedNoteID\":null}")));
	}

	@Test
	public void testUpdateDevelopmentNeedComplete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateDevelopmentNeedProgress/666666")
				.param("developmentNeedId", "1").param("progress", "2").param("comment", "Comment"))
				.andExpect(status().isOk()).andExpect(content().string(
						equalTo("{\"success\":\"Development Need progress updated\",\"autoGeneratedNoteID\":2}")));
	}

	@Test
	public void testUpdateDevelopmentNeedBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/updateDevelopmentNeedProgress/666667")
				.param("developmentNeedId", "1").param("progress", "1").param("comment", "Comment"))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testToggleDevelopmentNeedArchivedBadID() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/toggleDevelopmentNeedArchive/666667")
				.param("developmentNeedId", "1").param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	@Test
	public void testToggleDevelopmentNeedArchivedBaddevelopmentNeedId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/toggleDevelopmentNeedArchive/666666")
				.param("developmentNeedId", "3").param("comment", "Comment")).andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("{\"error\":\"Development Need not found.\"}")));
	}

	//////////////////////////////////////////////////////////////////////////////
	////////////////// NOTES END POINT METHODS FOLLOW///////////////////////
	//////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetNotes() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getNotes/666666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].noteDescription", is("Description")));
	}

	@Test
	public void testGetNotesBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getNotes/666667").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("Employee not found based on the criteria: EmployeeID: 666667")));
	}

	@Test
	public void testAddNote() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/addNote/666666").param("providerName", "TestName")
				.param("noteDescription", "description").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"success\":\"Note added\",\"noteID\":2}")));
	}

	@Test
	public void testAddNoteBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/addNote/666667").param("providerName", "TestName")
				.param("noteDescription", "description").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("Employee not found based on the criteria: EmployeeID: 666667")));
	}

	//////////////////////////////////////////////////////////////////////////////
	///////////////////// FEEDBACK END POINT METHODS FOLLOW//////////////////
	//////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetFeedback() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getFeedback/666666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].feedbackDescription", is("Description")));
	}

	@Test
	public void testGetFeedbackBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getFeedback/666667").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("Employee not found based on the criteria: EmployeeID: 666667")));
	}

	@Test
	public void testAddFeedbackBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.post("/addFeedback/666667").param("emails", "aaaaa@bbbb.com")
				.param("feedback", "Test Feedback").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	//////////////////////////////////////////////////////////////////////////////
	/////////////// FEEDBACK REQUEST END POINT METHODS FOLLOW//////////////////
	//////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetFeedbackRequest() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getFeedbackRequests/666666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].recipient", is("a@b.c")));
	}

	@Test
	public void testGetFeedbackRequestBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getFeedbackRequests/666667").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

	//////////////////////////////////////////////////////////////////////////////
	/////////////// RATINGS END POINT METHODS FOLLOW/////////////////////
	//////////////////////////////////////////////////////////////////////////////

	@Test
	public void testGetRating() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getCurrentRating/666666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetRatingBadID() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/getCurrentRating/666667").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						equalTo("{\"error\":\"Employee not found based on the criteria: EmployeeID: 666667\"}")));
	}

}
