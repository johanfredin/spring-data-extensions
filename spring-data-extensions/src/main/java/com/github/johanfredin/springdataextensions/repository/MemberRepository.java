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
public interface MemberRepository extends EmailHolderRepository<Member> {

    /**
     * @param artist the {@link Artist} the members belong to
     * @return a list of {@link Member}s for given artist (if any)
     */
    List<Member> findAllByArtistsIn(Artist artist);

    /**
     * Load a member and initialize it's artists as well (if any)
     *
     * @param userName             username of member we are looking for
     * @return persisted {@link Member} with artists
     */
    Member findOneByUserName(String userName);

    Member findOneByUserNameAndPassword(String userName, String password);

    /**
     * Get one or more {@link Member}s matching on {@link Member#getUserName()}.
     *
     * @param userName the username of the member we want
     * @return a collection of {@link Member}s matching the param(if any)
     */
    List<Member> findAllByUserNameContaining(String userName);

    /**
     * Get one or more {@link Member}s matching on {@link Member#getEmail()}.
     *
     * @param email the email of the member we want
     * @return a collection of {@link Member}s matching the param(if any)
     */
    @Query("select m from Member m where m.email LIKE ?1")
    List<Member> findAllByEmailIsLike(String email);

    /**
     * Get one or more {@link Member}s matching on {@link Member#getUserName()}.
     *
     * @param userName the username of the member we want
     * @param p        {@link Pageable} the amount of results we want
     * @return a collection of {@link Member}s matching the param(if any)
     */
    Page<Member> findAllByUserNameIsLike(String userName, Pageable p);

    @Query("select case when (count(m) <= 0) then true else false end from Member m where m.userName=:val")
    boolean isUserNameUnique(@Param("val") String userName);

    @Query("select case when (count(m) <= 0) then true else false end from Member m where m.userName=:val and m.id not like :id")
    boolean isNoOtherEntityWithUserName(@Param("id") long memberId, @Param("val") String userName);

    /**
     * Get one or more {@link Member}s matching on {@link Member#getEmail()}.
     *
     * @param email the email of the member we want
     * @param p     {@link Pageable} the amount of resuts we want
     * @return a collection of {@link Member}s matching the param(if any)
     */
    @Query("select m from Member m where m.email LIKE ?1")
    Page<Member> findAllByEmailIsLike(String email, Pageable p);

    @Query("select m from Member m left join m.artists ars left join m.sentMessages sms left join m.receivedMessages rms where m.id=:id")
    Member getMemberWithAllChildren(@Param("id") long memberId);

    @Query("select m from Member m left join m.artists where m.id=:id")
    Member getMemberWithArtists(@Param("id") long memberId);

    @Query("select m from Member m left join m.sentMessages sms left join m.receivedMessages rms where m.id=:id")
    Member getMemberWithMessages(@Param("id") long memberId);


    default Member getMemberWithChildren(long memberId, boolean isFetchArtists, boolean isFetchMessages) {
        switch (RepositoryUtil.getBooleanArgsAsString(isFetchArtists, isFetchMessages)) {
            case "10":
                return getMemberWithArtists(memberId);
            case "01":
                return getMemberWithMessages(memberId);
            case "00":
                return getOne(memberId);
        }
        return getMemberWithAllChildren(memberId);
    }

    default List<Member> findAllByUserNameOrEmail(String param) {
        if (param.contains("@")) {
            return findAllByEmailIsLike(param);
        }
        return findAllByUserNameContaining(param);
    }

    default Page<Member> findAllByUserNameOrEmail(String param, Pageable p) {
        if (param.contains("@")) {
            return findAllByEmailIsLike(param, p);
        }
        return findAllByUserNameOrEmail(param, p);
    }

}
