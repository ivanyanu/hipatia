package com.hipatia.app.repository;

import com.hipatia.app.domain.Prestamo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Prestamo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {}
