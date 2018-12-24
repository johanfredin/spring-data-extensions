package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.repository.VenueRepository;
import com.github.johanfredin.springdataextensions.service.VenueService;
import com.github.johanfredin.springdataextensions.service.impl.VenueServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
public abstract class AbstractVenueControllerTest<C extends ControllerBase<VenueService>> extends AbstractControllerTest<Venue, VenueRepository, VenueService, C> {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public VenueService initService() {
        return new VenueServiceImpl();
    }

    @Override
    public VenueRepository getRepository() {
        return this.venueRepository;
    }

    @Override
    protected List<Venue> initTestEntities() {
        List<Venue> venues = new ArrayList<>();
        venues.add(getEntity1());
        venues.add(getEntity2());
        return venues;
    }

    protected Venue getEntity1() {
        Venue venue = new Venue("The Whiskey", "info@whiskey.com", "http://www.thewhiskey.com", "0763112927", 100, 1L, Genre.GRUNGE_ROCK);
        venue.setContacts(TestFixture.getValidContactsWithoutReferences());
        venue.setAddress(TestFixture.getValidAddress());
        venue.setRelations();

        List<Artist> validArtists = TestFixture.getValidArtists();
        validArtists.get(0).setVenues(new ArrayList<>());
        validArtists.get(0).setReviews(new ArrayList<>());
        validArtists.get(0).addVenue(venue);
        validArtists.get(0).addReview(TestFixture.getValidReviewWithoutReferences(validArtists.get(0).getName()));
        validArtists.get(1).setVenues(new ArrayList<>());
        validArtists.get(1).setReviews(new ArrayList<>());
        validArtists.get(1).addVenue(venue);
        validArtists.get(1).addReview(TestFixture.getValidReviewWithoutReferences(validArtists.get(1).getName()));

        venue.setArtists(validArtists);

        venue.setReviews(TestFixture.getValidReviewsWithoutReferences(venue.getArtists().get(0).getName(), venue.getArtists().get(1).getName()));

        int notAgain = 0;
        for (VenueReview review : venue.getReviews()) {
            review.setVenue(venue);
            review.setArtist(venue.getArtists().get(notAgain));
            notAgain++;
        }


        return venue;
    }

    protected Venue getEntity2() {
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

}
