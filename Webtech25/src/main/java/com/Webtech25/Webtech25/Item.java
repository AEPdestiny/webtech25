package com.Webtech25.Webtech25;


import jakarta.persistence.*;

@Entity(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lager")
    private Long lager;

    @Column(name = "anzahl")
    private Long anzahl;

    @Column(name = "name")
    private String name;

    public Item(Long lager, Long anzahl, String name) {
        this.lager = lager;
        this.anzahl = anzahl;
        this.name = name;
    }

    protected Item(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLager() {
        return lager;
    }

    public void setLager(Long abteilung) {
        this.lager = abteilung;
    }

    public Long getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(Long anzahl) {
        this.anzahl = anzahl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}