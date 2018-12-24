package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.EditVenueReviewsBean;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.VenueReviewService;
import com.github.johanfredin.springdataextensions.service.VenueService;
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

/**
 * Controller for the editReview.jsp page
 * This is the page you enter from the venue page.
 * First displays all the reviews written by others(if any)
 * And then display the artists.
 * <p>
 * If artist has yet not written a review we display an option to create one.
 * If review is already written we offer the option to edit or remove the review
 *
 * @author johan
 */
@Controller
@RequestMapping(Page.EDIT_VENUE_REVIEW)
public class EditVenueReviewsController extends ControllerBase<VenueReviewService> implements ControllerExtensions<VenueReview, EditVenueReviewsBean> {

    @Autowired
    private VenueService venueService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private VenueReviewService reviewService;

    @GetMapping
    public String index(@PathVariable long venueId, @PathVariable long artistId,
                        @PathVariable long memberId, @PathVariable long reviewId, Model model) {

        VenueReview review = null;

        if (isExistingEntity(reviewId)) {
            review = getPersistedEntity(reviewId);
        } else {
            review = new VenueReview();
        }

        populateModel(model, new EditVenueReviewsBean(review, memberId, artistId, venueId));
        return Page.EDIT_VENUE_REVIEW_FW;
    }

    @Override
    @PostMapping
    public String handleSubmit(@Valid EditVenueReviewsBean bean, Model model, BindingResult bindingResult) {

        long artistId = bean.getArtistId();
        long memberId = bean.getMemberId();
        long venueId = bean.getVenueId();

        if (bindingResult.hasErrors()) {
            // Go through regular validation errors
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info("Error " + error.getCode() + " " + error.getDefaultMessage() + " " + error.getObjectName());
            }
            populateModel(model, bean);
            return Page.EDIT_VENUE_REVIEW_FW;
        }


        // Now create/update the review
        VenueReview review = bean.getReview();
        if (review.isExistingEntity()) {
            getMainService().save(review, true);
            log.info("Review updated");
        } else {
            getMainService().save(review);
            log.info("Review created!");

            // Attach the review to the venue and update the venue
            Venue venue = getVenueService().getVenueWithChildren(venueId, false, false, true, false);
            venue.addReview(review);
            review.setVenue(venue);
            getVenueService().save(venue, true);
            log.info("Review added to venue");

            // Attach the review to the artist and update the artist
            Artist artist = getArtistService().getArtistWithChildren(artistId, true, false, false, false);
            artist.addReview(review);
            review.setArtist(artist);
            getArtistService().save(artist, true);
            log.info("Review added to artist");
        }

        model.addAttribute("memberId", memberId);
        model.addAttribute("venueId", venueId);
        model.addAttribute("artistId", artistId);
        return MvcUtils.redirectTo(Page.EDIT_VENUE_FW, nre("venue", venueId), nre("artist", artistId), nre("member", memberId));
    }

    @Override
    public VenueReviewService getMainService() {
        return this.reviewService;
    }

    @Override
    public void setMainService(VenueReviewService mainService) {
        this.reviewService = mainService;
    }

    @Override
    public VenueReview getPersistedEntity(long id) {
        return this.reviewService.getReviewWithArtistAndVenue(id);
    }

    public VenueService getVenueService() {
        return venueService;
    }

    public void setVenueService(VenueService venueService) {
        this.venueService = venueService;
    }

    public ArtistService getArtistService() {
        return artistService;
    }

    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public void populateModel(Model model, EditVenueReviewsBean bean) {
        Venue venue = getVenueService().getVenueWithChildren(bean.getVenueId(), true, false, true, false);

        model.addAttribute("editReviewBean", bean);
        model.addAttribute("reviews", venue.getReviews());
        model.addAttribute("review", bean.getReview());
        model.addAttribute("artistId", bean.getArtistId());
        model.addAttribute("memberId", bean.getMemberId());
        model.addAttribute("venueId", bean.getVenueId());
        model.addAttribute("venueName", venue.getName());
    }

}
