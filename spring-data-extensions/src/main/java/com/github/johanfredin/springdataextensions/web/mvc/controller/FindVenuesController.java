package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.FindVenuesBean;
import com.github.johanfredin.springdataextensions.service.VenueSearchService;
import com.github.johanfredin.springdataextensions.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for finding venues. This controller does not extend {@link ControllerBase}
 * since service is custom
 *
 * @author johan
 */
@Controller
@RequestMapping(Page.FINDE_VENUES)
public class FindVenuesController {

    @Autowired
    private VenueSearchService venueSearchService;

    @GetMapping
    public String index(@Valid FindVenuesBean bean, @PathVariable long artistId, @PathVariable long memberId, Model model) {
        if (!bean.isAlive()) {
            bean = new FindVenuesBean();
        }

        populateModel(model, bean, artistId, memberId);
        return Page.FIND_VENUES_FW;
    }

    @PostMapping
    public String handleSubmit(FindVenuesBean bean, @PathVariable long artistId, @PathVariable long memberId, Model model) {
        // Get the search params
        String name = bean.getName().isEmpty() ? null : bean.getName();
        String city = bean.getCity().isEmpty() ? null : bean.getCity();
        String country = bean.getCountry().isEmpty() ? null : bean.getCity();

        // TODO: Fix this ugly logic
        Genre genre = bean.getGenre() == Genre.AMBIENCE ? null : bean.getGenre();

        // Get the results
        List<Venue> results = getVenueSearchService().getVenuesMatchingSearchParams(name, city, country, genre);

        // Give the results to the bean
        bean.setResults(results);

        // Reload page
        populateModel(model, bean, artistId, memberId);
        return Page.FIND_VENUES_FW;
    }

    public VenueSearchService getVenueSearchService() {
        return venueSearchService;
    }

    public void setVenueSearchService(VenueSearchService venueSearchService) {
        this.venueSearchService = venueSearchService;
    }

    private void populateModel(Model model, FindVenuesBean bean, long artistId, long memberId) {
        model.addAttribute("findVenusBean", bean);
        model.addAttribute("artistId", artistId);
        model.addAttribute("memberId", memberId);
        if (bean != null) {
            model.addAttribute("results", bean.getResults());
        }
    }

}
