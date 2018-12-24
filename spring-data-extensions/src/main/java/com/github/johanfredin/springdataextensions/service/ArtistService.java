package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.domain.Artist;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.ArtistRepository;

import java.util.List;

public interface ArtistService extends ServiceBase<Artist, ArtistRepository> {

    default List<Artist> getArtistsForMember(Member member) {
        return getRepository().getArtistsForMember(member);
    }

    default Artist getArtistWithMembers(long artistId) {
        return getRepository().getArtistWithMembers(artistId);
    }

    default Artist getArtistWithAllChildren(long artistId) {
        return getRepository().getArtistWithAllChildren(artistId);
    }

    default Artist getArtistWithReviewsAndVenues(long artistId) {
        return getRepository().getArtistWithReviewsAndVenues(artistId);
    }

    default Artist getArtistWithReviews(long artistId) {
        return getRepository().getArtistWithReviews(artistId);
    }

    default Artist getArtistWithVenues(long artistId) {
        return getRepository().getArtistWithVenues(artistId);
    }

    default boolean isEmailUnique(long id, String email) {
        return email.isEmpty() ? true : getRepository().isNoOtherEntityWithEmail(id, email);
    }

    default Artist getArtistWithChildren(long artistId, boolean isFetchReviews, boolean isFetchRequests, boolean isFetchGigs, boolean isFetchVenues) {
        return getRepository().getArtistWithChildren(artistId, isFetchReviews, isFetchRequests, isFetchGigs, isFetchVenues);
    }

}
