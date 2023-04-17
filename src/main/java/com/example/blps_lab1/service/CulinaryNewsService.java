package com.example.blps_lab1.service;

import com.example.blps_lab1.dto.request.AddCulinaryNewRequest;
import com.example.blps_lab1.exception.ResourceNotFoundException;
import com.example.blps_lab1.model.extended.CulinaryNews;
import com.example.blps_lab1.repository.extended.CulinaryNewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class CulinaryNewsService {
    private final CulinaryNewsRepository culinaryNewsRepository;

    private final UserService userService;


    public CulinaryNewsService(CulinaryNewsRepository culinaryNewsRepository, UserService userService) {
        this.culinaryNewsRepository = culinaryNewsRepository;
        this.userService = userService;
    }


    @Transactional(transactionManager = "atomikosTransactionManager")
    public CulinaryNews saveCulinaryNew(String user_login, AddCulinaryNewRequest addCulinaryNewRequest) {
        CulinaryNews culinaryNews = new CulinaryNews(addCulinaryNewRequest.getName(), user_login, addCulinaryNewRequest.getDescription(), new Timestamp(System.currentTimeMillis()));
        culinaryNewsRepository.save(culinaryNews);
        userService.incrementCulinaryNewsCounter(user_login);
        return culinaryNews;
    }

    public Page<CulinaryNews> getAllCulinaryNews(int page, int size, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "publication_date"));
        return culinaryNewsRepository.findAll(pageable);
    }

    public CulinaryNews findCulinaryNewById(Long id) {
        return culinaryNewsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Кулинарная новость с номером " + id + " не найдена в базе!"));
    }

}
