package com.hipatia.app.repository;

import com.hipatia.app.domain.Autor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AutorRepositoryWithBagRelationshipsImpl implements AutorRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Autor> fetchBagRelationships(Optional<Autor> autor) {
        return autor.map(this::fetchLibros);
    }

    @Override
    public Page<Autor> fetchBagRelationships(Page<Autor> autors) {
        return new PageImpl<>(fetchBagRelationships(autors.getContent()), autors.getPageable(), autors.getTotalElements());
    }

    @Override
    public List<Autor> fetchBagRelationships(List<Autor> autors) {
        return Optional.of(autors).map(this::fetchLibros).orElse(Collections.emptyList());
    }

    Autor fetchLibros(Autor result) {
        return entityManager
            .createQuery("select autor from Autor autor left join fetch autor.libros where autor is :autor", Autor.class)
            .setParameter("autor", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Autor> fetchLibros(List<Autor> autors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, autors.size()).forEach(index -> order.put(autors.get(index).getId(), index));
        List<Autor> result = entityManager
            .createQuery("select distinct autor from Autor autor left join fetch autor.libros where autor in :autors", Autor.class)
            .setParameter("autors", autors)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
