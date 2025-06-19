package com.saerok.showing.api.domain.theme.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "origin_title", nullable = false)
    private String originTitle;

    @Column(name = "description", nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(
        name = "theme_season",
        joinColumns = @JoinColumn(name = "theme_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private List<Season> seasons = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "theme_daytime",
        joinColumns = @JoinColumn(name = "theme_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "daytime")
    private List<DayTime> dayTimes = new ArrayList<>();

    @Column(name = "theme_image")
    private String themeImage;

    @Column(name = "location_x", nullable = false)
    private Double locationX;

    @Column(name = "location_y", nullable = false)
    private Double locationY;
}
