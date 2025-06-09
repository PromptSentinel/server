package com.example.promptsentinel.domain.csv.dao;

import com.example.promptsentinel.domain.csv.entity.CsvData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsvDataRepository extends JpaRepository<CsvData, Long> {
}
