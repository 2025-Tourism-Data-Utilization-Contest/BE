package com.saerok.showing.api.domain.poll.entity;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.poll.dto.request.PollCreateRequest;
import com.saerok.showing.api.domain.poll.dto.request.PollUpdateRequest;
import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.team.entity.Team;
import com.saerok.showing.api.global.entity.BaseEntity;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "poll")
public class Poll extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "poll_type", nullable = false)
    private PollStatus pollStatus;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "poll")
    private List<Route> routeOptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public static Poll toEntity(PollCreateRequest request, Member member, Team team) {
        return Poll.builder()
            .title(request.getTitle())
            .pollStatus(request.getPollStatus())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .likeCount(0)
            .member(member)
            .team(team)
            .build();
    }

    public void validateOwner(Poll poll, Member currentMember) {
        if (!poll.getMember().getId().equals(currentMember.getId())) {
            throw ShowingException.from(ErrorCode.POLL_MAKER_MISMATCH);
        }
    }

    public void update(PollUpdateRequest request) {
        this.title = request.getTitle();
        this.pollStatus = request.getPollStatus();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }

    public void registerRouteOption(Route route) {
        if (this.routeOptions.contains(route)) {
            throw ShowingException.from(ErrorCode.DUPLICATE_ROUTE_REGISTERED_IN_POLL);
        }
        this.routeOptions.add(route);
        route.setPoll(this);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
