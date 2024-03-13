package com.javalabappointment.javalabappointment.service;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.UnitValue;
import com.javalabappointment.javalabappointment.entity.TestEntity;
import com.javalabappointment.javalabappointment.persist.Test;
import com.javalabappointment.javalabappointment.repository.TestRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        testEntity.setCode(this.getTestCode());

        return testRepository.save(testEntity);
    }

    /*------------------------------------ TEST CODE --------------------------*/
    public String getTestCode(){
        String testCode=testRepository.findTestCode();
        if(testCode==null){
            return "TEST1";
        }
        else{
            String[] splitString=testCode.split("TEST");
            int newReferenceNumber=Integer.valueOf(splitString[1])+1;
            String finalReferenceNumber="TEST"+newReferenceNumber;
            return finalReferenceNumber;
        }
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

    /*------------------------------ DOWNLOAD TEST LISTS --------------------------------  */
    public void download(Integer pageNo, Integer pageSize, String orderBy,Test test,Integer fileType, String downloadColumn, HttpServletResponse response) throws IOException {
        Page<TestEntity> list = getAllTest(pageNo, pageSize, orderBy,test);
        String[] requiredColumns;
        if (downloadColumn != null) {
            requiredColumns = downloadColumn.split(",");
        } else {
            requiredColumns = new String[]{"id","name","description","cost"};
        }

        List<String> columnNames = new ArrayList<>();

        for (String columns : requiredColumns) {
            if (columns.matches("id")) {
                columnNames.add("Test No");
            }
            if (columns.matches("name")) {
                columnNames.add("Test Name");
            }
            if (columns.matches("description")) {
                columnNames.add("Test Description");
            }
            if (columns.matches("cost")) {
                columnNames.add("Test Cost");
            }
        }
        if (fileType == 1) {

            response.setHeader("Content-Disposition", "attachment; filename=test.pdf");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDocument;
            pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4.rotate());

            try {
                float[] colWidths = new float[requiredColumns.length];
                for (int i = 0; i < requiredColumns.length; i++) {
                    colWidths[i] = 10f;
                }

                com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table((UnitValue.createPercentArray(colWidths)));
                table.setFontSize(10);

                //Headed
                for (String col : columnNames) {
                    table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(col).setBold()));
                }

                //Set data
                for (TestEntity emp : list) {
                    for (String col : requiredColumns) {
                        if (col.matches("id")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getId() != null ? emp.getId().toString() : "")));
                        }
                        if (col.matches("name")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getName() != null ? emp.getName() : "")));
                        }
                        if (col.matches("description")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getDescription() != null ? emp.getDescription() : "")));
                        }
                        if (col.matches("cost")) {
                            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(emp.getCost() != null ? emp.getCost().toString() : "")));
                        }
                    }
                }
                document.add(table);
                document.close();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*-------------------------------- DELETE API----------------------------------- */
    public ResponseEntity<String> delete(Integer id)
    {
        testRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully !");
    }

    public Page<Map<Object,String>> getStatistics (Integer pageNo, Integer pageSize, String orderBy, Test test) {
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

        Page<Map<Object,String>>  testEntities;

        testEntities=testRepository.findStatistics(pageable,
                test.getId(),
                test.getName(),
                test.getDescription(),
                test.getCost(),
                searchLike);

        return testEntities;
    }
}
