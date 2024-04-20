package org.richard.home.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
    private Integer alter;
    private String position;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Embedded
    private Country countryOfBirth;

    public Player() {}

    public Player(Integer id, String name, Integer alter, String position, LocalDate dateOfBirth, Country countryOfBirth) {
        this.id = id;
        this.name = name;
        this.alter = alter;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.countryOfBirth = countryOfBirth;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAlter() {
        return alter;
    }

    public void setAlter(Integer alter) {
        this.alter = alter;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountryOfBirth() {
        return countryOfBirth.getValue();
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = Country.valueOf(countryOfBirth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alter=" + alter +
                ", position='" + position + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", countryOfBirth='" + countryOfBirth + '\'' +
                '}';
    }
}
