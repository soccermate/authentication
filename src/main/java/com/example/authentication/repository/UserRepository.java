package com.example.authentication.repository;

import com.example.authentication.entity.appUser.AppUser;
import com.example.authentication.entity.appUser.Provider;
import com.example.authentication.entity.appUser.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends CrudRepository<AppUser,Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO Users (role, provider, external_provider_name) "
            + "VALUES (:role, :provider, :external_provider_name)", nativeQuery = true)
    public void saveExternalProviderUser(@Param("role") String role,
                                         @Param("provider") String provider,
                                         @Param("external_provider_name") String external_provider_name);


    public Optional<AppUser> findByExternalProviderNameAndProvider(String external_provider_name, Provider provider);

    public Optional<AppUser> findByEmail(String email);

    @Query("SELECT CASE WHEN (COUNT(*) >= 1) THEN true ELSE false END FROM AppUser u WHERE u.email = :email")
    public boolean existByEmail(@Param("email") String email);

    @Query(value = "UPDATE AppUser u SET u.password = :password WHERE u.email = :email")
    @Modifying(clearAutomatically = true)
    @Transactional
    public void changePassword(@Param("email") String email, @Param("password") String password);

    @Query(value = "UPDATE AppUser u SET u.role = :role WHERE u.id = :id")
    @Modifying(clearAutomatically = true)
    @Transactional
    public void changeUserRole(@Param("id") Long id, @Param("role") Role role);


}
