package com.example.ecom.Repo;


import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecom.model.Ecom;

public interface Repository extends JpaRepository<Ecom,Long>{
        @Query("""
            SELECT p FROM Ecom p
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.descript) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
        Page<Ecom> Search(@Param("keyword") String keyword, Pageable pageable);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT e FROM Ecom e WHERE e.ecomId = :ecomId")
        Optional<Ecom> findIdForUpdate(@Param("ecomId") Long ecomId );
}
