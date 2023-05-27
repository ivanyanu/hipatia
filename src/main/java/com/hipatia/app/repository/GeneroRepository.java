package com.hipatia.app.repository;

import com.hipatia.app.domain.Genero;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Genero entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {}
