package com.hipatia.app.repository;

import com.hipatia.app.domain.Autor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AutorRepositoryWithBagRelationships {
    Optional<Autor> fetchBagRelationships(Optional<Autor> autor);

    List<Autor> fetchBagRelationships(List<Autor> autors);

    Page<Autor> fetchBagRelationships(Page<Autor> autors);
}
