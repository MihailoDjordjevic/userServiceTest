package com.raf.cinemauserservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BannedUsers {
    @Id
    private Long bannedId;


    public Long getBannedId() {
        return bannedId;
    }

    public void setBannedId(Long bannedId) {
        this.bannedId = bannedId;
    }
}
