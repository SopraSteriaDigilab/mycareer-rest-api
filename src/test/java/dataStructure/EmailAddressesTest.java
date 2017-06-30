package dataStructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.TestModels;

public class EmailAddressesTest {

	private EmailAddresses unitUnderTest;

	@Test
	public void getPreferredNullUserAddressTest() {

		unitUnderTest = new EmailAddresses.Builder().mail(TestModels.EMAIL_ADDRESS).build();

		String expectedEmail = "a@b.c";

		String actualEmail = unitUnderTest.getPreferred();

		assertEquals(expectedEmail, actualEmail);
	}

	@Test
	public void getPreferredEmptyUserAddressTest() {

		unitUnderTest = new EmailAddresses.Builder().mail(TestModels.EMAIL_ADDRESS).userAddress("").build();

		String expectedEmail = "a@b.c";

		String actualEmail = unitUnderTest.getPreferred();

		assertEquals(expectedEmail, actualEmail);
	}

	@Test
	public void getPrefferedUserAddressTest() {

		unitUnderTest = new EmailAddresses.Builder().mail(TestModels.EMAIL_ADDRESS).userAddress("c@b.a").build();

		String expectedEmail = "c@b.a";

		String actualEmail = unitUnderTest.getPreferred();

		assertEquals(expectedEmail, actualEmail);
	}
}
