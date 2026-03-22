package com.medilab.infospatients.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medilab.infospatients.entitys.Patient;
import com.medilab.infospatients.repositorys.PatientRepository;

@Service
public class PatientService {
    
    private PatientRepository patientRepository;
    
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }
    
    public Iterable<Patient> getListPatients(){
        return patientRepository.findAll();
    }
    
    public Optional<Patient> getPatientById(Integer id){
        return patientRepository.findById(id);
    }
    
    public void deletePatient(Integer id) {
        patientRepository.deleteById(id);
    }
}
