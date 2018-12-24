package com.github.johanfredin.springdataextensions.repository.custom.jpa;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.ArtistRepository;
import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import com.github.johanfredin.springdataextensions.repository.custom.DeleteMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaDeleteMemberRepository extends AbstractJpaExtensionRepository implements DeleteMemberRepository {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public void delete(long id) {
        List<Artist> artistsToBeRemoved = getArtistsBelongingToOnlyOneMember(id);
        if (!artistsToBeRemoved.isEmpty()) {
            this.artistRepository.deleteAll(artistsToBeRemoved);
        }

        this.memberRepository.deleteById(id);
    }

    /**
     * Used when we want to fetch artists that only belong to one specific {@link Member}
     * We will take this query into concern in the deletion of members. If a member has artists that no
     * other member has. Then those artists must also be removed. No one wants free floating artists without
     * members right?
     *
     * @return a list of {@link Artist}s belonging only to one specific {@link Member} (if any)
     */
    @SuppressWarnings("unchecked")
    public List<Artist> getArtistsBelongingToOnlyOneMember(long id) {
        String sql = "select * from ARTIST where ENTITY_ID in(\n" +
                "select ARTIST_ID from MEMBER_ARTIST_REL \n" +
                "where MEMBER_ID =:id\n" +
                "and ARTIST_ID not in(\n" +
                "select ARTIST_ID from MEMBER_ARTIST_REL\n" +
                "where MEMBER_ID !=:id))";
        Query query = em.createNativeQuery(sql, Artist.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

}
