package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.ArtistRepository;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public class MockArtistRepository extends MockRepository<Artist> implements ArtistRepository {

    @Override
    public boolean isEmailUnique(String email) {
        for (Artist a : findAll()) {
            if (a.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isNoOtherEntityWithEmail(long id, String email) {
        return true;
    }

    @Override
    public List<Artist> getArtistsForMember(Member member) {
        return member.getArtists();
    }

    @Override
    public Artist getArtistWithMembers(long artistId) {
        Artist a = getOne(artistId);
        if (a.getMembers() == null || a.getMembers().isEmpty()) {
            return null;
        }
        return a;
    }

    @Override
    public Artist getArtistWithAllChildren(long artistId) {
        return getOne(artistId);
    }

    @Override
    public Artist getArtistWithReviewsAndVenues(long artistId) {
        return getOne(artistId);
    }

    @Override
    public Artist getArtistWithReviews(long artistId) {
        return getOne(artistId);
    }

    @Override
    public Artist getArtistWithVenues(long artistId) {
        return getOne(artistId);
    }


    @Override
    public Optional<Artist> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public <S extends Artist> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }
}
