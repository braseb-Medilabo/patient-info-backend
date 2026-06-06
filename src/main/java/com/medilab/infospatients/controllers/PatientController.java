package com.medilab.infospatients.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medilab.infospatients.entitys.Patient;
import com.medilab.infospatients.services.PatientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
public class PatientController {
    
    private PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    
    @GetMapping("/patient/list")
    public ResponseEntity<Iterable<Patient>> getPatients() {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getListPatients());
    }
    
    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Integer id) {
        System.out.println("je rentre");
        return patientService.getPatientById(id)
                .map(patient -> ResponseEntity.ok(patient))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/patient/{id}")
    public ResponseEntity<Patient> updatePatient(@Valid @RequestBody Patient patient, @PathVariable Integer id){
                return  patientService.getPatientById(id)
                                    .map(p -> {
                                                p.setLastName(patient.getLastName());
                                                p.setFirstName(patient.getFirstName());
                                                p.setAddress(patient.getAddress());
                                                p.setDateOfBirth(patient.getDateOfBirth());
                                                p.setGender(patient.getGender());
                                                p.setPhoneNumber(patient.getPhoneNumber());
                                                return ResponseEntity.accepted().body(patientService.save(p));
                                    })
                                    .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/patient")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient){
        return new ResponseEntity<Patient>(patientService.save(patient), HttpStatus.CREATED);
                       
    }
    
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
