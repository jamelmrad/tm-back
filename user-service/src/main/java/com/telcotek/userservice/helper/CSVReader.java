package com.telcotek.userservice.helper;

import com.opencsv.exceptions.CsvException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CSVReader {

    private CSVReader() {
        throw new IllegalStateException("This class reads csv files");
    }

    public static List<String[]> readCsv(MultipartFile file) throws IOException, CsvException {
        try (com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(new InputStreamReader(file.getInputStream()))) {
            return csvReader.readAll();
        }
    }
}
