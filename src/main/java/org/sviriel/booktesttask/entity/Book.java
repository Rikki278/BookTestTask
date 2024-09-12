package org.sviriel.booktesttask.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.Year;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // анотація для перевірки, щоб було заповнено поле
    @NotBlank(message = "Can't be empty")
    private String title;

    @NotBlank(message = "Can't be empty")
    private String author;

    // анотація для перевірки, що publicationYear
    // немає у майбутньму,
    // а є тільки у минулому чи у поточному часі
    @PastOrPresent(message = "The year of publication cannot be greater than the current year")
    private Year publicationYear;

    private String genre;

    @Column(length = 13, unique = true)
    @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters long")
    private String isbn;


}
