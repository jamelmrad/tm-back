package com.telcotek.uploadservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_files")
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    private Long user_id;

    private Boolean profile_picture;

    @Lob
    private byte[] data;

    public FileDB(String name, String type,Long userId, Boolean pic, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.user_id = userId;
        this.profile_picture = Boolean.TRUE;
    }

    public FileDB(String name, String type,byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
