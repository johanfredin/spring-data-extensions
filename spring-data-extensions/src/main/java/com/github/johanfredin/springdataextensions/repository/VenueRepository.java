package com.github.johanfredin.springdataextensions.repository;


import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.util.RepositoryUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Primary
public interface VenueRepository extends EmailHolderRepository<Venue> {

    /**
     * @param name the name of the {@link Venue} to find matches for
     * @return a list with all venues whose name is like passed in name
     */
    List<Venue> findAllByNameContains(String name);    // replaces List<Venue> getAllVenuesWithMatchingName(String name);

    /**
     * @param name     the name of the {@link Venue} to find matches for
     * @param pageable {@link Pageable} to set limit
     * @return a list with all venues whose name is like passed in name
     */
    Page<Venue> findAllByNameContains(String name, Pageable pageable);// replaces List<Venue> getAllVenuesWithMatchingName(String name, int maxResults);

    /**
     * Get all names of persisted venues
     *
     * @return a {@link List} with all venue names
     */
    @Query("select v.name from Venue v")
    List<String> getAllVenueNames();

    /**
     * Get all names of persisted venues
     *
     * @param pageable {@link Pageable} to set limit
     * @return a {@link List} with all venue names within the passed in maxResult param
     */
    @Query("select v.name from Venue v")
    Page<String> getAllVenueNames(Pageable pageable);    // replaces List<String> getAllVenueNames(int limit);

    /**
     * Fetch all venues associated with a member.
     * Venues are first and foremost associated with {@link Artist}s but
     * then again {@link Artist} are associated with {@link Member}s
     *
     * @param member the {@link Member} owning the {@link Artist}s associated with the {@link Venue}s
     * @return a liste of venues for a member
     */
    @Query(value = "select * from VENUE where ENTITY_ID in(" +
            "select VENUE_ID from VENUE_ARTIST_REL where ARTIST_ID in(" +
            "select ARTIST_ID from MEMBER_ARTIST_REL where MEMBER_ID=:memberId))", nativeQuery = true)
    List<Venue> getVenuesForMember(@Param("memberId") long memberId);

    @Query("select v from Venue v"
            + " left join v.artists as"
            + " left join v.contacts cs"
            + " left join v.reviews rs"
            + " left join v.address a"
            + " where v.id=:id")
    Venue getVenueWithAllChildren(@Param("id") long venueId);

    @Query("select v from Venue v"
            + " left join v.artists as"
            + " left join v.reviews rs"
            + " where v.id=:id")
    Venue getVenueWithArtistsAndReviews(@Param("id") long venueId);


    default Venue getVenueWithChildren(long venueId, boolean isFetchArtists, boolean isFetchContacts, boolean isFetcReviews, boolean isFetchAddress) {
        switch (RepositoryUtil.getBooleanArgsAsString(isFetchArtists, isFetchContacts, isFetcReviews, isFetchAddress)) {
            case "1010":
                return getVenueWithArtistsAndReviews(venueId);
        }
        return getVenueWithAllChildren(venueId);
    }

}

