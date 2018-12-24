package com.github.johanfredin.springdataextensions.web;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.constants.Constants;
import com.github.johanfredin.springdataextensions.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class MvcUtilsTest {

    @Test
    public void testIsRepeatPasswordIncorrect() {
        String pw = getRandomValidPassword();
        String rpw = pw.toUpperCase();
        assertTrue("Passwords should not match", MvcUtils.isRepeatPasswordIncorrect(pw, rpw));
    }

    @Test
    public void testIsCurrentPasswordIncorrect() {
        String pw1 = "";
        String pw2 = getRandomValidPassword();

        assertTrue("Passwords should not pass since one of them is empty", MvcUtils.isCurrentPasswordIncorrect(pw1, pw2));
        pw1 = pw2;
        assertFalse("Passwords should now pass since they are equal", MvcUtils.isCurrentPasswordIncorrect(pw1, pw2));
    }

    @Test
    public void testIsValidButIncorrectPassword() {
        Member validMember = TestFixture.getValidMemberWithoutReferences("Yo", getRandomValidPassword(), "Johan");

        assertFalse("Passing in a null member should return false", MvcUtils.isValidButIncorrectPassword(getRandomValidPassword(), null));
        assertTrue("Passing in a valid member and non matching passwords should return true", MvcUtils.isValidButIncorrectPassword(getRandomValidPassword(), validMember));
    }

    @Test
    public void testIsValidPassword() {
        String tooLongPw = getRandomValidPassword() + "toolong";
        String tooShortPw = getRandomValidPassword().substring(0, Constants.MIN_LENGTH_PWD - 1);

        assertFalse("tooLongPw is too long to be valid", MvcUtils.isValidPassword(tooLongPw));
        assertFalse("tooShortPw is too short to be valid", MvcUtils.isValidPassword(tooShortPw));
    }

    @Test
    public void testIsAnyErrors() {
        boolean[] oneFalsie = {true, false, true};
        boolean[] allTrue = {true, true, true, true};
        boolean[] allFalse = {false, false};

        assertTrue("oneFalse should not pass", MvcUtils.isAnyErrors(oneFalsie));
        assertFalse("allTrue should pass", MvcUtils.isAnyErrors(allTrue));
        assertTrue("allFalse should not pass", MvcUtils.isAnyErrors(allFalse));
    }

    @Test
    public void testRedirect() {
        String redirectPage = MvcUtils.redirectTo("test");
        String result = "redirect:/test.html";
        assertEquals("Redirect page should equal " + result, result, redirectPage);
    }

    @Test
    public void testRedirectOneEntity() {
        String redirectPage = MvcUtils.redirectTo("test", new RedirectEntity("yolo", 1));
        String result = "redirect:/test/yolo=1.html";
        assertEquals("Redirect page should equal " + result, result, redirectPage);
    }

    @Test
    public void testRedirectMultipleEntities() {
        RedirectEntity[] res = {
                new RedirectEntity("e1", 1),
                new RedirectEntity("e2", 5),
                new RedirectEntity("e5", 25)
        };

        String redirectPage = MvcUtils.redirectTo("test", res);
        String result = "redirect:/test/e1=1/e2=5/e5=25.html";
        assertEquals("Redirect page should equal " + result, result, redirectPage);
    }

    @Test
    public void testRedirectMultipleEntities2() {
        String redirectPage = MvcUtils.redirectTo(Page.EDIT_VENUE_FW, new RedirectEntity("venue", 1), new RedirectEntity("artist", 1), new RedirectEntity("member", 1));
        String result = "redirect:/editVenue/venue=1/artist=1/member=1.html";
        assertEquals("Redirect page should equal " + result, result, redirectPage);
    }


    @Test
    public void testRedirectWithAbstractEntity() {
        AbstractEntity e = new Member();
        e.setId(5L);
        String redirectUrl = MvcUtils.redirectTo("editMember", e);
        String expectedResult = "redirect:/editMember/member=5.html";
        assertEquals("Redirect page should equal", expectedResult, redirectUrl);
    }

    @Test
    public void testRedirectWithMultipleAbstractEntities() {
        AbstractEntity[] es = {new Member(), new Artist()};
        es[0].setId(1);
        es[1].setId(2L);

        String redirectUrl = MvcUtils.redirectTo("editArtist", es);
        String expectedResult = "redirect:/editArtist/member=1/artist=2.html";
        assertEquals("Redirect page should equal", expectedResult, redirectUrl);
    }

    private String getRandomValidPassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789@$€¥{[[";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < Constants.MAX_LENGTH_PWD; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length() - 1)));
        }
        return sb.toString();
    }


}
