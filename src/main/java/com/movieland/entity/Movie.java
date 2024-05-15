package com.movieland.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.VERSION)
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_id_sequence")
    @SequenceGenerator(name = "movies_id_sequence", sequenceName = "movies_id_sequence", allocationSize = 1)
    private int id;

    @Column(name = "name_russian")
    private String nameRussian;

    @Column(name = "name_native")
    private String nameNative;

    @Column(name = "year_of_release")
    private int yearOfRelease;

    @Column(name = "description")
    private String description;

    @Column(name = "rating", columnDefinition = "NUMERIC(8,2)")
    private Double rating;

    @Column(name = "price", columnDefinition = "NUMERIC(8,2)")
    private Double price;

    @Column(name = "picture_path")
    private String picturePath;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "movies_countries_map",
            joinColumns = @JoinColumn(
                    name = "movie_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "country_id",
                    referencedColumnName = "id"
            )
    )
    private List<Country> countries;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "movies_genre_map",
            joinColumns = @JoinColumn(
                    name = "movie_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_id",
                    referencedColumnName = "id"
            )
    )
    private List<Genre> genres;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Review> reviews;

    public void addReview(Review review) {
        this.getReviews().add(review);
        review.setMovie(this);
    }

    @Version
    private Integer version;
}
