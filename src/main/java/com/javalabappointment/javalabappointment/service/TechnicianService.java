package com.javalabappointment.javalabappointment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javalabappointment.javalabappointment.entity.TechnicianEntity;
import com.javalabappointment.javalabappointment.persist.Technician;
import com.javalabappointment.javalabappointment.repository.TechnicianRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TechnicianService {
    private TechnicianRepository technicianRepository;

    @Autowired
    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    /*------------------------- CREATE TECHNICIAN --------------------------------*/
    @Transactional
    public TechnicianEntity store(Technician technician, MultipartFile file) {
        if (technician.getName() == null || technician.getName().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Name !");
        }

        if (technician.getAddress() == null || technician.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Address !");
        }

        if (technician.getEmail() == null || technician.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician E-mail !");
        }

        if (technician.getPhoneNumber() == null || technician.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Mobile Number !");
        }

        if (technician.getPosition() == null || technician.getPosition().isEmpty()) {
            throw new IllegalArgumentException("Please Enter Technician Position !");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please Upload Technician Image !");
        }

        /*--------------------- IMAGE SAVING IN DATABASE --------------------*/
        String uploadDirectory = "src/main/resources/static/img/technician";

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);
            String relativeFilePath = Paths.get(fileName).toString();

            Map<String, String> imagePath = new HashMap<>();
            imagePath.put("filePath", relativeFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            String imagePathJson = objectMapper.writeValueAsString(imagePath);

            TechnicianEntity technicianEntity = new TechnicianEntity();
            technicianEntity.setName(technician.getName());
            technicianEntity.setImage(imagePathJson);

            return technicianRepository.save(technicianEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }

    }

    /*------------------------------------  GET ALL TECHNICIANS --------------------------*/
    public Page<TechnicianEntity> getAll(Integer pageNo, Integer pageSize, String orderBy, Technician technician) {
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
        if(technician.getSearch() != null){
            searchLike = "%"+technician.getSearch()+"%";
        }

        Page<TechnicianEntity> technicianEntities;

        technicianEntities=technicianRepository.findAllTechnician(pageable,
                technician.getId(),
                technician.getName(),
                technician.getAddress(),
                technician.getPhoneNumber(),
                searchLike);

        return technicianEntities;
    }
}
