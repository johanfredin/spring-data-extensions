package com.github.johanfredin.springdataextensions.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface EmailHolderRepository<E extends EmailHolder> extends BaseRepository<E> {

    @Query("select case when (count(e) <= 0) then true else false end from #{#entityName} e where e.email=:val")
    boolean isEmailUnique(@Param("val") String email);

    @Query("select case when (count(e) <= 0) then true else false end from #{#entityName} e where e.email=:val and e.id != :id")
    boolean isNoOtherEntityWithEmail(@Param("id") long id, @Param("val") String email);

}
