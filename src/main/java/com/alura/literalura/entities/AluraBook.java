package com.alura.literalura.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
public class AluraBook
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String language;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonManagedReference
    private AluraAuthor author;

    private Long numberOfDownloads;
}


