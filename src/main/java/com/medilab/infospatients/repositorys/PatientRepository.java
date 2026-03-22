package com.medilab.infospatients.repositorys;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.medilab.infospatients.entitys.Patient;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {

}
