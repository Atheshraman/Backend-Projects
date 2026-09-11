package com.example.ecom.Repo;

import com.example.ecom.model.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepo extends JpaRepository<users,Integer> {
    Optional<users> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<users> findByUsername(String username);
}
