package com.telcotek.uploadservice.service;

import com.telcotek.uploadservice.model.FileDB;
import com.telcotek.uploadservice.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileDBRepository.save(FileDB);
    }

    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public FileDB changeProfilePicture(MultipartFile file, Long userId) throws IOException {
        FileDB fileDB = fileDBRepository.findProfilePic(userId);
        fileDB.setData(file.getBytes());
        fileDB.setType(file.getContentType());

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileDB.setName(fileName);

        return fileDBRepository.save(fileDB);
    }

    public FileDB getProfilePicture(Long userId) {
        return fileDBRepository.findProfilePic(userId);
    }

    public void updateFileDB(FileDB updatedFileDB) {
        // Assuming updatedFileDB has the ID of the entity you want to update
        if (updatedFileDB.getId() != null) {
            // Fetch the existing entity from the database
            FileDB existingFileDB = fileDBRepository.findById(updatedFileDB.getId()).orElse(null);

            if (existingFileDB != null) {
                // Update the properties of the existing entity
                existingFileDB.setName(updatedFileDB.getName());
                existingFileDB.setType(updatedFileDB.getType());
                existingFileDB.setData(updatedFileDB.getData());

                // Save the updated entity back to the database
                fileDBRepository.save(existingFileDB);
            }
        }
    }

}
