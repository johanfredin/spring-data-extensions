package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class MockVenueRepository extends MockRepository<Venue> implements VenueRepository {

    @Override
    public boolean isEmailUnique(String email) {
        return isNoOtherEntityWithEmail(-1L, email);
    }

    @Override
    public boolean isNoOtherEntityWithEmail(long id, String email) {
        for (Venue v : findAll()) {
            if (v.getEmail().equals(email) && v.getId() != id) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Venue> findAllByNameContains(String name) {
        List<Venue> matches = new ArrayList<>();
        for (Venue v : findAll()) {
            if (v.getName().toLowerCase().contains(name.toLowerCase())) {
                matches.add(v);
            }
        }
        return matches;
    }

    @Override
    public Page<Venue> findAllByNameContains(String name, Pageable pageable) {
        return null;
    }

    @Override
    public List<String> getAllVenueNames() {
        List<String> names = new ArrayList<>();
        for (Venue v : findAll()) {
            names.add(v.getName());
        }
        return names;
    }

    @Override
    public Page<String> getAllVenueNames(Pageable pageable) {
        return null;
    }

    @Override
    public List<Venue> getVenuesForMember(long memberId) {
        List<Venue> matches = new ArrayList<>();
        for (Venue v : findAll()) {
            for (Artist a : v.getArtists()) {
                for (Member m : a.getMembers()) {
                    if (m.getId() == memberId && !matches.contains(v)) {
                        matches.add(v);
                    }
                }
            }
        }
        return matches;
    }

    @Override
    public Venue getVenueWithAllChildren(long venueId) {
        return getOne(venueId);
    }

    @Override
    public Venue getVenueWithArtistsAndReviews(long venueId) {
        return getOne(venueId);
    }

}
