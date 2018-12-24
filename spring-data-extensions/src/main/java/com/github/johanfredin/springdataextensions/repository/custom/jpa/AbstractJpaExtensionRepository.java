package com.github.johanfredin.springdataextensions.repository.custom.jpa;

import com.github.johanfredin.springdataextensions.util.RepositoryUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractJpaExtensionRepository {

    @PersistenceContext
    protected EntityManager em;

    /**
     * @param param
     * @return {@link RepositoryUtil#isValidParam(param)}
     */
    protected boolean isVp(Object param) {
        return RepositoryUtil.isValidParam(param);
    }

}
