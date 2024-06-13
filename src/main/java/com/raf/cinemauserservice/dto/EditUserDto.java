package com.raf.cinemauserservice.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import java.time.LocalDate;

public class EditUserDto {

    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private LocalDate birthday;
    @Email
    @Nullable
    private String email;

    public EditUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }
}
