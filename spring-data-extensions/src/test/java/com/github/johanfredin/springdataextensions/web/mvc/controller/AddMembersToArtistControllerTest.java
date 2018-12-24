package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.web.mvc.bean.AddMembersForArtistBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.MessageService;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
@ComponentScan
public class AddMembersToArtistControllerTest extends AbstractMemberControllerTest<AddMembersToArtistController> {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private MessageService messageService;

    private Member invitingMember;
    private Member invitedMember;

    private Artist artistToGrant;

    private Message message;

    private List<Member> inviteList;

    @Override
    @Before
    public void init() {
        super.init();

        // Give the services to the controller
        this.controller.setArtistService(this.artistService);
        this.controller.setMessageService(this.messageService);

        // Persist the inviting and invited members
        this.invitingMember = this.service.save(TestFixture.getValidMemberWithoutReferences("zolost", "lizardKING", "Johan"));
        this.invitedMember = this.service.save(TestFixture.getValidMemberWithoutReferences("taikesBass", "hetgurka", "Alex"));

        // Give empty message collections to the sending and receiving member
        this.invitingMember.setSentMessages(new ArrayList<>());
        this.invitingMember.setReceivedMessages(new ArrayList<>());
        this.invitedMember.setSentMessages(new ArrayList<>());
        this.invitedMember.setReceivedMessages(new ArrayList<>());

        // Init the artists of the member
        this.invitingMember.setArtists(new ArrayList<>());
        this.invitedMember.setArtists(new ArrayList<>());

        // Init the artist property
        this.artistToGrant = TestFixture.getValidArtist("Taikes", "Mölndal", "Sweden");
        this.artistToGrant.setMembers(new ArrayList<>());

        // Give it to the inviting member
        this.invitingMember.addArtist(artistToGrant);

        // Persist the artist
        this.artistService.save(artistToGrant);

        // Update the inviting member
        this.service.save(invitingMember, true);

        // Init the invite list and give it the invited member
        this.inviteList = new ArrayList<>();
        this.inviteList.add(this.invitedMember);
    }

