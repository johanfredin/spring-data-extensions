package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.MvcUtils;
import com.github.johanfredin.springdataextensions.web.Page;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
public class IndexControllerTest extends AbstractMemberControllerTest<IndexController> {

    public void testHandleSubmitAllGood() {
        // Not used
    }

    protected IndexController initController() {
        return new IndexController();
    }

    @Override
    protected String getValidIndexUrl() {
        return MvcUtils.redirectTo(Page.INDEX_FW, new RedirectEntity("member", 1));
    }

    @Override
    protected String getValidForwardIndexUrl() {
        return Page.INDEX_FW;
    }

}
