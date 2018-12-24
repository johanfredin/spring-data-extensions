package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.web.RedirectEntity;
import com.github.johanfredin.springdataextensions.web.mvc.bean.Bean;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import com.github.johanfredin.springdataextensions.web.ExtError;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

/**
 * Holds more functions to be used in different controllers than the basic {@link ControllerBase}
 *
 * @param <E> any class extending {@link AbstractEntity}
 * @param <B> any class extending {@link Bean}
 * @author johan
 */
public interface ControllerExtensions<E extends AbstractEntity, B extends Bean<?>> {

    /**
     * All domain entities manipulation should be handled via beans to keep things more tidy.
     * All controllers have at least one instance of a {@link Model} where we put domain entities
     * and other fields. This method ensures we follow this convention
     *
     * @param model the {@link Model} that should hold the bean and validation data
     * @param bean  the {@link Bean} implementation that should hold all {@link AbstractEntity} implementations as well as other
     *              data that is used in the views.
     */
    void populateModel(Model model, B bean);

    /**
     * Calls {@link BaseRepository} implementation method getEntityWithChildren(id, boolean...)
     * Instead of having to set the properties each time we only need to do it once here
     *
     * @param id id of the {@link IdHolder} we want
     * @return a populated {@link IdHolder} with arbitrary references fetched
     */
    E getPersistedEntity(long id);

    /**
     * Default HTTP POST method that all classes implementing this interface needs to have.
     * This to ensure that all data is handled by beans. We can not achieve this with the GET method
     * since it varies in parameters.
     *
     * @param bean          a validated {@link Bean} extension that holds all the domain model data we are going to manage
     * @param model         the model is where we add the beans and errors and presents it to the view
     * @param bindingResult holds any standard validation errors like empty fields etc..
     * @return a string URL either reloading the page or redirecting back to another page depending on the result
     */
    String handleSubmit(@Valid B bean, Model model, BindingResult bindingResult);

    /**
     * A more compact way to call a redirect entity
     *
     * @param name entity name
     * @param id   entity id
     * @return a new {@link RedirectEntity} with the name and id params
     */
    default RedirectEntity nre(String name, long id) {
        return new RedirectEntity(name, id);
    }

    /**
     * @param id
     * @return <code>true</code> if id > 0
     */
    default boolean isExistingEntity(long id) {
        return id > 0L;
    }

    default ExtError getError(boolean state, String msg) {
        return new ExtError(state, msg);
    }
}
