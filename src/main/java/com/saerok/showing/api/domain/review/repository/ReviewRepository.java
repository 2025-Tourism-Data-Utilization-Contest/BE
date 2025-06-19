package com.saerok.showing.api.domain.review.repository;

import com.saerok.showing.api.domain.review.entity.Review;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    int countByTargetTypeAndTargetId(ReviewTargetType targetType, Long targetId);

    List<Review> findTop5ByTargetTypeAndTargetIdOrderByCreatedAtDesc(ReviewTargetType targetType, Long targetId);
}