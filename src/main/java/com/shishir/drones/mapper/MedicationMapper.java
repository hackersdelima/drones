package com.shishir.drones.mapper;

import com.shishir.drones.dto.MedicationRequest;
import com.shishir.drones.dto.MedicationResponse;
import com.shishir.drones.entity.Medication;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MedicationMapper {
    public Medication toMedication(MedicationRequest medicationRequest) {
        Medication medication = new Medication();
        BeanUtils.copyProperties(medicationRequest, medication);
        return medication;
    }

    public List<Medication> toMedications(List<MedicationRequest> medicationRequests) {
        List<Medication> medications = new ArrayList<>();
        medicationRequests.forEach(medicationRequest -> medications.add(this.toMedication(medicationRequest)));
        return medications;
    }

    public MedicationResponse toMedicationResponse(Medication medication){
        MedicationResponse medicationResponse = new MedicationResponse();
        BeanUtils.copyProperties(medication,medicationResponse);
        return medicationResponse;
    }

    public List<MedicationResponse> toMedicationResponses(List<Medication> medications){
        List<MedicationResponse> medicationResponses = new ArrayList<>();
        medications.forEach(medication -> medicationResponses.add(this.toMedicationResponse(medication)));
        return medicationResponses;
    }
}
