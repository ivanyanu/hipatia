package com.hipatia.app.repository;

import com.hipatia.app.domain.Editorial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Editorial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {}
