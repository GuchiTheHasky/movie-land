package com.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countries_id_seq")
    @SequenceGenerator(name = "countries_id_seq", sequenceName = "countries_id_sequence", allocationSize = 1)
    private int id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "movies_countries_map",
            joinColumns = @JoinColumn(name = "country_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private List<Movie> movies;
}
