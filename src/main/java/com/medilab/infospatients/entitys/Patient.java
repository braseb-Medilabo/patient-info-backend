package com.medilab.infospatients.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 125, nullable = false)
    @NotBlank(message = "firstName is required")
    private String firstName;
    @Column(length = 125, nullable = false)
    @NotBlank(message = "lastName is required")
    private String lastName;
    @Column(length = 10, nullable = false)
    @NotBlank(message = "date of birth is required")
    private String dateOfBirth;
    @Column(length = 10, nullable = false)
    @NotBlank(message = "gender is required")
    private String gender;
    private String address;
    private String phoneNumber;
    
}
