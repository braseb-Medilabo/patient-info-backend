package com.medilab.infospatients;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilab.infospatients.controllers.PatientController;
import com.medilab.infospatients.entitys.Patient;

import com.medilab.infospatients.services.PatientService;

//@SpringBootTest
//@ActiveProfiles("dev")
//@AutoConfigureMockMvc
@WebMvcTest(controllers = PatientController.class)
class InfosPatientsApplicationTests {

    @Autowired
    MockMvc mockMvc;
    
    //@Autowired
    ObjectMapper objectMapper;
    
    @MockitoBean
    PatientService patientService;
    
        
    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        
    }
    
	/*@Test
	void contextLoads() {
	}*/
    
    @Test
    void shouldReturnPatientList() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("toto");
        patient.setLastName("liste");
        
        
        List<Patient> patients = Arrays.asList(patient);
        when(patientService.getListPatients()).thenReturn(patients);

        mockMvc.perform(get("/patient/list"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("toto"))
                .andExpect(jsonPath("$[0].lastName").value("liste"));
    }
    
    @Test
    void shouldReturnPatientWhenFound() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("toto");
        patient.setLastName("recherche");

        when(patientService.getPatientById(anyInt())).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/patient/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("toto"))
                .andExpect(jsonPath("$.lastName").value("recherche"));
    }
    
    @Test
    void shouldReturn404WhenPatientNotFound() throws Exception {
        when(patientService.getPatientById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/patient/99"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
	
    @Test
    void shouldCreatePatient() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("toto");
        patient.setLastName("creation");
        patient.setDateOfBirth("10/01/2000");
        patient.setGender("M");

        when(patientService.save(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                        .andDo(print())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.firstName").value("toto"))
                        .andExpect(jsonPath("$.lastName").value("creation"));
    }

    @Test
    void shouldReturn400WhenCreatingInvalidPatient() throws Exception {
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName(""); // test @NotBlank
        invalidPatient.setLastName("");  // test @NotBlank

        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldReturn400WhenCreatingPatientWithInvalidDate() throws Exception {
        Patient invalidPatient = new Patient();
        invalidPatient.setId(1);
        invalidPatient.setFirstName("toto");
        invalidPatient.setLastName("creation");
        invalidPatient.setDateOfBirth("01-02-2000");
        invalidPatient.setGender("M");

        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }
	
    @Test
    void shouldUpdatePatientWhenFound() throws Exception {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("toto");
        patient.setLastName("update");
        patient.setDateOfBirth("10/01/2000");
        patient.setGender("M");

        when(patientService.save(any(Patient.class))).thenReturn(patient);
        when(patientService.getPatientById(any(Integer.class))).thenReturn(Optional.of(patient));

        mockMvc.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                        .andDo(print())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isAccepted())
                        .andExpect(jsonPath("$.firstName").value("toto"))
                        .andExpect(jsonPath("$.lastName").value("update"));
    }

    @Test
    void shouldReturn404WhenUpdatingUnknownPatient() throws Exception {
        Patient patient = new Patient();
        patient.setFirstName("toto");
        patient.setLastName("update");
        patient.setDateOfBirth("10/01/2000");
        patient.setGender("M");

        when(patientService.getPatientById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/patient/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                        .andDo(print())
                        .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenUpdatingWithInvalidPatient() throws Exception {
        Patient invalidPatient = new Patient();
        invalidPatient.setFirstName(""); // firstName vide -> viole @NotBlank
        invalidPatient.setLastName("");  // lastName vide -> viole @NotBlank

        mockMvc.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeletePatient() throws Exception {
        doNothing().when(patientService).deletePatient(anyInt());

        mockMvc.perform(delete("/patient/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).deletePatient(1);
    }

}
