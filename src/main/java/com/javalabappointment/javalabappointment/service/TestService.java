package com.javalabappointment.javalabappointment.service;

import com.javalabappointment.javalabappointment.entity.TestEntity;
import com.javalabappointment.javalabappointment.persist.Test;
import com.javalabappointment.javalabappointment.repository.TestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private TestRepository testRepository;
    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    /*------------------------- CREATE TEST LISTS --------------------------------  */
    @Transactional
    public TestEntity store(Test test) {
        TestEntity testEntity=new TestEntity();

        if(test.getName() == null || test.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Test Name !");
        }

        if(test.getDescription() == null || test.getDescription().isEmpty())
        {
            throw new IllegalStateException("Please Enter Test Description !");
        }

        if(test.getCost() == null)
        {
            throw new IllegalStateException("Please Enter Test Amount !");
        }

        testEntity.setName(test.getName());
        testEntity.setDescription(test.getDescription());
        testEntity.setCost(test.getCost());

        return testRepository.save(testEntity);
    }

    /*------------------------------ GET ALL TEST LISTS --------------------------------  */
    public Page<TestEntity> getAllTest (Integer pageNo,Integer pageSize,String orderBy,Test test) {
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
        if(test.getSearch() != null){
            searchLike = "%"+test.getSearch()+"%";
        }

        Page<TestEntity> testEntities;

        testEntities=testRepository.findAllTest(pageable,
                                                test.getId(),
                                                test.getName(),
                                                test.getDescription(),
                                                test.getCost(),
                                                searchLike);

        return testEntities;
    }

    /*------------------------------ UPDATE TEST LISTS --------------------------------  */
    @Transactional
    public TestEntity update(Test test) {
        TestEntity testEntity=testRepository.findById(test.getId()).orElseThrow(()->new IllegalStateException(
                "Test With Id"+test.getId()+"Doesn't Exist !"
        ));

        if(test.getName() == null || test.getName().isEmpty())
        {
            throw new IllegalStateException("Please Enter Test Name !");
        }

        if(test.getDescription() == null || test.getDescription().isEmpty())
        {
            throw new IllegalStateException("Please Enter Test Description !");
        }

        if(test.getCost() == null)
        {
            throw new IllegalStateException("Please Enter Test Amount !");
        }

        testEntity.setName(test.getName());
        testEntity.setDescription(test.getDescription());
        testEntity.setCost(test.getCost());

        return testRepository.save(testEntity);
    }

}
