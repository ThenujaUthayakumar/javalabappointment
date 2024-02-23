package com.javalabappointment.javalabappointment.service;

import com.javalabappointment.javalabappointment.entity.AppointmentEntity;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import com.javalabappointment.javalabappointment.persist.Appointment;
import com.javalabappointment.javalabappointment.persist.Test;
import com.javalabappointment.javalabappointment.repository.AppointmentRepository;
import com.javalabappointment.javalabappointment.repository.TestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private TestRepository testRepository;
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,TestRepository testRepository) {
        this.appointmentRepository = appointmentRepository;
        this.testRepository = testRepository;
    }

    /*------------------------- CREATE APPOINTMENT --------------------------------  */
    @Transactional
    public AppointmentEntity store(Appointment appointment) throws ParseException {
        AppointmentEntity appointmentEntity=new AppointmentEntity();

        if(appointment.getName() == null || appointment.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Name !");
        }

        if(appointment.getPhoneNumber() == null || appointment.getPhoneNumber().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Mobile Number !");
        }

        if(appointment.getAddress() == null || appointment.getAddress().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Address !");
        }

        if(appointment.getEmail() == null || appointment.getEmail().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient E-mail !");
        }

        if(appointment.getAge() == null || appointment.getAge().isEmpty())
        {
            throw new IllegalStateException("Please Enter Patient Age !");
        }

        TestEntity testEntity=testRepository.findById(appointment.getTestId().getId()).orElse(null);
        if (testEntity==null)
        {
            throw new IllegalStateException("Test Not Found !");
        }

        if(appointment.getAppointmentDateTime() == null || appointment.getAppointmentDateTime().isEmpty())
        {
            throw new IllegalStateException("Please Enter Appointment Date & Time !");
        }

        if (appointment.getGender()==null || appointment.getGender().isEmpty()){
            throw new IllegalStateException("Please Select the Gender !");
        }
        appointmentEntity.setName(appointment.getName());
        appointmentEntity.setPhoneNumber(appointment.getPhoneNumber());
        appointmentEntity.setAddress(appointment.getAddress());
        appointmentEntity.setEmail(appointment.getEmail());
        appointmentEntity.setAge(appointment.getAge());
        appointmentEntity.setTestId(appointment.getTestId());
        appointmentEntity.setAppointmentDateTime(appointment.getAppointmentDateTime());
        if (appointment.getDoctorName()!=null)
        {
            appointmentEntity.setDoctorName(appointment.getDoctorName());
        }
        appointmentEntity.setReferenceNumber(this.getReferencenumber());
        appointmentEntity.setGender(appointment.getGender());

        return appointmentRepository.save(appointmentEntity);
    }

    /*------------------------ SET AUTO REFERENCE NUMBER FOR APPOINTMENT ------------------*/
    public String getReferencenumber(){
        String referenceNumber=appointmentRepository.findReferenceNumber();
        if(referenceNumber==null){
            return "ARN1";
        }
        else{
            String[] splitString=referenceNumber.split("ARN");
            int newReferenceNumber=Integer.valueOf(splitString[1])+1;
            String finalReferenceNumber="ARN"+newReferenceNumber;
            return finalReferenceNumber;
        }
    }

    /*------------------------------ GET ALL APPOINTMENTS RECORDS --------------------------------  */
    public Page<AppointmentEntity> getAll(Integer pageNo, Integer pageSize, String orderBy, Appointment appointment) {
        Pageable pageable = null;
        List<Sort.Order> sorts = new ArrayList<>();
        if (orderBy != null) {
            String[] split = orderBy.split("&");
            for (String s : split) {
                String[] orders = s.split(",");
                sorts.add(new Sort.Order(Sort.Direction.valueOf(orders[1]), orders[0]));
            }
        }
        if (pageNo != null && pageSize != null) {
            if (orderBy != null) {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));
            } else {
                pageable = PageRequest.of(pageNo, pageSize);
            }
        } else {
            if (orderBy != null) {
                pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(sorts));
            }
        }

        String searchLike = null;
        if(appointment.getSearch() != null){
            searchLike = "%"+appointment.getSearch()+"%";
        }

        Page<AppointmentEntity> appointmentEntities;

        appointmentEntities=appointmentRepository.findAllAppointment(pageable,
                appointment.getId(),
                appointment.getName(),
                appointment.getAddress(),
                appointment.getEmail(),
                appointment.getAge(),
                appointment.getReferenceNumber(),
                appointment.getPhoneNumber(),
                appointment.getAppointmentDateTime(),
                appointment.getTestId(),
                appointment.getDoctorName(),
                searchLike,
                appointment.getGender());

        return appointmentEntities;
    }
}
