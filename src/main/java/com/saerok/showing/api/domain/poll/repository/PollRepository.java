package com.saerok.showing.api.domain.poll.repository;

import com.saerok.showing.api.domain.poll.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollRepository extends JpaRepository<Poll, Long> {

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.poll.id = :pollId")
    int countCommentsOfPoll(@Param("pollId") Long pollId);
}
