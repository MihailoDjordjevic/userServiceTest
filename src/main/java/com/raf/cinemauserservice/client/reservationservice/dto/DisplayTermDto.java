package com.raf.cinemauserservice.client.reservationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class DisplayTermDto {

    private Long id;
    private LocalDateTime date;
    private String gym;
    private String trainingType;
    private Integer avaliableSpots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public Integer getAvaliableSpots() {
        return avaliableSpots;
    }

    public void setAvaliableSpots(Integer avaliableSpots) {
        this.avaliableSpots = avaliableSpots;
    }
}
