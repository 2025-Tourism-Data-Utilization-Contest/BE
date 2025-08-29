package com.saerok.showing.api.domain.memberTeam.repository;

import com.saerok.showing.api.domain.memberTeam.entity.MemberTeam;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);

    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);

    @Query("""
            SELECT mt FROM MemberTeam mt
            JOIN FETCH mt.member
            WHERE mt.team.id = :teamId
        """)
    List<MemberTeam> findWithMemberByTeamId(@Param("teamId") Long teamId);

    @Query("""
            SELECT mt FROM MemberTeam mt
            JOIN FETCH mt.team
            WHERE mt.member.id = :memberId
        """)
    List<MemberTeam> findWithTeamByMemberId(@Param("memberId") Long memberId);
}
