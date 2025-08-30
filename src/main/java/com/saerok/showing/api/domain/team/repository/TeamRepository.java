package com.saerok.showing.api.domain.team.repository;

import com.saerok.showing.api.domain.team.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    boolean existsByName(String name);

    @Query("""
            select distinct t
            from Team t
            join t.memberTeams mt
            join fetch t.leader
            where mt.member.id = :memberId
            order by t.name asc
        """)
    List<Team> findAllByMemberId(Long memberId);
}
