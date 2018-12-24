package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.EditVenueBean;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.service.VenueReviewService;
import com.github.johanfredin.springdataextensions.service.VenueService;
import com.github.johanfredin.springdataextensions.util.comparator.DateComparator;
import com.github.johanfredin.springdataextensions.web.MvcErrors;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(Page.EDIT_VENUE)
public class EditVenueController extends ControllerBase<VenueService> implements ControllerExtensions<Venue, EditVenueBean> {

    @Autowired
    private VenueService venueService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private VenueReviewService reviewService;

    @Autowired
    private DateComparator dateComparator;

    @GetMapping
    public String index(@PathVariable long venueId, @PathVariable long artistId, @PathVariable long memberId, Model model) {
        Venue venue = null;
        if (isExistingEntity(venueId)) {
            venue = getPersistedEntity(venueId);
        } else {
            venue = new Venue();
        }

        populateModel(model, new EditVenueBean(venue, memberId, artistId, this.dateComparator));
        return Page.EDIT_VENUE_FW;
    }

    @Override
    @PostMapping
    public String handleSubmit(@Valid EditVenueBean bean, Model model, BindingResult bindingResult) {

        Venue venue = bean.getPopulatedEntityFromBean();

        boolean isUniqueEmail = getMainService().isNoOtherEntityWithEmail(venue.getId(), venue.getEmail());

        if (MvcUtils.isAnyErrors(isUniqueEmail, !bindingResult.hasErrors())) {

            if (venue.isExistingEntity()) {
                Venue dbVenue = getPersistedEntity(venue.getId());
                venue.copyReferencesFromEntity(dbVenue);
            }

            if (!isUniqueEmail) {
                log.info("Eror: already another venue with that email");
                model.addAttribute(MvcErrors.EMAIL_EXISTS, true);
                model.addAttribute(MvcErrors.EMAIL_EXISTS_MSG_KEY, MvcErrors.getEmailMessage(venue));
            }
            if (bindingResult.hasErrors()) {
                // Go through regular validation errors
                for (ObjectError error : bindingResult.getAllErrors()) {
                    log.info("Error " + error.getCode() + " " + error.getDefaultMessage() + " " + error.getObjectName());
                }
            }

            return Page.EDIT_VENUE_FW;
        }

        // All good time to update/persist db
        venue.setRelations();

        if (venue.isExistingEntity()) {
            Venue dbVenue = getPersistedEntity(venue.getId());
            dbVenue.copyDataFromEntity(venue);
            getMainService().save(venue, true);
            log.info("Venue=" + venue.getId() + " updated!");
        } else {
            // Set back references once creation/update is done
            Artist dbArtist = getArtistService().getArtistWithChildren(bean.getArtistId(), false, false, false, true);

            venue.addArtist(dbArtist);
            getMainService().save(venue);
            log.info("Venue=" + venue.getId() + " created!");

            dbArtist.addVenue(venue);
            getArtistService().save(dbArtist, true);
        }

        return MvcUtils.redirectTo(Page.EDIT_ARTIST_FW, nre("member", bean.getMemberId()), nre("artist", bean.getArtistId()));
    }

    @Override
    public VenueService getMainService() {
        return this.venueService;
    }

    @Override
    public void setMainService(VenueService mainService) {
        this.venueService = mainService;
    }

    @Override
    public Venue getPersistedEntity(long id) {
        return getMainService().getVenueWithAllChildren(id);
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public ArtistService getArtistService() {
        return artistService;
    }

    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    public VenueReviewService getReviewService() {
        return reviewService;
    }

    public void setReviewService(VenueReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public DateComparator getDateComparator() {
        return dateComparator;
    }

    public void setDateComparator(DateComparator dateComparator) {
        this.dateComparator = dateComparator;
    }

    @Override
    public void populateModel(Model model, EditVenueBean bean) {
        model.addAttribute("editVenueBean", bean);

        // Sort the reviews
        bean.sort(bean.getReviews());

        model.addAttribute("venue", bean.getVenue());
        model.addAttribute("address", bean.getVenue().getAddress());
        model.addAttribute("reviews", bean.getVenue().getReviews());
        model.addAttribute("artists", bean.getVenue().getArtists());
        model.addAttribute("contacts", bean.getVenue().getContacts());

        model.addAttribute("artistId", bean.getArtistId());
        model.addAttribute("memberid", bean.getMemberId());

        // We need to know if the artist has written a review for the venue or not!
        long reviewId = 0L;

        // First check if venue is persisted
        if (isExistingEntity(bean.getVenue().getId())) {
            Artist artist = getArtistService().getArtistWithChildren(bean.getArtistId(), true, false, false, false);
            // Fetch the artist and check if the artist has written any reviews at all
            if (artist.getReviews() != null && !artist.getReviews().isEmpty()) {
                // If so check if artist has written a review for this venue
                reviewId = getReviewService().findOneByArtistAndVenue(artist, bean.getVenue()).getId();
            }
        }
        // Add reviewId to model
        model.addAttribute("reviewId", reviewId);
    }

}
