package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.ResetMemberPasswordBean;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
@ComponentScan
public class ResetPasswordControllerTest extends AbstractMemberControllerTest<ResetPasswordController> {

    @Override
    public void testHandleSubmitAllGood() {
        Member member = service.save(TestFixture.getValidMember("Jon", "doe12345", "Jonas"));
        String newPassword = "newPassword123";

        ResetMemberPasswordBean bean = new ResetMemberPasswordBean(member.getUserName(), newPassword, newPassword);
        String handleSubmit = controller.handleSubmit(bean, mockBindingResult, mockModel);
        String expectedResult = redirectTo("index", new RedirectEntity("member", member.getId()));
        assertEquals("Handle submit should return " + expectedResult, expectedResult, handleSubmit);
    }

    @Test
    public void testHandleSubmitUserDoesNotExist() {
        Member unknownMember = initTestEntities().get(0);
        String newPassword = "newPassword123";

        ResetMemberPasswordBean bean = new ResetMemberPasswordBean(unknownMember.getUserName(), newPassword, newPassword);
        String handleSubmit = controller.handleSubmit(bean, mockBindingResult, mockModel);
        String expectedResult = Page.RESET_PASSWORD_FW;

        assertTrue("Mockmodel should contain userNotFound key and message", mockModel.containsKey(MvcErrors.USR_NAME_EXISTS) && mockModel.containsKey(MvcErrors.USR_NAME_EXISTS_MSG_KEY));
        assertEquals("Handle submit should return " + expectedResult, expectedResult, handleSubmit);
    }

    @Test
    public void testHandleSubmitRepeatPasswordDontMatch() {
        Member member = service.save(TestFixture.getValidMember("Jon", "doe12345", "Jonas"));
        String newPassword = "newPassword123";

        ResetMemberPasswordBean bean = new ResetMemberPasswordBean(member.getUserName(), "oldPassword123", newPassword);
        String handleSubmit = controller.handleSubmit(bean, mockBindingResult, mockModel);
        String expectedResult = Page.RESET_PASSWORD_FW;

        assertTrue("Mockmodel should contain isIncorrectRepeatPassword key and message", mockModel.containsKey(MvcErrors.NEW_PW_INCORRECT) && mockModel.containsKey(MvcErrors.NEW_PW_INCORRECT_MSG_KEY));
        assertEquals("Handle submit should return " + expectedResult, expectedResult, handleSubmit);
    }

    @Override
    protected ResetPasswordController initController() {
        return new ResetPasswordController();
    }

    @Override
    public void testIndex() throws Exception {
        mockMvc.perform(get(Page.RESET_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(Page.RESET_PASSWORD_FW));
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.RESET_PASSWORD;
    }

    @Override
    protected String getValidIndexUrl() {
        return Page.RESET_PASSWORD_FW;
    }

}