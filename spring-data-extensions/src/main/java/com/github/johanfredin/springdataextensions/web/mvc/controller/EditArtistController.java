package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.EditArtistBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.MemberService;
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
import java.util.ArrayList;
import java.util.List;

import static com.github.johanfredin.springdataextensions.web.MvcErrors.*;

@Controller
@RequestMapping(Page.EDIT_ARTIST)
public class EditArtistController extends ControllerBase<ArtistService> implements ControllerExtensions<Artist, EditArtistBean> {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String index(@PathVariable long memberId, @PathVariable long artistId, Model model) {

        Artist artist = null;
        if (artistId > 0) {
            artist = getPersistedEntity(artistId);
        } else {
            artist = new Artist();
        }

        populateModel(model, new EditArtistBean(artist, memberId));
        return Page.EDIT_ARTIST_FW;
    }

    @Override
    @PostMapping
    public String handleSubmit(@Valid EditArtistBean bean, Model model, BindingResult bindingResult) {

        Artist artist = bean.getEntity();

        // First check standard validation, not empty etc...
        if (bindingResult.hasErrors()) {
            // Go through regular validation errors
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info("Error " + error.getCode() + " " + error.getDefaultMessage() + " " + error.getObjectName());
            }
            return Page.EDIT_ARTIST_FW;
        }

        // Check if there are any non unique fields that need to be unique
        if (!getMainService().isEmailUnique(artist.getId(), artist.getEmail())) {
            model.addAttribute(EMAIL_EXISTS, true).addAttribute(EMAIL_EXISTS_MSG_KEY, getEmailMessage(artist));
            return Page.EDIT_ARTIST_FW;
        }

        Member member = null;
        long memberId = bean.getMemberId();

        // No errors after this
        if (artist.isExistingEntity()) {
            getMainService().save(artist, true);
        } else {

            // If we are creating a new artist we need to tell it what member it belongs to
            getMainService().save(artist);
            member = getMemberService().findOne(memberId);

            if (member.getArtists() != null) {
                member.addArtist(artist);
            } else {
                List<Artist> artists = new ArrayList<>();
                artists.add(artist);
                member.setArtists(artists);
            }

            List<Member> membersOfArtist = new ArrayList<>();
            membersOfArtist.add(member);
            artist.setMembers(membersOfArtist);
            getMemberService().save(member, true);
        }

        return MvcUtils.redirectTo("index", nre("member", memberId));
    }

    @Override
    public ArtistService getMainService() {
        return artistService;
    }

    @Override
    public void setMainService(ArtistService artistService) {
        this.artistService = artistService;
    }

    @Override
    public Artist getPersistedEntity(long id) {
        return getMainService().getArtistWithAllChildren(id);
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void populateModel(Model model, EditArtistBean bean) {
        model.addAttribute("editArtistBean", bean);
    }


}
