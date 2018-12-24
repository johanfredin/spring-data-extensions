package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.mvc.bean.AddMembersForArtistBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.service.MessageService;
import com.github.johanfredin.springdataextensions.web.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

import static com.github.johanfredin.springdataextensions.web.MvcErrors.*;

@Controller
@RequestMapping(Page.ADD_MEMBERS_TO_ARTIST)
public class AddMembersToArtistController extends ControllerBase<MemberService> implements ControllerExtensions<Member, AddMembersForArtistBean> {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private MessageService messageService;

    @GetMapping
    public String index(@Valid AddMembersForArtistBean bean, @PathVariable long artistId, @PathVariable long memberId, @PathVariable long inviteId, Model model) {

        if (!bean.isAlive()) {
            bean = new AddMembersForArtistBean(getArtistService().findOne(artistId), memberId, inviteId);
        }

        populateModel(model, bean);

        return Page.ADD_MEMBERS_TO_ARTIST_FW;
    }

    @Override
    @PostMapping
    public String handleSubmit(@Valid AddMembersForArtistBean bean, Model model, BindingResult bindingResult) {

        String param = bean.getUserNameOrEmail();    // Get the search params

        if (param.isEmpty()) {
            model.addAttribute(FIND_USR_PARAM_EMPTY, true).addAttribute(FIND_USR_PARAM_EMPTY_MSG_KEY, FIND_USR_PARAM_EMPTY_MSG_VALUE);
            return Page.ADD_MEMBERS_TO_ARTIST_FW;
        }

        List<Member> results = null;
        if (bean.getResults() != null) {
            results = bean.getResults();
        } else {
            results = getMainService().findAllByUserNameOrEmail(param);    // Get the results
        }

        // If there are NOT empty we should move ahead
        if (!results.isEmpty()) {

            long inviteId = bean.getInviteId();
            // If inviteId > 0 it means we invited a member
            if (inviteId > 0) {
                Member memberToInvite = bean.getMember(inviteId);                                    // Fetch the member we want to invite to our artist
                Artist artistToInvite = getArtistService().findOne(bean.getArtist().getId());        // Fetch the artist we are going to give to that member
                Member invitingMember = getMainService().findOne(bean.getMemberId());                // Fetch the member to give the invitation (us)
                invitingMember.grantArtistAccess(artistToInvite, memberToInvite);                    // Add the artist to that member

                Message message = new Message("Hey I just added you to my band " + bean.getArtist().getName(), MessageSubject.OFFER_MEMBERSHIP, invitingMember, memberToInvite);    // Also create a new message letting the member know he/she was invited to our artist
                getMessageService().save(message);    // Persist the message

                invitingMember.sendMessage(message, invitingMember, memberToInvite);    // Send the message

                // Update all parts involved
                getMainService().save(invitingMember, true);
                getMainService().save(memberToInvite, true);
                getArtistService().save(artistToInvite, true);

                // Set the id back to 0 so we don't end up here again
                inviteId = 0;
            }

            // Give the results to the bean
            bean.setResults(results);
        } else {
            // There was no results
            model.addAttribute(FIND_USR_NO_RESULTS, true);
            model.addAttribute(FIND_USR_NO_RESULTS_MSG_KEY, FIND_USR_NO_RESULTS_MSG_VAL);
        }

        // Reload page
        populateModel(model, bean);

        return Page.ADD_MEMBERS_TO_ARTIST_FW;
    }

    @Override
    public MemberService getMainService() {
        return this.memberService;
    }

    @Override
    public void setMainService(MemberService mainService) {
        this.memberService = mainService;
    }

    public ArtistService getArtistService() {
        return artistService;
    }

    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Member getPersistedEntity(long id) {
        return getMainService().getMemberWithChildren(id, true, false);
    }

    @Override
    public void populateModel(Model model, AddMembersForArtistBean bean) {
        model.addAttribute("addMembersToArtistBean", bean);
        model.addAttribute("artistId", bean.getArtist().getId());
        model.addAttribute("memberId", bean.getMemberId());
        if (bean.getResults() != null) {
            model.addAttribute("results", bean.getResults());
        }
    }

}
