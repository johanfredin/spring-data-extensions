package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.RegisterMemberBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.web.MvcErrors;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.github.johanfredin.springdataextensions.web.MvcUtils.redirectTo;
import static org.junit.Assert.*;

/**
 * @author johan
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
@ComponentScan
public class RegisterMemberControllerTest extends AbstractMemberControllerTest<RegisterMemberController> {

    @Override
    public void testHandleSubmitAllGood() {
        Member member = initTestEntities().get(0);

        // Verify that the member does not exist
        assertNull("Member should not exist in db", service.findOne(member.getId()));

        RegisterMemberBean registerMemberBean = new RegisterMemberBean();
        registerMemberBean.setComparePassword(member.getPassword());
        registerMemberBean.setMember(member);

        String handleSubmit = controller.handleSubmit(registerMemberBean, mockModel, mockBindingResult);
        String expectedResultUrl = redirectTo("index", new RedirectEntity("member", member.getId()));

        // Ensure redirect URL is correct
        assertEquals("Redirect URL from handleSubmit when all is good should be=" + expectedResultUrl, expectedResultUrl, handleSubmit);

        // Ensure member was persisted in DB
        assertNotNull("Member should not exist in db", service.findOne(member.getId()));
    }

    @Test
    public void testHandleSubmitRepeatPasswordIncorrect() {
        Member newMember = initTestEntities().get(1);

        // ensure the repeat password fails
        RegisterMemberBean registerMemberBean = new RegisterMemberBean(newMember, "fail");

        String handleSubmit = controller.handleSubmit(registerMemberBean, mockModel, mockBindingResult);
        String expectedResultUrl = Page.REGISTER_MEMBER_FW;

        // Verify our mock model contains the errors we expect it to have
        assertTrue("Mock model should contain key=" + MvcErrors.NEW_PW_INCORRECT, mockModel.containsKey(MvcErrors.NEW_PW_INCORRECT));
        assertTrue("Mock model should contain key=" + MvcErrors.NEW_PW_INCORRECT_MSG_KEY, mockModel.containsKey(MvcErrors.NEW_PW_INCORRECT_MSG_KEY));
        assertEquals("Redirect URL from handleSubmit when all is good should be=" + expectedResultUrl, expectedResultUrl, handleSubmit);
    }

    @Test
    public void testHandleSubmitExistingUserOrEmail() {
        Member dbMember = service.save(initTestEntities().get(0));

        Member newMember = initTestEntities().get(1);

        RegisterMemberBean registerMemberBean = new RegisterMemberBean(newMember, newMember.getPassword());     // ensure the repeat password don't fail

        newMember.setEmail(dbMember.getEmail());            // ensure email unique fails
        newMember.setUserName(dbMember.getUserName());        // ensure username unique fails

        String handleSubmit = controller.handleSubmit(registerMemberBean, mockModel, mockBindingResult);
        String expectedResultUrl = Page.REGISTER_MEMBER_FW;

        assertTrue("user name should already exist and an error message should be added", mockModel.get(MvcErrors.USR_NAME_EXISTS).equals(true) && mockModel.containsKey(MvcErrors.USR_NAME_EXISTS_MSG_KEY));
        assertTrue("email should already exist and an error message should be added", mockModel.get(MvcErrors.EMAIL_EXISTS).equals(true) && mockModel.containsKey(MvcErrors.EMAIL_EXISTS_MSG_KEY));
        assertEquals("Redirect URL from handleSubmit when all is good should be=" + expectedResultUrl, expectedResultUrl, handleSubmit);
    }

    @Override
    protected RegisterMemberController initController() {
        return new RegisterMemberController();
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.REGISTER_MEMBER_FW;
    }

    @Override
    protected String getValidIndexUrl() {
        return Page.REGISTER_MEMBER;
    }


}
