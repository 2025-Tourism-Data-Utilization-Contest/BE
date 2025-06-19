package com.saerok.showing.api.domain.birdTheme.respository;

import com.saerok.showing.api.domain.bird.entity.Bird;
import com.saerok.showing.api.domain.birdTheme.entity.BirdTheme;
import com.saerok.showing.api.domain.theme.entity.Theme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BirdThemeRepository extends JpaRepository<BirdTheme, Long> {

    @Query("SELECT bt.bird FROM BirdTheme bt WHERE bt.theme.id = :themeId")
    List<Bird> findBirdsByThemeId(@Param("themeId") Long themeId);

    @Query("SELECT bt.theme FROM BirdTheme bt WHERE bt.bird.id = :birdId")
    List<Theme> findThemesByBirdId(@Param("birdId") Long birdId);
}
