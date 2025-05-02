package com.Webtech25.Webtech25;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Kleidung {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String groesse;
    private String lager;

    public Kleidung() {}

    public Kleidung(String name, String groesse, String lager) {
        this.name = name;
        this.groesse = groesse;
        this.lager = lager;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroesse() {
        return groesse;
    }

    public String getLager() {
        return lager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroesse(String groesse) {
        this.groesse = groesse;
    }

    public void setLager(String lager) {
        this.lager = lager;
    }
}
