package com.movieland.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_sequence")
    @SequenceGenerator(name = "reviews_id_sequence", sequenceName = "reviews_id_sequence", allocationSize = 1)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User user;

    @Column(name = "text")
    private String text;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "movie_id")
    private Movie movie;

}
