package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.constants.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class AddressTest extends EntityTest<Address> {

    @Override
    public void testDefaultConstructor() {
        Address address = getEmptyInstance();
        assertEquals("Id should be", 0, address.getId());
        assertEquals("Latitude should be " + Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LATITUDE, address.getLatitude(), 1);
        assertEquals("Longitude should be " + Constants.DEFAULT_LONGITUDE, Constants.DEFAULT_LONGITUDE, address.getLongitude(), 1);

        assertNull("Street should be empty", address.getStreet());
        assertNull("Zip code should be empty", address.getZipCode());
        assertNull("City shuld be empty", address.getCity());
        assertNull("State or region should be empty", address.getStateOrRegion());
        assertNull("Country should be empty", address.getCountry());
    }

    @Override
    public void testConstructorWithParameters() {
        Address address = getPopulatedInstance();
        assertEquals("Id should be 0", 0, address.getId());
        assertEquals("Street should be Street", "Street", address.getStreet());
        assertEquals("City should be City", "City", address.getCity());
        assertEquals("Zip code should be Zippie", "Zippie", address.getZipCode());
        assertEquals("State or region should be Region", "Region", address.getStateOrRegion());
        assertEquals("Country should be Country", "Country", address.getCountry());
    }

    @Test
    public void testCoordinates() {
        Address address = getPopulatedInstance();
        assertEquals("Latitude should be " + Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LATITUDE, address.getLatitude(), 1);
        assertEquals("Longitude should be " + Constants.DEFAULT_LONGITUDE, Constants.DEFAULT_LONGITUDE, address.getLongitude(), 1);
        address.setLatitude(1D);
        address.setLongitude(2D);
        assertEquals("Latitude should now be \"1\"", 1D, address.getLatitude(), 0);
        assertEquals("Longitude should now be \"2\"", 2D, address.getLongitude(), 0);
    }

    @Override
    public void testRelations() {
        Address address = getPopulatedInstance();
        Venue venue = TestFixture.getValidVenue();
        venue.setAddress(address);
        venue.setRelations();
        assertNotNull("Address should now have a venue", address.getVenue());
        assertTrue("Venue and Addresses venue should match", venue.equals(address.getVenue()));
    }

    @Override
    public void testCopyData() {
        Address populatedAddress = getPopulatedInstance();
        populatedAddress.setId(TestFixture.FAKE_ID);

        Address emptyAddress = getEmptyInstance();

        emptyAddress.copyDataFromEntity(populatedAddress);
        assertEquals("Empty address Id should be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptyAddress.getId());
        assertEquals("Empty address Street should be Street", "Street", emptyAddress.getStreet());
        assertEquals("Empty address City should be City", "City", emptyAddress.getCity());
        assertEquals("Empty address Zip code should be Zippie", "Zippie", emptyAddress.getZipCode());
        assertEquals("Empty address State or region should be Region", "Region", emptyAddress.getStateOrRegion());
        assertEquals("Empty address Country should be Country", "Country", emptyAddress.getCountry());
    }

    @Override
    protected Address getEmptyInstance() {
        return new Address();
    }

    @Override
    protected Address getPopulatedInstance() {
        return TestFixture.getValidAddress();
    }

}
