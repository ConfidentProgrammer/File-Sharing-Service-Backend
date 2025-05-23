package io.filesharing.file_sharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.filesharing.file_sharing.model.File;

@Repository
public interface FileShareRepository extends JpaRepository<File, String> {

}