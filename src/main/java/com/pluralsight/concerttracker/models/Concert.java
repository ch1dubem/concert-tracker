package com.pluralsight.concerttracker.models;

import jakarta.persistence.*;

// Every concert points to exactly one artist, one venue, and one promoter.
// optional = false makes each required, so a concert can never be saved without all three.
@Entity
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int concertYear;
    private double ticketPrice;
    private int ticketsSold;

    @ManyToOne(optional = false)
    private Artist artist;

    @ManyToOne(optional = false)
    private Venue venue;

    @ManyToOne(optional = false)
    private Promoter promoter;

    public Concert() { }

    public Concert(int concertYear, double ticketPrice, int ticketsSold,
                   Artist artist, Venue venue, Promoter promoter) {
        this.concertYear = concertYear;
        this.ticketPrice = ticketPrice;
        this.ticketsSold = ticketsSold;
        this.artist = artist;
        this.venue = venue;
        this.promoter = promoter;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getConcertYear() { return concertYear; }
    public void setConcertYear(int concertYear) { this.concertYear = concertYear; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
    public int getTicketsSold() { return ticketsSold; }
    public void setTicketsSold(int ticketsSold) { this.ticketsSold = ticketsSold; }
    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public Promoter getPromoter() { return promoter; }
    public void setPromoter(Promoter promoter) { this.promoter = promoter; }

}