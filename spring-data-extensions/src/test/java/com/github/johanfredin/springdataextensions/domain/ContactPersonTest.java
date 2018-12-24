package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ContactPersonTest extends EntityTest<ContactPerson> {

    @Override
    public void testDefaultConstructor() {
        ContactPerson contactPerson = getEmptyInstance();
        assertEquals("Id should be 0", 0L, contactPerson.getId());
        assertNull("Contact phone nr should be empty", contactPerson.getPhoneNr());
        assertNull("Contact role should be null", contactPerson.getRole());
        assertNull("Contact venue should be null", contactPerson.getVenue());

        // Properties inherited from DescribingEntity
        assertNull("Contact should have no name yet", contactPerson.getName());
        assertNull("Contact email should be empty", contactPerson.getEmail());
        assertNull("Contact should have no city yet", contactPerson.getCity());
        assertNull("Contact should have no country yet", contactPerson.getCountry());
    }

    @Override
    public void testConstructorWithParameters() {
        ContactPerson contactPerson = getPopulatedInstance();
        assertEquals("Contact Id should be 0", 0, contactPerson.getId());
        assertEquals("Contact phone nr should be 123456789", TestFixture.FAKE_PHONE, contactPerson.getPhoneNr());
        assertEquals("Contact role should be NOT_SET", MemberRole.NOT_SET, contactPerson.getRole());
        assertEquals("Contact display Role should be " + MemberRole.NOT_SET.getRole(), MemberRole.NOT_SET.getRole(), contactPerson.getRole().getRole());

        // Properties inherited from DescribingEntity
        assertEquals("Contact Name should be " + TestFixture.FAKE_NAME, TestFixture.FAKE_NAME, contactPerson.getName());
        assertEquals("Contact email should be " + TestFixture.getFakeMail(TestFixture.FAKE_NAME), TestFixture.getFakeMail(TestFixture.FAKE_NAME), contactPerson.getEmail());
        assertEquals("Contact city should be " + TestFixture.FAKE_CITY, TestFixture.FAKE_CITY, contactPerson.getCity());
        assertEquals("Contact country should be " + TestFixture.FAKE_COUNTRY, TestFixture.FAKE_COUNTRY, contactPerson.getCountry());
    }

    @Test
    public void testAlteringRole() {
        ContactPerson contactPerson = getPopulatedInstance();
        assertEquals("Role should be NOT_SET", MemberRole.NOT_SET, contactPerson.getRole());
        assertEquals("display Role should be " + MemberRole.NOT_SET.getRole(), MemberRole.NOT_SET.getRole(), contactPerson.getRole().getRole());
        contactPerson.setRole(MemberRole.MANAGER);
        assertEquals("Role should now be", MemberRole.MANAGER, contactPerson.getRole());
    }

    @Test
    public void testVenueRelation() {
        ContactPerson contactPerson = getPopulatedInstance();
        assertNotNull("Venue should not be null", contactPerson.getVenue());
        assertEquals("Name of Venue connected to Contact Person should be " + TestFixture.FAKE_VENUE_NAME, TestFixture.FAKE_VENUE_NAME, contactPerson.getVenue().getName());
    }

    @Override
    public void testRelations() {
        List<ContactPerson> validContacts = TestFixture.getValidContacts();
        Venue venue = TestFixture.getValidVenue();
        venue.setContacts(validContacts);
        venue.setRelations();

        for (ContactPerson contact : validContacts) {
            assertNotNull("ContactPerson should now have a venue", contact.getVenue());
            assertTrue("Venue should equal the venue of the contacts", venue.equals(contact.getVenue()));
        }
    }

    @Override
    public void testCopyData() {
        ContactPerson populatedContactPerson = getPopulatedInstance();
        populatedContactPerson.setId(TestFixture.FAKE_ID);

        ContactPerson emptyPerson = getEmptyInstance();
        emptyPerson.copyDataFromEntity(populatedContactPerson);

        assertEquals("Empty person Id should now be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptyPerson.getId());
        assertEquals("Empty person phone nr should now be 123456789", TestFixture.FAKE_PHONE, emptyPerson.getPhoneNr());
        assertEquals("Empty person role should now be NOT_SET", MemberRole.NOT_SET, emptyPerson.getRole());
        assertEquals("Empty person Role should now be " + MemberRole.NOT_SET.getRole(), MemberRole.NOT_SET.getRole(), emptyPerson.getRole().getRole());

        // Properties inherited from DescribingEntity
        assertEquals("Empty person Name should now be " + TestFixture.FAKE_NAME, TestFixture.FAKE_NAME, emptyPerson.getName());
        assertEquals("Empty person email should now be " + TestFixture.getFakeMail(TestFixture.FAKE_NAME), TestFixture.getFakeMail(TestFixture.FAKE_NAME), emptyPerson.getEmail());
        assertEquals("Contact city should now be " + TestFixture.FAKE_CITY, TestFixture.FAKE_CITY, emptyPerson.getCity());
        assertEquals("Contact country should now be " + TestFixture.FAKE_COUNTRY, TestFixture.FAKE_COUNTRY, emptyPerson.getCountry());
    }


    @Override
    protected ContactPerson getEmptyInstance() {
        return new ContactPerson();
    }

    @Override
    protected ContactPerson getPopulatedInstance() {
        return TestFixture.getValidContactPerson();
    }

}
