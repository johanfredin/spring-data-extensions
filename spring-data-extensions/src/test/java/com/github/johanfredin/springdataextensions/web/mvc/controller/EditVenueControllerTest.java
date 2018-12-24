package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.*;
import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.EditVenueBean;
import com.pocstage.venuehub.domain.*;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.service.VenueReviewService;
import com.github.johanfredin.springdataextensions.util.comparator.DateComparator;
import com.github.johanfredin.springdataextensions.web.MvcErrors;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
public class EditVenueControllerTest extends AbstractVenueControllerTest<EditVenueController> {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private VenueReviewService reviewService;

    @Autowired
    private DateComparator dateComparator;

    private Venue venue;
    private Member member;
    private Artist artist;
    private VenueReview review;
    private ContactPerson contact;

    @Override
    public void init() {
        super.init();
        this.controller.setMemberService(this.memberService);
        this.controller.setArtistService(this.artistService);
        this.controller.setReviewService(this.reviewService);
        this.controller.setDateComparator(this.dateComparator);

        // Persist 2 members
        this.memberService.save(TestFixture.getValidMemberWithoutReferences("zolost", "cobain123", "Johan"));
        this.memberService.save(TestFixture.getValidMemberWithoutReferences("alex", "hetgurka95", "Akex"));

        // Assign the members to the artists
        int fuggly = 0; // (don't do this ever again!)
        for (Venue venue : getTestEntities()) {
            for (Artist artist : venue.getArtists()) {
                artist.setMembers(new ArrayList<>());
                artist.addMember(memberService.findAllById().get(fuggly));
                this.memberService.save(artist.getMembers());
            }
            fuggly++;
        }

        // Persist the artists
        this.artistService.save(getTestEntities().get(0).getArtists());
        this.artistService.save(getTestEntities().get(1).getArtists());

        // Persist the venues
        this.service.save(getTestEntities());

        this.venue = service.findAllById().get(0);
        this.artist = artistService.findAllById().get(0);
        this.member = memberService.findAllById().get(0);
        this.review = reviewService.findAllById().get(0);
        this.contact = venue.getContacts().get(0);
    }

    @Test
    public void verifyInit() {
        assertNotNull("Controller is alive", controller);
        assertNotNull("Service is alive", service);
        assertNotNull("Repository is alive", getRepository());
        assertNotNull("Review service is alive", reviewService);
        assertNotNull("Member service is alive", memberService);
        assertNotNull("Artist service is alive", artistService);
        assertNotNull("Date service is alive", dateComparator);

        assertNotNull("Venue alive", venue);
        assertNotNull("Member alive", member);
        assertNotNull("Artist alive", artist);
        assertNotNull("Review alive", review);
        assertNotNull("Contact is alive", contact);

        assertTrue("Venues exist in db", service.findAllById().size() > 0);
        assertTrue("Members exist in db", memberService.findAllById().size() > 0);
        assertTrue("Artists exist in db", artistService.findAllById().size() > 0);
        assertTrue("Reviews exist in db", reviewService.findAllById().size() > 0);
    }

