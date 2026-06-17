package com.pluralsight.concerttracker.models;

import jakarta.persistence.*;

// The person or company that puts on a concert. One promoter can run many concerts.
@Entity
public class Promoter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Promoter() { }

    public Promoter(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
