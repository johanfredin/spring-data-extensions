package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.EditArtistBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.web.MvcErrors;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
@DataJpaTest
public class EditArtistControllerTest extends AbstractArtistControllerTest<EditArtistController> {

    @Before
    public void init() {
        super.init();
        this.controller.setMemberService(memberService);
        this.memberService.save(this.member);
    }

    @After
    public void drop() {
        this.memberRepository.deleteAll();
        this.artistRepository.deleteAll();
        this.testEntities.clear();
    }

    @Override
    public void testIndex() throws Exception {
        mockMvc.perform(get("/editArtist/member=1/artist=1"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(Page.EDIT_ARTIST_FW));
    }

    @Test
    public void testIndexNewArtist() {
        String index = controller.index(getMemberRepository().getOne(member.getId()).getId(), 0, mockModel);
        EditArtistBean bean = (EditArtistBean) mockModel.get("editArtistBean");
        assertFalse("Artist should not be an existing artist", bean.getEntity().isExistingEntity());
        assertEquals("Return string should equal " + Page.EDIT_ARTIST_FW, Page.EDIT_ARTIST_FW, index);
    }

    @Test
    public void testIndexExistingArtist() {
        persistData();

        String index = controller.index(member.getId(), member.getArtists().get(0).getId(), mockModel);
        EditArtistBean bean = (EditArtistBean) mockModel.get("editArtistBean");
        assertTrue("Artist should be an existing artist", bean.getEntity().isExistingEntity());
        assertEquals("Return string should equal " + Page.EDIT_ARTIST_FW, Page.EDIT_ARTIST_FW, index);
    }

    // Create an artist and make sure all good
    @Override
    public void testHandleSubmitAllGood() {
        Artist validArtist = TestFixture.getValidArtist("Fake", "Fake", "Fake");

        // We want to assure this id is unchanged since we simply update the member
        long memberIdBeforeUpdate = member.getId();

        // Member should not have the artist yet!
        assertNull("Member should have no artists yet", member.getArtists());

        // Artist should have no members
        assertNull("Artist should have no members yet", validArtist.getMembers());

        EditArtistBean bean = new EditArtistBean(validArtist, member.getId());

        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedReturnValue = MvcUtils.redirectTo("index", new RedirectEntity("member", member.getId()));
        assertTrue("Expected returnValue should be=" + expectedReturnValue, expectedReturnValue.equals(handleSubmit));

        assertTrue("Artist should now be persisted", validArtist.isExistingEntity());
        assertTrue("Member should now be connected to artist", member.getArtists().contains(validArtist));
        assertTrue("Artist should now belong to the member", validArtist.getMembers().contains(member));

        // Make sure member id is not altered
        assertTrue("Members identifier should not have changed", member.getId() == memberIdBeforeUpdate);
    }

    // Update an artist and make sure all good
    @Test
    public void testHandleSubmitAllGoodUpdate() {
        Artist validArtist = getTestEntities().get(0);
        Member validMember = TestFixture.getValidMemberWithoutReferences("Fake", "Fake", "Fake");
        validMember.setArtists(getInitialList(validArtist));
        validArtist.setMembers(getInitialMembers(validMember));

        getMemberRepository().save(validMember);
        service.save(validArtist);

        // Ensure entities are persisted in DB
        assertTrue("Member is persisted", validMember.isExistingEntity());
        assertTrue("Artist is persisted", validArtist.isExistingEntity());

        // We want to assure this id is unchanged since we simply update the member
        long memberIdBeforeUpdate = validMember.getId();

        // Change properties in artist
        validArtist.setName("New Kids On The Block!");
        validArtist.setEmail("yolo@email.com");

        long artistIdBeforeMerge = validArtist.getId();

        // Member should be connected to artist
        assertTrue("Member should be connected to artist", validMember.getArtists().contains(validArtist));

        // Artist should be connected to member
        assertTrue("Artist should be connected to member", validArtist.getMembers().contains(validMember));

        EditArtistBean bean = new EditArtistBean(validArtist, validMember.getId());

        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);

        // Reload the artist from DB (never required LIVE but since we are running against a mocked repo we need to do it here
        Artist persistedArtist = service.findOne(validArtist.getId());

        // Now verify the persisted artist was merged
        assertTrue("Persisted name should now equal new name", validArtist.getName().equals(persistedArtist.getName()));
        assertTrue("Persisted email should now equal new email", validArtist.getEmail().equals(persistedArtist.getEmail()));

        String expectedReturnValue = MvcUtils.redirectTo("index", validMember);
        assertEquals("Expected returnValue should be=" + expectedReturnValue, expectedReturnValue, handleSubmit);
        assertTrue("Members identifier should not have changed", validMember.getId() == memberIdBeforeUpdate);
        assertTrue("Artists identifier should not have changed", validArtist.getId() == artistIdBeforeMerge);
    }

    @Test
    public void testHandleSubmitNonUniqueEmail() {
        persistData();

        Artist a = TestFixture.getValidArtist("FakeName", "FakeCity", "FakeCountry");
        Artist persistedArtist = service.findAllById().get(0);
        a.setEmail(persistedArtist.getEmail());

        EditArtistBean bean = new EditArtistBean(a, member.getId());

        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);

        assertTrue("Mockmodel should contain isExistingEmail boolan", mockModel.containsKey(MvcErrors.EMAIL_EXISTS));
        assertTrue("Mockmodel should contain isExistingEmailMsg message", mockModel.containsKey(MvcErrors.EMAIL_EXISTS_MSG_KEY));
        assertEquals("Return string should equal " + Page.EDIT_ARTIST_FW, Page.EDIT_ARTIST_FW, handleSubmit);
    }

    @Override
    protected EditArtistController initController() {
        return new EditArtistController();
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.EDIT_ARTIST_FW;
    }

    @Override
    protected String getValidIndexUrl() {
        return MvcUtils.redirectTo(Page.EDIT_ARTIST_FW, new RedirectEntity("member", 1), new RedirectEntity("artist", 1));
    }

    private void persistData() {
        this.service.save(getTestEntities());
        this.member.setArtists(this.service.findAllById());

        for (Artist a : service.findAllById()) {
            a.setMembers(new ArrayList<>());
        }
    }


}