    @Override
    public void testIndex() throws Exception {
        String url = MvcUtils.directTo(Page.EDIT_VENUE_FW, venue, artist, member);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(getValidForwardIndexUrl()));
    }

    @Test
    public void testIndexNewVenue() {
        controller.index(0L, 1L, 1L, mockModel);
        EditVenueBean bean = (EditVenueBean) mockModel.get("editVenueBean");
        assertFalse("Venue of bean is not a persisted entity", bean.getVenue().isExistingEntity());
    }

    @Test
    public void testIndexExistingVenue() {
        controller.index(venue.getId(), artist.getId(), member.getId(), mockModel);
        EditVenueBean bean = (EditVenueBean) mockModel.get("editVenueBean");
        Venue venue = bean.getVenue();
        assertTrue("Venue of bean is not a persisted entity", venue.isExistingEntity());
    }

    /*
     * (non-Javadoc)
     * Create a new venue
     * @see AbstractControllerTest#testHandleSubmitAllGood()
     */
    @Override
    public void testHandleSubmitAllGood() {
        Venue venue = TestFixture.getValidVenueWithoutReferences("The Seabiscuit", true);

        // Verify venue has no references
        assertTrue("Venue should have no artists", venue.getArtists().isEmpty());
        assertTrue("Venue should have no reviews", venue.getReviews().isEmpty());
        assertTrue("Venue should have no contacts", venue.getContacts().isEmpty());

        // Add some references to begin with
        ContactPerson contact = TestFixture.getValidContactPersonWithoutReferences("Jon Doe");
        Address address = TestFixture.getValidAddress();

        venue.setAddress(address);
        venue.addContact(contact);

        // Verify address and contact does not yet belong to venue
        assertNull("Address has no venue", address.getVenue());
        assertNull("Contact has no venue", contact.getVenue());

        // Create bean for post method
        EditVenueBean bean = new EditVenueBean(venue, member.getId(), artist.getId());

        // Call post method
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = MvcUtils.redirectTo(Page.EDIT_ARTIST_FW, nre("member", bean.getMemberId()), nre("artist", bean.getArtistId()));

        // Verify result string is ok
        assertEquals("result string should be=" + expectedResult, expectedResult, handleSubmit);

        // Verify venue now existsById in db
        assertTrue("Venue should now be persisted", venue.isExistingEntity());

        // Verify artist-venue relations
        assertTrue("Artist has venue", artist.getVenues().contains(venue));
        assertTrue("Venue has artist", venue.getArtists().contains(artist));

        // Verify contact was persisted as well and has a venue that is the same we persisted
        assertTrue("Contact is now persisted", contact.isExistingEntity());
        assertTrue("Contact should have the venue", contact.getVenue().equals(venue));

        // Verify address was persisted as well and has a venue that is the same we persisted
        assertTrue("Address is now persisted", address.isExistingEntity());
        assertTrue("Address should have the venue", address.getVenue().equals(venue));
    }

    /*
     * Update an existing venue
     */
    @Test
    public void testHandleSubmitUpdate() {
        // Verify venue has references
        assertFalse("Venue should artists", venue.getArtists().isEmpty());
        assertFalse("Venue should reviews", venue.getReviews().isEmpty());
        assertFalse("Venue should contacts", venue.getContacts().isEmpty());

        Address address = venue.getAddress();

        // Verify address and contact belongs to venue
        assertNotNull("Address venue", address.getVenue());
        assertNotNull("Contact venue", contact.getVenue());

        // Alter some things
        venue.setName("Some Random Name");
        address.setCity("Some Random City");
        contact.setName("Random Dude");
        review.setReview("I am random review");

        // Create bean for post method
        EditVenueBean bean = new EditVenueBean(venue, member.getId(), artist.getId());

        // Call post method
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = MvcUtils.redirectTo(Page.EDIT_ARTIST_FW, nre("member", bean.getMemberId()), nre("artist", bean.getArtistId()));

        // Verify result string is ok
        assertEquals("result string should be=" + expectedResult, expectedResult, handleSubmit);

        // Fetch db venue that was updated
        Venue dbVenue = service.findOne(venue.getId());

        // Verify venue name changed
        assertEquals("Venue name should now be changed", "Some Random Name", dbVenue.getName());

        // Verify address city changed
        assertEquals("Address city should now be changed", "Some Random City", dbVenue.getAddress().getCity());

        // Verify contact name changed
        assertEquals("Contact person name should now be changed", "Random Dude", dbVenue.getContacts().get(0).getName());

        // Verify review text changed
        assertEquals("Review text should now be changed", "I am random review", dbVenue.getReviews().get(0).getReview());
    }

    @Test
    public void testHandleSubmitExistingEmail() {
        Venue venue2 = service.findAllById().get(1);
        venue.setEmail(venue2.getEmail());

        // Create bean for post method
        EditVenueBean bean = new EditVenueBean(venue, member.getId(), artist.getId());

        // Call post method
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = Page.EDIT_VENUE_FW;

        // Verify result string is ok
        assertEquals("result string should be=" + expectedResult, expectedResult, handleSubmit);

        assertTrue("Model should contain key=isExistingEmail", mockModel.containsKey(MvcErrors.EMAIL_EXISTS));
        assertTrue("Model should contain key=isExistingEmailMsg", mockModel.containsKey(MvcErrors.EMAIL_EXISTS_MSG_KEY));
    }

    @Override
    protected String getValidIndexUrl() {
        return MvcUtils.directTo(Page.EDIT_VENUE_FW, new RedirectEntity("venue", 1), new RedirectEntity("artist", 1), new RedirectEntity("member", 1));
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.EDIT_VENUE_FW;
    }

    @Override
    protected EditVenueController initController() {
        return new EditVenueController();
    }


}
