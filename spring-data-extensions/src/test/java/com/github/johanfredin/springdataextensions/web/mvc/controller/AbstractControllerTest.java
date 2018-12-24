package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.StandaloneMvcTestViewResolver;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import com.github.johanfredin.springdataextensions.repository.mock.MockRepository;
import com.github.johanfredin.springdataextensions.service.ServiceBase;
import com.github.johanfredin.springdataextensions.web.mvc.controller.mock.MockBindingResult;
import com.github.johanfredin.springdataextensions.web.mvc.controller.mock.MockModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Abstract helper class for minimizing code duplication in testing of controllers.
 *
 * @param <E> any class extending {@link AbstractEntity}
 * @param <R> any class extending {@link MockRepository}
 * @param <S> any class extending {@link ServiceBase}
 * @param <C> any class extending {@link ControllerBase}
 * @author johan
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
public abstract class AbstractControllerTest<E extends AbstractEntity, R extends BaseRepository<E>, S extends ServiceBase<E, R>, C extends ControllerBase<S>> {

    protected S service;
    protected C controller;

    protected List<E> testEntities;

    protected MockMvc mockMvc;
    protected MockBindingResult mockBindingResult;
    protected MockModel mockModel;

    @Before
    public void init() {
        this.service = initService();
        this.controller = initController();
        this.mockBindingResult = new MockBindingResult(this.getClass().getSimpleName());
        this.mockModel = new MockModel();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).setViewResolvers(new StandaloneMvcTestViewResolver()).build();

        this.testEntities = initTestEntities();
        this.service.setRepository(getRepository());
        this.controller.setMainService(this.service);
    }

    /**
     * Use this to test the index GET methods in the controllers
     *
     * @throws Exception
     */
    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get(getValidIndexUrl()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(getValidForwardIndexUrl()));
    }

    /**
     * Use this to test the handleSubmit POST method and assume there are no errors
     */
    @Test
    public abstract void testHandleSubmitAllGood();

    public abstract S initService();

    public abstract R getRepository();

    /**
     * The {@link ControllerBase} implementation to use for testing
     */
    protected abstract C initController();

    /**
     * @return the default entities to test with the controller
     */
    protected abstract List<E> initTestEntities();

    protected abstract String getValidIndexUrl();

    protected abstract String getValidForwardIndexUrl();

    /**
     * Get an arbitrary amount of entities
     *
     * @param entities and arbitrary amount of entities
     * @return
     */
    @SafeVarargs
    protected final List<E> initTestEntities(E... entities) {
        return new ArrayList<>(Arrays.asList(entities));
    }

    public List<E> getTestEntities() {
        return testEntities;
    }

    protected List<?> getEmptyList() {
        return new ArrayList<>();
    }

    protected RedirectEntity nre(String name, long id) {
        return new RedirectEntity(name, id);
    }

    @Override
    public String toString() {
        return "AbstractControllerTest [\nservice=" + service + ", \ncontroller=" + controller + ", \ntestEntities="
                + testEntities + ", \nmockMvc=" + mockMvc + ", \nmockBindingResult=" + mockBindingResult
                + ", \nmockModel=" + mockModel + ", \ngetValidIndexUrl()=" + getValidIndexUrl()
                + ", \ngetValidForwardIndexUrl()=" + getValidForwardIndexUrl() + "]";
    }


}
