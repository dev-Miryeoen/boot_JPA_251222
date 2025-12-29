package com.example.demo.repository;

import com.example.demo.dto.FileDTO;
import com.example.demo.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findByBno(long bno);

    List<File> findBySaveDir(String today);
}
