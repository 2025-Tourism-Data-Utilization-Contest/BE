package com.saerok.showing.api.domain.team.entity;

import com.saerok.showing.api.domain.team.dto.request.TeamCreateRequest;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "team")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

    public static Team toEntity(TeamCreateRequest request, Member leader, String encryptedPassword) {
        return Team.builder()
            .name(request.getName())
            .password(encryptedPassword)
            .leader(leader)
            .build();
    }

    public boolean isLeader(Member member) {
        return leader != null && leader.getId().equals(member.getId());
    }
}
