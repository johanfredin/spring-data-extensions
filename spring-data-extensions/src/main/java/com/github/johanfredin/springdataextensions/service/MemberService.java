package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import com.github.johanfredin.springdataextensions.repository.custom.DeleteMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService extends ServiceBase<Member, MemberRepository> {

    DeleteMemberRepository getDeleteMemberRepository();

    void setDeleteMemberRepository(DeleteMemberRepository repository);

    /**
     * Use the {@link DeleteMemberRepository}s delete method for removing members
     */
    @Override
    default void delete(Member entity) {
        getDeleteMemberRepository().delete(entity.getId());
    }

    default List<Member> findAllByArtistsIn(Artist artist) {
        return getRepository().findAllByArtistsIn(artist);
    }

    default Member findOneByUserName(String userName) {
        return getRepository().findOneByUserName(userName);
    }

    default Member findOneByUserNameAndPassword(String userName, String password) {
        return getRepository().findOneByUserNameAndPassword(userName, password);
    }

    default List<Member> findAllByUserNameContaining(String userName) {
        return getRepository().findAllByUserNameContaining(userName);
    }

    default List<Member> findAllByEmailIsLike(String email) {
        return getRepository().findAllByEmailIsLike(email);
    }

    default Page<Member> findAllByUserNameIsLike(String userName, Pageable p) {
        return getRepository().findAllByUserNameIsLike(userName, p);
    }

    default boolean isUserNameUnique(String userName) {
        return getRepository().isUserNameUnique(userName);
    }

    default boolean isNoOtherEntityWithUserName(long memberId, String userName) {
        return getRepository().isNoOtherEntityWithUserName(memberId, userName);
    }

    default boolean isNoOtherEntityWithEmail(long memberId, String email) {
        return getRepository().isNoOtherEntityWithEmail(memberId, email);
    }

    default Page<Member> findAllByEmailIsLike(String email, Pageable p) {
        return getRepository().findAllByEmailIsLike(email, p);
    }

    default Member getMemberWithAllChildren(long memberId) {
        return getRepository().getMemberWithAllChildren(memberId);
    }

    default Member getMemberWithArtists(long memberId) {
        return getRepository().getMemberWithArtists(memberId);
    }

    default Member getMemberWithMessages(long memberId) {
        return getRepository().getMemberWithMessages(memberId);
    }

    default Member getMemberWithChildren(long memberId, boolean isFetchArtists, boolean isFetchMessages) {
        return getRepository().getMemberWithChildren(memberId, isFetchArtists, isFetchMessages);
    }

    default List<Member> findAllByUserNameOrEmail(String param) {
        return getRepository().findAllByUserNameOrEmail(param);
    }

    default Page<Member> findAllByUserNameOrEmail(String param, Pageable p) {
        return getRepository().findAllByUserNameOrEmail(param, p);
    }


}
