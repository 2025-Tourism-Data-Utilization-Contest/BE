package com.saerok.showing.api.domain.review.repository;

import com.saerok.showing.api.domain.review.entity.Review;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    int countByTargetTypeAndTargetId(ReviewTargetType targetType, Long targetId);

    List<Review> findByTargetTypeAndTargetId(ReviewTargetType targetType, Long targetId);

    List<Review> findTop5ByTargetTypeAndTargetIdOrderByCreatedAtDesc(ReviewTargetType targetType, Long targetId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.targetType = :targetType AND r.targetId = :targetId")
    Double findAverageRatingByTarget(
        @Param("targetType") ReviewTargetType targetType,
        @Param("targetId") Long targetId
    );
}