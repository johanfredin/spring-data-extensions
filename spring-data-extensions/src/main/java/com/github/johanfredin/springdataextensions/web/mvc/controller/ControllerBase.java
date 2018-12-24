package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.service.ServiceBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract superclass for all controller classes that comes
 * with some helper methods as well as abstract methods that
 * all child controllers should have
 *
 * @author johan
 */
public abstract class ControllerBase<S extends ServiceBase<?, ?>> {

    protected Logger log = LogManager.getLogger(this.getClass());

    public ControllerBase() {
    }

    public ControllerBase(S mainService) {
        setMainService(mainService);
    }

    /**
     * @return the main {@link ServiceBase} implementation used in controller
     */
    public abstract S getMainService();

    /**
     * @param mainService the main {@link ServiceBase} implementation used in controller
     */
    public abstract void setMainService(S mainService);

}
