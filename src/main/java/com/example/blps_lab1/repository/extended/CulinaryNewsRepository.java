package com.example.blps_lab1.repository.extended;

import com.example.blps_lab1.model.extended.CulinaryNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CulinaryNewsRepository extends JpaRepository<CulinaryNews, Long> {

}
