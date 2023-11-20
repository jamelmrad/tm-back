package com.telcotek.uploadservice.repository;

import com.telcotek.uploadservice.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

    @Query(value = "SELECT * FROM t_files WHERE user_id = :userId AND profile_picture = 1", nativeQuery = true)
    FileDB findProfilePic(@Param("userId")Long id);

}
