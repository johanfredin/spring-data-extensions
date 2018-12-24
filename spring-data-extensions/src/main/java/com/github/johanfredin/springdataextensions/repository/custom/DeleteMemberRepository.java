package com.github.johanfredin.springdataextensions.repository.custom;

import com.github.johanfredin.springdataextensions.domain.Member;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

import java.util.List;

@NoRepositoryBean
@Component(value = "deleteMemberRepository")
public interface DeleteMemberRepository {

    /**
     * The remove operation of a member needs special logic.
     * By default there is no cascade delete for a members artists.
     * However, if we decide to drop a member and the artist(s) of the member
     * only have 1 member which is the member we are removing here, we also
     * remove that artist. There should be no artist without members
     *
     * @param member the {@link Member} we want to remove
     */
    default void delete(Member member) {
        delete(member.getId());
    }

    /**
     * The remove operation of a member needs special logic.
     * By default there is no cascade delete for a members artists.
     * However, if we decide to drop a member and the artist(s) of the member
     * only have 1 member which is the member we are removing here, we also
     * remove that artist. There should be no artist without members
     *
     * @param id the identifier of the {@link Member} we want to remove
     */
    void delete(long id);

    /**
     * Used when we want to fetch artists that only belong to one specific {@link Member}
     * We will take this query into concern in the deletion of members. If a member has artists that no
     * other member has. Then those artists must also be removed. No one wants free floating artists without
     * members right?
     *
     * @param member the the {@link Member} owning the artists
     * @return a list of {@link Artist}s belonging only to one specific {@link Member} (if any)
     */
    public List<Artist> getArtistsBelongingToOnlyOneMember(long id);
}
