package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.repository.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VenueService extends ServiceBase<Venue, VenueRepository> {

    default List<Venue> findAllByNameContains(String name) {
        return getRepository().findAllByNameContains(name);
    }

    default Page<Venue> findAllByNameContains(String name, Pageable pageable) {
        return getRepository().findAllByNameContains(name, pageable);
    }

    default List<String> getAllVenueNames() {
        return getRepository().getAllVenueNames();
    }

    default Page<String> getAllVenueNames(Pageable pageable) {
        return getRepository().getAllVenueNames(pageable);
    }

    default List<Venue> getVenuesForMember(long memberId) {
        return getRepository().getVenuesForMember(memberId);
    }

    default boolean isEmailUnique(String email) {
        return getRepository().isEmailUnique(email);
    }

    default boolean isNoOtherEntityWithEmail(long id, String email) {
        return getRepository().isNoOtherEntityWithEmail(id, email);
    }

    default Venue getVenueWithAllChildren(long venueId) {
        return getRepository().getVenueWithAllChildren(venueId);
    }

    default Venue getVenueWithArtistsAndReviews(long venueId) {
        return getRepository().getVenueWithArtistsAndReviews(venueId);
    }

    default Venue getVenueWithChildren(long venueId, boolean isFetchArtists, boolean isFetchContacts, boolean isFetcReviews, boolean isFetchAddress) {
        return getRepository().getVenueWithChildren(venueId, isFetchArtists, isFetchContacts, isFetcReviews, isFetchAddress);
    }

}
