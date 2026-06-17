package com.pluralsight.concerttracker.models;

import jakarta.persistence.*;

// A musical act that headlines concerts. One artist can headline many concerts.
@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;

    public Artist() { }

    public Artist(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
