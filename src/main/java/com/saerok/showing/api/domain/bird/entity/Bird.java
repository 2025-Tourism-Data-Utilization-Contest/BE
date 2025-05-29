package com.saerok.showing.api.domain.bird.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "bird")
public class Bird {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bird_id")
    private Long id;

    @Column(name = "name_kr", nullable = false)
    private String nameKr;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "description")
    private String description;

    @Column(name = "bird_image")
    private String birdImage;
}
