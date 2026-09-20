package com.medilab.infospatients.controllers;


import java.util.Optional;

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
import com.medilab.infospatients.exception.MessageError;
import com.medilab.infospatients.services.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
public class PatientController {
    
    private PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
     
    @GetMapping("/patient/list")
    @Operation(
                summary = "Get the list of all patients",
                description = "Retrieves the list of all patients."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "List of all patients",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                        schema = @Schema(implementation = Patient.class)
                        )
            )
        )
    })
    
    public ResponseEntity<Iterable<Patient>> getPatients() {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.getListPatients());
    }
    
    @GetMapping("/patient/{id}")
    @Operation(
            summary = "Get a patient by ID",
            description = "Retrieves a patient using their unique identifier."
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Patient found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Patient.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found",
            content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageError.class)
            )
        )
    })
    public ResponseEntity<?> getPatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    example = "1"
            )
            @PathVariable Integer id) {
        
        Optional<Patient> existingPatient = patientService.getPatientById(id);
        
        if (existingPatient.isEmpty()) {
            return new ResponseEntity<>(new MessageError("Patient not found"), HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok(existingPatient.get());
     }
    
    @PutMapping("/patient/{id}")
    @Operation(
            summary = "Update a patient",
            description = "Updates the information of an existing patient."
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "202",
            description = "Patient successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Patient.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid patient data",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = MessageError.class
                    )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Patient not found",
            content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageError.class)
            )
        )
    })
    public ResponseEntity<?> updatePatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    example = "1"
            )
            @PathVariable Integer id,
            @Valid @RequestBody Patient patient){
                
        Optional<Patient> existingPatient = patientService.getPatientById(id);
        
        if (existingPatient.isEmpty()) {
            return new ResponseEntity<>(new MessageError("Patient not found"), HttpStatus.NOT_FOUND);
        }
    
        Patient patientUpdated = existingPatient.get();
        patientUpdated.setLastName(patient.getLastName());
        patientUpdated.setFirstName(patient.getFirstName());
        patientUpdated.setAddress(patient.getAddress());
        patientUpdated.setDateOfBirth(patient.getDateOfBirth());
        patientUpdated.setGender(patient.getGender());
        patientUpdated.setPhoneNumber(patient.getPhoneNumber());
        
        return ResponseEntity.accepted().body(patientService.save(patientUpdated));
 
    }
    
    @PostMapping("/patient")
    @Operation(
            summary = "Create a patient",
            description = "Creates a new patient using the provided information."
        )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Patient successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Patient.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid patient data",
            content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(
                                       implementation = MessageError.class
                                )
            )
        )
    })
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return new ResponseEntity<Patient>(patientService.save(patient), HttpStatus.CREATED);
                       
    }
    
    @DeleteMapping("/patient/{id}")
    @Operation(
            summary = "Delete a patient",
            description = "Deletes a patient using their unique identifier."
        )
    @ApiResponse(
        responseCode = "204",
        content = @Content
    )
    public ResponseEntity<?> deletePatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    example = "1"
            )
            @PathVariable Integer id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
