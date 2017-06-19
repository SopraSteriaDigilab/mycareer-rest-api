package dataStructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Models;

public class EmailAddressesTest {

	private EmailAddresses unitUnderTest;

	@Test
	public void getPreferredNullUserAddressTest() {

		unitUnderTest = new EmailAddresses.Builder().mail(Models.EMAIL_ADRESS).build();

		String expectedEmail = "a@b.c";

		String actualEmail = unitUnderTest.getPreferred();

		assertEquals(expectedEmail, actualEmail);
	}

	@Test
	public void getPreferredEmptyUserAddressTest() {

		unitUnderTest = new EmailAddresses.Builder().mail(Models.EMAIL_ADRESS).userAddress("").build();

		String expectedEmail = "a@b.c";

		String actualEmail = unitUnderTest.getPreferred();

		assertEquals(expectedEmail, actualEmail);
	}

	@Test
	public void getPrefferedUserAddressTest() {

		unitUnderTest = new EmailAddresses.Builder().mail(Models.EMAIL_ADRESS).userAddress("c@b.a").build();

		String expectedEmail = "c@b.a";

		String actualEmail = unitUnderTest.getPreferred();

		assertEquals(expectedEmail, actualEmail);
	}
}
