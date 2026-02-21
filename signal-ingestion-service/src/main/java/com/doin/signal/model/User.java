package com.doin.signal.model;

import com.doin.signal.constant.db.DbConstant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = DbConstant.DbUser.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @Builder.Default
    private String role = "USER";

    @Column(name = DbConstant.DbCommon.CREATED_AT)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}