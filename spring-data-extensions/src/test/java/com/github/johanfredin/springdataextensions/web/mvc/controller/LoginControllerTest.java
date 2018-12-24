package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.LoginMemberBean;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
public class LoginControllerTest extends AbstractMemberControllerTest<LoginController> {

    @Override
    public void testHandleSubmitAllGood() {
        // Persist a member
        Member member = this.service.save(getTestEntities().get(0));

        // Verify member was persisted
        assertTrue("Member is persisted", member.isExistingEntity());

        LoginMemberBean loginMemberBean = new LoginMemberBean(member.getUserName(), member.getPassword());

        // Sign in with the persisted member
        String handleSubmit = this.controller.handleSubmit(loginMemberBean, mockBindingResult, mockModel);

        String expectedResult = MvcUtils.redirectTo(Page.INDEX_FW, new RedirectEntity("member", member.getId()));

        assertEquals("Expected result=" + expectedResult + " should match return value", expectedResult, handleSubmit);
    }

    @Test
    public void testHandleSubmitNoUserWithThatname() {
        Member member = this.service.save(TestFixture.getValidMemberWithoutReferences("User", "Passw0rd123", "UserReal"));

        // Verify member was persisted
        assertTrue("Member is persisted", member.isExistingEntity());

        String nonMatchingUserName = "SomeRandomShite";

        assertFalse("Member should not have username=" + nonMatchingUserName, nonMatchingUserName.equals(member.getUserName()));

        // Give the bean the non existing username
        LoginMemberBean loginMemberBean = new LoginMemberBean(nonMatchingUserName, member.getPassword());

        String handleSubmit = this.controller.handleSubmit(loginMemberBean, mockBindingResult, mockModel);
        String expectedResult = Page.LOGIN_FW;

        assertEquals("Expected result=" + expectedResult + " should match return value", expectedResult, handleSubmit);
        assertTrue("Mock model should contain key=userNotFound", mockModel.containsKey("userNotFound"));
        assertTrue("Mock model should contain key=userNotFoundMsg", mockModel.containsKey("userNotFoundMsg"));
    }

    @Override
    protected LoginController initController() {
        return new LoginController();
    }

    @Override
    protected String getValidIndexUrl() {
        return Page.LOGIN;
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.LOGIN_FW;
    }


}
