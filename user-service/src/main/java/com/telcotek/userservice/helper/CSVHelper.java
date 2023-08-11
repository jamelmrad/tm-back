package com.telcotek.userservice.helper;

import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {

    public static final String TYPE = "text/csv";

    private CSVHelper() {
        throw new IllegalStateException("This class helps to determine whether the file is in csv format or not");
    }

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
}
