package com.telcotek.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "member_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "t_users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long Id;
    private String firstname;
    private String lastname;
    private String email;
   // @JsonIgnore
    private String password;
    private String phoneNumber;
    private Boolean available;
    private Boolean connected;
    private Boolean emailVerified;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String firstname, String lastname, String email,String phone, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phone;
        this.password = password;
        this.available = Boolean.FALSE;
        this.connected = Boolean.FALSE;
        this.emailVerified = Boolean.FALSE;
    }
}
