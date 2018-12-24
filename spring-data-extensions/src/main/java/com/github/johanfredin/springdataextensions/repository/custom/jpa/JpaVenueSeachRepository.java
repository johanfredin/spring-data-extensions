package com.github.johanfredin.springdataextensions.repository.custom.jpa;

import com.github.johanfredin.springdataextensions.repository.custom.VenueSearchRepository;
import com.github.johanfredin.springdataextensions.util.LikeQuery;
import com.github.johanfredin.springdataextensions.util.RepositoryUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaVenueSeachRepository extends AbstractJpaExtensionRepository implements VenueSearchRepository {

    @Transactional
    public List<Venue> getVenuesMatchingSearchParams(String name, String city, String country, Genre genre) {
        return getVenuesMatchingSearchParams(name, city, country, genre, RepositoryUtil.DEFAULT_LARGE_RESULTS);
    }

    @Transactional
    public List<Venue> getVenuesMatchingSearchParams(String name, String city, String country, Genre genre, int maxResults) {

        final String WHERE = " where ";
        final String AND = " and ";

        StringBuilder sql = new StringBuilder();
        sql.append("select DISTINCT(v) from Venue v join v.address a left join v.reviews rvs");

        // Needed to know if we are going to append "where " or " and " to the sql string
        boolean firstParamAdded = false;

        // Check the params
        if (isVp(name)) {
            sql.append(firstParamAdded ? AND : WHERE).append("lower (v.name) like lower(").append(p(name)).append(")");
            firstParamAdded = true;
        }
        if (isVp(city)) {
            sql.append(firstParamAdded ? AND : WHERE).append("lower (a.city) like lower(").append(p(city)).append(")");
            firstParamAdded = true;
        }
        if (isVp(country)) {
            sql.append(firstParamAdded ? AND : WHERE).append("lower (a.country) like lower(").append(p(country)).append(")");
            firstParamAdded = true;
        }
        if (isVp(genre)) {
            sql.append(firstParamAdded ? AND : WHERE).append("v.genre = '").append(genre).append("'");
            firstParamAdded = true;
        }

        TypedQuery<Venue> query = em.createQuery(sql.toString(), Venue.class);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    private String p(String param) {
        return RepositoryUtil.lP(param, LikeQuery.END, false);
    }


}
