package com.saerok.showing.api.domain.birdTheme.entity;

import com.saerok.showing.api.domain.bird.entity.Bird;
import com.saerok.showing.api.domain.theme.entity.Theme;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "bird_theme")
public class BirdTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bird_theme_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bird_id", nullable = false)
    private Bird bird;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    public static BirdTheme create(Bird bird, Theme theme) {
        return BirdTheme.builder()
            .bird(bird)
            .theme(theme)
            .build();
    }
}
