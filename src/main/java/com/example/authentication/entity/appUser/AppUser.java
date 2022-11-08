package com.example.authentication.entity.appUser;


import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@ToString
@Table(name="users")
@Builder
public class AppUser {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private final String email;

    @Column(name = "password")
    private final String password;

    @Column(name = "provider")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private final Provider provider = Provider.INTERNAL;

    @Column(name = " external_provider_name")
    private final String externalProviderName;

    @Column(name = "role")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private final Role role = Role.REGISTRATION_NOT_COMPLETE_USER;


}
