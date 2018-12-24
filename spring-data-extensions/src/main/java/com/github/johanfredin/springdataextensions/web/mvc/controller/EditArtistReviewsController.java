package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.EditArtistReviewsBean;
import com.github.johanfredin.springdataextensions.service.ArtistService;
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
@RequestMapping(Page.EDIT_ARTIST_REVIEWS)
public class EditArtistReviewsController extends ControllerBase<ArtistService> implements ControllerExtensions<Artist, EditArtistReviewsBean> {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    public String index(@PathVariable long artistId, @PathVariable long memberId, Model model) {
        Artist artist = getPersistedEntity(artistId);
        populateModel(model, new EditArtistReviewsBean(memberId, artist.getId(), artist.getReviews()));
        return Page.EDIT_ARTIST_REVIEWS_FW;
    }

    //TODO: Finish this controller
    @Override
    @PostMapping
    public String handleSubmit(@Valid EditArtistReviewsBean bean, Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Go through regular validation errors
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info("Error " + error.getCode() + " " + error.getDefaultMessage() + " " + error.getObjectName());
            }
            populateModel(model, bean);
            return Page.EDIT_ARTIST_REVIEWS_FW;
        }

        bean.getReviews();
        return MvcUtils.redirectTo(Page.EDIT_ARTIST_FW, nre("artist", bean.getArtistId()), nre("member", bean.getMemberId()));

    }

    @Override
    public ArtistService getMainService() {
        return artistService;
    }

    @Override
    public void setMainService(ArtistService mainService) {
        this.artistService = mainService;
    }

    @Override
    public Artist getPersistedEntity(long id) {
        return getMainService().getArtistWithChildren(id, true, false, false, true);
    }

    @Override
    public void populateModel(Model model, EditArtistReviewsBean bean) {
        model.addAttribute("editArtistReviewsBean", bean);
        model.addAttribute("artist", bean.getReviews());
    }


}
