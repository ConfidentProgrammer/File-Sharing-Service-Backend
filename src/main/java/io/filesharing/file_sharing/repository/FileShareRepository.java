package io.filesharing.file_sharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.filesharing.file_sharing.model.File;

public interface FileShareRepository extends JpaRepository<File, Long> {

    boolean existsByRandomId(String randomId);

}