    @Test
    public void testIndex() throws Exception {
        String url = MvcUtils.directTo(Page.ADD_MEMBERS_TO_ARTIST_FW,
                nre("member", invitingMember.getId()),
                nre("artist", artistToGrant.getId()),
                nre("invite", invitedMember.getId()));

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(getValidForwardIndexUrl()));
    }

    @Test
    public void verifyInit() {
        assertTrue("Inviting member should be persisted", this.invitingMember.isExistingEntity());
        assertTrue("Invited member should be persisted", this.invitedMember.isExistingEntity());
        assertTrue("The artist to grant should be persisted", this.artistToGrant.isExistingEntity());
        assertTrue("Inviting member should have the artist to grant", this.invitingMember.getArtists().contains(this.artistToGrant));
        assertNull("Message should not yet be initialized", this.message);
        assertTrue("Message collections should be initiaded",
                invitingMember.getSentMessages() != null && invitingMember.getReceivedMessages() != null &&
                        invitedMember.getSentMessages() != null && invitedMember.getReceivedMessages() != null);
    }

    @Test
    public void testIndexFirstEntry() {
        // The first time we enter this page we should have no results

        String index = controller.index(new AddMembersForArtistBean(artistToGrant, invitingMember.getId(), invitedMember.getId()), artistToGrant.getId(), invitingMember.getId(), invitedMember.getId(), mockModel);
        assertEquals("Index return value should be=" + Page.ADD_MEMBERS_TO_ARTIST_FW, Page.ADD_MEMBERS_TO_ARTIST_FW, index);

        long artistId = (Long) mockModel.get("artistId");
        long memberId = (Long) mockModel.get("memberId");
        AddMembersForArtistBean bean = (AddMembersForArtistBean) mockModel.get("addMembersToArtistBean");

        assertNotNull("Bean should have been instantiated", bean);
        assertNull("Bean should have no results in members", bean.getResults());
        assertTrue("Artist id=artistToGrant.getId()", artistId == artistToGrant.getId());
        assertTrue("Member id=invitingMember.getid()", memberId == invitingMember.getId());
    }

    @Test
    public void testIndexRefreshed() {
        // The second time we enter index we should have some data, not so easy to test but a few things we can check...
        AddMembersForArtistBean bean = new AddMembersForArtistBean(invitedMember.getEmail(), inviteList, artistToGrant, invitingMember.getId(), invitedMember.getId());
        String index = controller.index(bean, artistToGrant.getId(), bean.getMemberId(), bean.getInviteId(), mockModel);
        assertEquals("Index return value should be=" + Page.ADD_MEMBERS_TO_ARTIST_FW, Page.ADD_MEMBERS_TO_ARTIST_FW, index);

        long artistId = (Long) mockModel.get("artistId");
        long memberId = (Long) mockModel.get("memberId");
        AddMembersForArtistBean resultBean = (AddMembersForArtistBean) mockModel.get("addMembersToArtistBean");

        assertNotNull("Bean should have been instantiated", resultBean);
        assertNotNull("Bean should have results in members", resultBean.getResults());
        assertTrue("Artist id=artistToGrant.getId()", artistId == artistToGrant.getId());
        assertTrue("Member id=invitingMember.getid()", memberId == invitingMember.getId());
    }

    @Override
    public void testHandleSubmitAllGood() {
        // First verify that there is no message in db and that the sender and receiver has no messages
        assertTrue("There should be no messages in db", messageService.findAllById().isEmpty());
        assertTrue("Inviting member should have no sent messages", invitingMember.getSentMessages().isEmpty());
        assertTrue("Invited member should have no received messages", invitedMember.getReceivedMessages().isEmpty());
        assertFalse("Invited member should not have access to the artist yet", invitedMember.getArtists().contains(artistToGrant));

        // The second time we enter index we should have some data, not so easy to test but a few things we can check...
        AddMembersForArtistBean bean = new AddMembersForArtistBean(invitedMember.getEmail(), inviteList, artistToGrant, invitingMember.getId(), invitedMember.getId());
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);

        // Verify the return string is correct
        String expectedResult = Page.ADD_MEMBERS_TO_ARTIST_FW;
        assertTrue("Expected result should=" + expectedResult, handleSubmit.equals(expectedResult));

        // Verify a message was persisted
        assertTrue("There should now be a message in db", messageService.findAllById().size() == 1);

        // Verify that the inviting member has a sent message
        assertTrue("Inviting member should now have a sent message", invitingMember.getSentMessages().size() == 1);

        // Verify that the invited member has a received message
        assertTrue("Invited member should now have a received message", invitedMember.getReceivedMessages().size() == 1);

        // Verify the message
        Message message = messageService.findAllById().get(0);
        assertTrue("Message should have sender id match inviting member", message.getSender().equals(invitingMember));
        assertTrue("Message should have the receiver match the invited member", message.getReceiver().equals(invitedMember));
        assertEquals("Subject should be=" + MessageSubject.OFFER_MEMBERSHIP, MessageSubject.OFFER_MEMBERSHIP, message.getSubject());

        // Verify it is the same message that the sender and receiver have
        assertTrue("Message should match senders sent message", message.getId() == invitingMember.getSentMessages().get(0).getId());
        assertTrue("Message should match receivers received message", message.getId() == invitedMember.getReceivedMessages().get(0).getId());

        // Finally verify that the invited member now has the artist
        assertTrue("Invited member should now have access to the artist", invitedMember.getArtists().contains(artistToGrant));
    }

    @Test
    public void testHandleSubmitAllGoodUserNameParam() {
        // First verify that there is no message in db and that the sender and receiver has no messages
        assertTrue("There should be no messages in db", messageService.findAllById().isEmpty());
        assertTrue("Inviting member should have no sent messages", invitingMember.getSentMessages().isEmpty());
        assertTrue("Invited member should have no received messages", invitedMember.getReceivedMessages().isEmpty());
        assertFalse("Invited member should not have access to the artist yet", invitedMember.getArtists().contains(artistToGrant));

        // The second time we enter index we should have some data, not so easy to test but a few things we can check...
        AddMembersForArtistBean bean = new AddMembersForArtistBean(invitedMember.getUserName(), inviteList, artistToGrant, invitingMember.getId(), invitedMember.getId());
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);

        // Verify the return string is correct
        String expectedResult = Page.ADD_MEMBERS_TO_ARTIST_FW;
        assertTrue("Expected result should=" + expectedResult, handleSubmit.equals(expectedResult));

        // Verify a message was persisted
        assertTrue("There should now be a message in db", messageService.findAllById().size() == 1);

        // Verify that the inviting member has a sent message
        assertTrue("Inviting member should now have a sent message", invitingMember.getSentMessages().size() == 1);

        // Verify that the invited member has a received message
        assertTrue("Invited member should now have a received message", invitedMember.getReceivedMessages().size() == 1);

        // Verify the message
        Message message = messageService.findAllById().get(0);
        assertTrue("Message should have sender id match inviting member", message.getSender().equals(invitingMember));
        assertTrue("Message should have the receiver match the invited member", message.getReceiver().equals(invitedMember));
        assertEquals("Subject should be=" + MessageSubject.OFFER_MEMBERSHIP, MessageSubject.OFFER_MEMBERSHIP, message.getSubject());

        // Verify it is the same message that the sender and receiver have
        assertTrue("Message should match senders sent message", message.getId() == invitingMember.getSentMessages().get(0).getId());
        assertTrue("Message should match receivers received message", message.getId() == invitedMember.getReceivedMessages().get(0).getId());

        // Finally verify that the invited member now has the artist
        assertTrue("Invited member should now have access to the artist", invitedMember.getArtists().contains(artistToGrant));
    }


    @Test
    public void testHandleSubmitEmptyParam() {
        // First verify that there is no message in db
        assertTrue("There should be no messages in db", messageService.findAllById().isEmpty());

        AddMembersForArtistBean bean = new AddMembersForArtistBean("", inviteList, artistToGrant, invitingMember.getId(), invitedMember.getId());
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = Page.ADD_MEMBERS_TO_ARTIST_FW;
        assertTrue("Expected result should=" + expectedResult, handleSubmit.equals(expectedResult));


        // Verify the model contains the empty attributes
        assertTrue("Model should contain emptyParam key", mockModel.containsKey("emptyParam"));
        assertTrue("Model should contain emptyParamMsg", mockModel.containsKey("emptyParamMsg"));

        // First verify that there are still no message in db
        assertTrue("There should still be no messages in db", messageService.findAllById().isEmpty());
    }

    @Test
    public void testHandleSubmitNoResults() {
        // First verify that there is no message in db
        assertTrue("There should be no messages in db", messageService.findAllById().isEmpty());

        String nonExistingUsername = "NonExisting";
        assertFalse("Member user name is not equal to the non existing user name", invitedMember.getUserName().equals(nonExistingUsername));

        // Pass in a null arraylist with no results. That way we will force the service to find one in db
        AddMembersForArtistBean bean = new AddMembersForArtistBean(nonExistingUsername, null, artistToGrant, invitingMember.getId(), invitedMember.getId());
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = Page.ADD_MEMBERS_TO_ARTIST_FW;
        assertTrue("Expected result should=" + expectedResult, handleSubmit.equals(expectedResult));


        // Verify the model contains the empty attributes
        assertTrue("Model should contain noResults key", mockModel.containsKey("noResults"));
        assertTrue("Model should contain noResultsMsg key", mockModel.containsKey("noResultsMsg"));

        // First verify that there are still no message in db
        assertTrue("There should still be no messages in db", messageService.findAllById().isEmpty());
    }

    @Override
    protected AddMembersToArtistController initController() {
        return new AddMembersToArtistController();
    }

    @Override
    protected String getValidIndexUrl() {
        return "/addMembersToArtist/member=1/artist=1/invite=1";
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.ADD_MEMBERS_TO_ARTIST_FW;
    }

}
