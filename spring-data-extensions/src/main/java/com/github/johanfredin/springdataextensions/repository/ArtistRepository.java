package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.util.RepositoryUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Primary
public interface ArtistRepository extends EmailHolderRepository<Artist> {

    /**
     * Get all the artists of the member
     *
     * @param member the {@link Member} containing the artists
     * @return all the artists of the member or empty list
     */
    @Query("select m.artists from Member m where m=:param")
    List<Artist> getArtistsForMember(@Param("param") Member member);

    /**
     * @param artistId the id of the artist to fetch
     * @return the {@link Artist} with the passed in id and all its {@link Member}s
     */
    @Query("select DISTINCT(a) from Artist a join fetch a.members mrs where a.id=:id")
    Artist getArtistWithMembers(@Param("id") long artistId);

    /**
     * @param artistId the id of the {@link Artist}
     * @return the artist and all of the children (members, reviews, requests, gigs and venues) initiated
     */
    @Query("select DISTINCT(a) from Artist a"
            + " join fetch a.members mrs"
            + " left join a.reviews rvw"
            + " left join a.requests rqs"
            + " left join a.gigs gs"
            + " left join a.venues vs"
            + " where a.id=:id")
    Artist getArtistWithAllChildren(@Param("id") long artistId);

    @Query("select DISTINCT(a) from Artist a left join a.reviews rvw left join a.venues vs where a.id=:id")
    Artist getArtistWithReviewsAndVenues(@Param("id") long artistId);

    @Query("select DISTINCT(a) from Artist a left join a.reviews rvw where a.id=:id")
    Artist getArtistWithReviews(@Param("id") long artistId);

    @Query("select DISTINCT(a) from Artist a left join a.venues vs where a.id=:id")
    Artist getArtistWithVenues(@Param("id") long artistId);

    default Artist getArtistWithChildren(long artistId, boolean isFetchReviews, boolean isFetchRequests, boolean isFetchGigs, boolean isFetchVenues) {
        switch (RepositoryUtil.getBooleanArgsAsString(isFetchReviews, isFetchRequests, isFetchGigs, isFetchVenues)) {
            case "1001":
                return getArtistWithReviewsAndVenues(artistId);
            case "0001":
                return getArtistWithVenues(artistId);
            case "1000":
                return getArtistWithReviews(artistId);
        }
        return getArtistWithAllChildren(artistId);
    }
}
