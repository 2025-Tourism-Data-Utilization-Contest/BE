package com.saerok.showing.api.domain.team.repository;

import com.saerok.showing.api.domain.team.entity.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t JOIN FETCH t.members WHERE t.id = :teamId")
    Optional<Team> findByIdWithMembers(@Param("teamId") Long teamId);
}
