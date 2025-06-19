package com.saerok.showing.api.domain.place.entity;

import com.saerok.showing.api.domain.theme.entity.Theme;
import com.saerok.showing.api.global.entity.BaseEntity;
import com.saerok.showing.api.global.utils.Locatable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@Table(name = "place")
public class Place extends BaseEntity implements Locatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "available_from")
    private LocalDate availableFrom;

    @Column(name = "available_to")
    private LocalDate availableTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private PlaceType placeType;

    @Column(name = "place_image")
    private String placeImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Column(name = "location_x", nullable = false)
    private Double locationX;

    @Column(name = "location_y", nullable = false)
    private Double locationY;

    @Override
    public double getLocationX() {
        return this.locationX;
    }

    @Override
    public double getLocationY() {
        return this.locationY;
    }
}
