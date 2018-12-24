package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.EditMemberBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.web.MvcErrors;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditMemberControllerTest extends AbstractMemberControllerTest<EditMemberController> {

    @Before
    public void init() {
        super.init();
        this.service.save(TestFixture.getValidMembersWithoutReferences());
    }

    @Override
    public void testIndex() throws Exception {
        String url = MvcUtils.directTo(Page.EDIT_MEMBER_FW, service.findAllById().get(0));
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(getValidForwardIndexUrl()));
    }

    @Test
    public void testIndexAllGood() {
        Member member = service.findAllById().get(0);
        String index = controller.index(member.getId(), mockModel);
        String expectedResult = Page.EDIT_MEMBER_FW;
        assertEquals("index should equal " + expectedResult, expectedResult, index);
        assertTrue("Model should contain a bean", mockModel.containsKey("editMemberBean"));
    }

    @Override
    public void testHandleSubmitAllGood() {
        Member member = service.findAllById().get(0);
        member.setName("Eggman Calling");

        EditMemberBean bean = new EditMemberBean(member);
        bean.setCurrentPassword(member.getPassword());
        bean.setNewPassword("yolo123");
        bean.setResetPassword("yolo123");

        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = MvcUtils.redirectTo("index", member);

        Member updatedMember = service.findOne(member.getId());

        assertEquals("Updated member name should be=Eggman Calling", "Eggman Calling", updatedMember.getName());
        assertEquals("Updated member pw should be=yolo123", "yolo123", updatedMember.getPassword());
        assertEquals("return string should=" + expectedResult, expectedResult, handleSubmit);
    }

    @Test
    public void testHandleSubmitNoPasswordChange() {
        Member member = service.findAllById().get(0);
        member.setName("Eggman Calling");

        EditMemberBean bean = new EditMemberBean(member);

        String password = member.getPassword();
        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = MvcUtils.redirectTo("index", member);

        Member updatedMember = service.findOne(member.getId());

        assertEquals("return string should=" + expectedResult, expectedResult, handleSubmit);
        assertEquals("Updated member name should be=Eggman Calling", "Eggman Calling", updatedMember.getName());
        assertTrue("Password should be unchanged", password.equals(updatedMember.getPassword()));
    }

    @Test
    public void testHandleSubmitExistingUserNameAndEmail() {
        Member member = service.findAllById().get(0);
        Member existingMember = service.findAllById().get(1);

        member.setUserName(existingMember.getUserName());
        member.setEmail(existingMember.getEmail());

        EditMemberBean bean = new EditMemberBean(member);

        String handleSubmit = controller.handleSubmit(bean, mockModel, mockBindingResult);
        String expectedResult = Page.EDIT_MEMBER_FW;

        assertEquals("return string should=" + expectedResult, expectedResult, handleSubmit);

        assertTrue("Model should contain isExistingEmail key", mockModel.containsKey(MvcErrors.EMAIL_EXISTS));
        assertTrue("Model should contain isExistingEmailMsg key", mockModel.containsKey(MvcErrors.EMAIL_EXISTS_MSG_KEY));
        assertTrue("Model should contain isExistingUserName key", mockModel.containsKey(MvcErrors.USR_NAME_EXISTS));
        assertTrue("Model should contain isExistingUserNameMsg key", mockModel.containsKey(MvcErrors.USR_NAME_EXISTS_MSG_KEY));
    }

    @Override
    protected EditMemberController initController() {
        return new EditMemberController();
    }

    @Override
    protected String getValidIndexUrl() {
        return MvcUtils.directTo(Page.EDIT_MEMBER_FW, new RedirectEntity("member", 1));
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.EDIT_MEMBER_FW;
    }

}
