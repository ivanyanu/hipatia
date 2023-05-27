package com.hipatia.app.web.rest;

import com.hipatia.app.domain.Autor;
import com.hipatia.app.repository.AutorRepository;
import com.hipatia.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hipatia.app.domain.Autor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AutorResource {

    private final Logger log = LoggerFactory.getLogger(AutorResource.class);

    private static final String ENTITY_NAME = "autor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutorRepository autorRepository;

    public AutorResource(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    /**
     * {@code POST  /autors} : Create a new autor.
     *
     * @param autor the autor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autor, or with status {@code 400 (Bad Request)} if the autor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/autors")
    public ResponseEntity<Autor> createAutor(@Valid @RequestBody Autor autor) throws URISyntaxException {
        log.debug("REST request to save Autor : {}", autor);
        if (autor.getId() != null) {
            throw new BadRequestAlertException("A new autor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Autor result = autorRepository.save(autor);
        return ResponseEntity
            .created(new URI("/api/autors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /autors/:id} : Updates an existing autor.
     *
     * @param id the id of the autor to save.
     * @param autor the autor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autor,
     * or with status {@code 400 (Bad Request)} if the autor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/autors/{id}")
    public ResponseEntity<Autor> updateAutor(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Autor autor)
        throws URISyntaxException {
        log.debug("REST request to update Autor : {}, {}", id, autor);
        if (autor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Autor result = autorRepository.save(autor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /autors/:id} : Partial updates given fields of an existing autor, field will ignore if it is null
     *
     * @param id the id of the autor to save.
     * @param autor the autor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autor,
     * or with status {@code 400 (Bad Request)} if the autor is not valid,
     * or with status {@code 404 (Not Found)} if the autor is not found,
     * or with status {@code 500 (Internal Server Error)} if the autor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/autors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Autor> partialUpdateAutor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Autor autor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Autor partially : {}, {}", id, autor);
        if (autor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Autor> result = autorRepository
            .findById(autor.getId())
            .map(existingAutor -> {
                if (autor.getNombreAutor() != null) {
                    existingAutor.setNombreAutor(autor.getNombreAutor());
                }
                if (autor.getApellidoAutor() != null) {
                    existingAutor.setApellidoAutor(autor.getApellidoAutor());
                }
                if (autor.getOrigenAutor() != null) {
                    existingAutor.setOrigenAutor(autor.getOrigenAutor());
                }

                return existingAutor;
            })
            .map(autorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autor.getId().toString())
        );
    }

    /**
     * {@code GET  /autors} : get all the autors.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autors in body.
     */
    @GetMapping("/autors")
    public List<Autor> getAllAutors(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Autors");
        if (eagerload) {
            return autorRepository.findAllWithEagerRelationships();
        } else {
            return autorRepository.findAll();
        }
    }

    /**
     * {@code GET  /autors/:id} : get the "id" autor.
     *
     * @param id the id of the autor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/autors/{id}")
    public ResponseEntity<Autor> getAutor(@PathVariable Long id) {
        log.debug("REST request to get Autor : {}", id);
        Optional<Autor> autor = autorRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(autor);
    }

    /**
     * {@code DELETE  /autors/:id} : delete the "id" autor.
     *
     * @param id the id of the autor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/autors/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        log.debug("REST request to delete Autor : {}", id);
        autorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
