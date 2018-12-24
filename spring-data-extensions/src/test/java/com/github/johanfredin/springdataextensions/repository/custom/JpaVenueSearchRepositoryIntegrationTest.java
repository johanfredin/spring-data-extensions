package com.github.johanfredin.springdataextensions.repository.custom;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.repository.VenueRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class JpaVenueSearchRepositoryIntegrationTest {

    @Autowired
    private VenueSearchRepository venueSeachRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Test
    public void testGetVenuesMatchingSearchParams() {
        // Persist some data
        List<Venue> venues = this.venueRepository.save(getEntity1(), getEntity2());
        Venue v1 = venues.get(0);
        Venue v2 = venues.get(1);

        // Fetch some testing variables
        String v1Name = v1.getName();                    // The Whiskey
        String v2Name = v2.getName();                    // Sticky Fingers
        String v1Cty = v1.getAddress().getCity();        // City
        String v2Cty = v2.getAddress().getCity();        // City
        String v1Ctr = v1.getAddress().getCountry();    // Country
        String v2Ctr = v2.getAddress().getCountry();    // Country
        Genre v1g = v1.getGenre();                        // Grunge-Rock
        Genre v2g = v2.getGenre();                        // Grunge-Rock

        // Test finding a venues matching v1's name and city (should result in 1 match)
        List<Venue> matchOnV1NameAndCty = search(v1Name, v1Cty, null, null);
        assertEquals("Matching on v1 name and city should give 1 result", 1, matchOnV1NameAndCty.size());
        assertTrue("Result of first query should be v1", v1 == matchOnV1NameAndCty.get(0));

        // Test finding venues on v1 and v2's city (should result in 2 matches)
        // We will make a substringed version since the query should match a name that also starts with the value
        List<Venue> matchOnCty = search(null, v1Cty.substring(0, v1Cty.length() - 3).toUpperCase(), null, null);
        assertEquals("Matching on v1 and v2 city should give 2 results", 2, matchOnCty.size());

        // Test finding venues on city, country and genre params (should result in 2 matches)
        List<Venue> matchOnCtyAndCtrAndGr = search("", v2Cty, v2Ctr, v2g);
        assertEquals("Matching on city, country and genre should give 2 results", 2, matchOnCtyAndCtrAndGr.size());

        // Test finding venues matching ALL params (should result in 1 match)
        List<Venue> matchOnAllV1Params = search(v1Name, v1Cty, v1Ctr, v1g);
        assertEquals("Matching on all v1 params should give 1 result", 1, matchOnAllV1Params.size());
        assertTrue("Result of first query should be v1", v1 == matchOnAllV1Params.get(0));

        // Try the same but switch to v2s name
        List<Venue> matchOnAllV2Params = search(v2Name, v2Cty, v2Ctr, v2g);
        assertEquals("Matching on all v1 params should give 1 result", 1, matchOnAllV2Params.size());
        assertTrue("Result of first query should be v1", v2 == matchOnAllV2Params.get(0));
    }

    @Test
    public void testGetVenuesMatchingSearchParamsVerifiyCaseInsensiveAndLikeQuery() {
        // Persist some data
        List<Venue> venues = this.venueRepository.save(getEntity1(), getEntity2());
        Venue v1 = venues.get(0);
        Venue v2 = venues.get(1);

        // Fetch some testing variables
        String v1Name = lprc(v1.getName(), 4);                    // The Whiskey
        String v2Name = lprc(v2.getName(), 2);                    // Sticky Fingers
        String v1Cty = lprc(v1.getAddress().getCity(), 2);        // City
        String v2Cty = lprc(v2.getAddress().getCity(), 3);        // City
        String v1Ctr = lprc(v1.getAddress().getCountry(), 4);    // Country
        String v2Ctr = lprc(v2.getAddress().getCountry(), 1);    // Country
        Genre v1g = v1.getGenre();                                // Grunge-Rock
        Genre v2g = v2.getGenre();                                // Grunge-Rock

        // Test finding a venues matching v1's name and city (should result in 1 match)
        List<Venue> matchOnV1NameAndCty = search(v1Name, v1Cty, null, null);
        assertEquals("Matching on v1 name and city should give 1 result", 1, matchOnV1NameAndCty.size());
        assertTrue("Result of first query should be v1", v1 == matchOnV1NameAndCty.get(0));

        // Test finding venues on v1 and v2's city (should result in 2 matches)
        // We will make a substringed version since the query should match a name that also starts with the value
        List<Venue> matchOnCty = search(null, v1Cty, null, null);
        assertEquals("Matching on v1 and v2 city should give 2 results", 2, matchOnCty.size());

        // Test finding venues on city, country and genre params (should result in 2 matches)
        List<Venue> matchOnCtyAndCtrAndGr = search("", v2Cty, v2Ctr, v2g);
        assertEquals("Matching on city, country and genre should give 2 results", 2, matchOnCtyAndCtrAndGr.size());

        // Test finding venues matching ALL params (should result in 1 match)
        List<Venue> matchOnAllV1Params = search(v1Name, v1Cty, v1Ctr, v1g);
        assertEquals("Matching on all v1 params should give 1 result", 1, matchOnAllV1Params.size());
        assertTrue("Result of first query should be v1", v1 == matchOnAllV1Params.get(0));

        // Try the same but switch to v2s name
        List<Venue> matchOnAllV2Params = search(v2Name, v2Cty, v2Ctr, v2g);
        assertEquals("Matching on all v1 params should give 1 result", 1, matchOnAllV2Params.size());
        assertTrue("Result of first query should be v1", v2 == matchOnAllV2Params.get(0));
    }

    @Test
    public void testGetVenuesMatchingSearchParamsWithLimit() {
        List<Venue> venues = this.venueRepository.save(getEntity1(), getEntity2());
        String city = venues.get(0).getAddress().getCity();

        // Both persisted venues should have the same city, so a query with that city should result in 2 venues
        assertEquals("Fetching venues based on city=" + city + " without limit should result in 2 matches", 2, search(null, city, null, null).size());

        // Same query with a limit set to 1 should result in 1 match
        assertEquals("Fetching venues based on city=" + city + " with limit should result in 1 matches", 1, search(null, city, null, null, 1).size());
    }

    private List<Venue> search(String p1, String p2, String p3, Genre p4) {
        return this.venueSeachRepository.getVenuesMatchingSearchParams(p1, p2, p3, p4);
    }

    private List<Venue> search(String p1, String p2, String p3, Genre p4, int results) {
        return this.venueSeachRepository.getVenuesMatchingSearchParams(p1, p2, p3, p4, results);
    }

    private Venue getEntity1() {
        Venue venue = new Venue("The Whiskey", "info@whiskey.com", "http://www.thewhiskey.com", "0763112927", 100, 1L, Genre.GRUNGE_ROCK);
        venue.setContacts(TestFixture.getValidContactsWithoutReferences());
        venue.setReviews(TestFixture.getValidReviewsWithoutReferences());
        venue.setAddress(TestFixture.getValidAddress());
        venue.setRelations();

        List<Artist> validArtists = TestFixture.getValidArtists();
        validArtists.get(0).setVenues(new ArrayList<Venue>());
        validArtists.get(0).addVenue(venue);
        validArtists.get(1).setVenues(new ArrayList<Venue>());
        validArtists.get(1).addVenue(venue);

        venue.setArtists(validArtists);

        return venue;
    }

    private Venue getEntity2() {
        Venue venue = new Venue("Sticky Fingers", "info@sticky.com", "http://www.stickyfingers.com", "0761253033", 1000, 2L, Genre.GRUNGE_ROCK);
        venue.setContacts(TestFixture.getValidContactsWithoutReferences());
        venue.setReviews(TestFixture.getValidReviewsWithoutReferences());
        venue.setAddress(TestFixture.getValidAddress());
        venue.setRelations();

        List<Artist> validArtists = TestFixture.getValidArtists();
        validArtists.get(0).setVenues(new ArrayList<Venue>());
        validArtists.get(0).addVenue(venue);
        validArtists.get(1).setVenues(new ArrayList<Venue>());
        validArtists.get(1).addVenue(venue);

        venue.setArtists(validArtists);

        return venue;
    }

    /**
     * @param val the value to transform
     * @param cut how much to shrink the value
     * @return a shortened substring of the val with random casing
     */
    private String lprc(String val, int cut) {
        val = val.substring(0, (val.length() - cut));
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : val.toCharArray()) {
            switch (i) {
                case 0:
                    sb.append(Character.toLowerCase(c));
                    break;
                case 1:
                    sb.append(Character.toUpperCase(c));
                    break;
            }
            i = i >= 1 ? 0 : (i + 1);
        }
        return sb.toString();
    }


}
