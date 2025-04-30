package io.filesharing.file_sharing.repository;

import java.io.File;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileShareRepository extends JpaRepository<File, Long> {

}