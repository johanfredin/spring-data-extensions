package com.github.johanfredin.springdataextensions.repository.custom.mock;

import com.github.johanfredin.springdataextensions.repository.custom.DeleteMemberRepository;
import com.github.johanfredin.springdataextensions.repository.custom.jpa.AbstractJpaExtensionRepository;

import java.util.List;

public class MockDeleteMemberRepository extends AbstractJpaExtensionRepository implements DeleteMemberRepository {

    @Override
    public void delete(long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Artist> getArtistsBelongingToOnlyOneMember(long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
