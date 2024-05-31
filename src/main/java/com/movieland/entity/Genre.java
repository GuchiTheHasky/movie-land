package com.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genres_id_sequence")
    @SequenceGenerator(name = "genres_id_sequence", sequenceName = "genres_id_sequence", allocationSize = 1)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Movie> movies;

}
