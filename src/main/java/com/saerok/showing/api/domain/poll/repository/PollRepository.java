package com.saerok.showing.api.domain.poll.repository;

import com.saerok.showing.api.domain.poll.entity.Poll;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends JpaRepository<Poll, Long> {

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.poll.id = :pollId")
    int countCommentsOfPoll(@Param("pollId") Long pollId);

    // 팀 내 모든 투표 조회
    @Query("SELECT p FROM Poll p WHERE p.team.id = :teamId")
    List<Poll> findAllByTeamId(@Param("teamId") Long teamId);

    // 팀 내 특정 투표 단건 조회
    @Query("SELECT p FROM Poll p WHERE p.id = :pollId AND p.team.id = :teamId")
    Optional<Poll> findByIdAndTeamId(@Param("pollId") Long pollId, @Param("teamId") Long teamId);
}
