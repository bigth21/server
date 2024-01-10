package com.example.server.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;
    
    @Column(nullable = false, columnDefinition = "varchar(50) comment 'Email'")
    private String username;
    
    @Column // TODO: Check size after encryption.
    private String password;
}
