package com.hipatia.app.web.rest;

import com.hipatia.app.domain.Genero;
import com.hipatia.app.repository.GeneroRepository;
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
 * REST controller for managing {@link com.hipatia.app.domain.Genero}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GeneroResource {

    private final Logger log = LoggerFactory.getLogger(GeneroResource.class);

    private static final String ENTITY_NAME = "genero";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeneroRepository generoRepository;

    public GeneroResource(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    /**
     * {@code POST  /generos} : Create a new genero.
     *
     * @param genero the genero to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genero, or with status {@code 400 (Bad Request)} if the genero has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generos")
    public ResponseEntity<Genero> createGenero(@Valid @RequestBody Genero genero) throws URISyntaxException {
        log.debug("REST request to save Genero : {}", genero);
        if (genero.getId() != null) {
            throw new BadRequestAlertException("A new genero cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Genero result = generoRepository.save(genero);
        return ResponseEntity
            .created(new URI("/api/generos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generos/:id} : Updates an existing genero.
     *
     * @param id the id of the genero to save.
     * @param genero the genero to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genero,
     * or with status {@code 400 (Bad Request)} if the genero is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genero couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generos/{id}")
    public ResponseEntity<Genero> updateGenero(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Genero genero
    ) throws URISyntaxException {
        log.debug("REST request to update Genero : {}, {}", id, genero);
        if (genero.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genero.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Genero result = generoRepository.save(genero);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genero.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /generos/:id} : Partial updates given fields of an existing genero, field will ignore if it is null
     *
     * @param id the id of the genero to save.
     * @param genero the genero to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genero,
     * or with status {@code 400 (Bad Request)} if the genero is not valid,
     * or with status {@code 404 (Not Found)} if the genero is not found,
     * or with status {@code 500 (Internal Server Error)} if the genero couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Genero> partialUpdateGenero(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Genero genero
    ) throws URISyntaxException {
        log.debug("REST request to partial update Genero partially : {}, {}", id, genero);
        if (genero.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genero.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Genero> result = generoRepository
            .findById(genero.getId())
            .map(existingGenero -> {
                if (genero.getNombreGenero() != null) {
                    existingGenero.setNombreGenero(genero.getNombreGenero());
                }
                if (genero.getDescripcionGenero() != null) {
                    existingGenero.setDescripcionGenero(genero.getDescripcionGenero());
                }

                return existingGenero;
            })
            .map(generoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genero.getId().toString())
        );
    }

    /**
     * {@code GET  /generos} : get all the generos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of generos in body.
     */
    @GetMapping("/generos")
    public List<Genero> getAllGeneros() {
        log.debug("REST request to get all Generos");
        return generoRepository.findAll();
    }

    /**
     * {@code GET  /generos/:id} : get the "id" genero.
     *
     * @param id the id of the genero to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genero, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generos/{id}")
    public ResponseEntity<Genero> getGenero(@PathVariable Long id) {
        log.debug("REST request to get Genero : {}", id);
        Optional<Genero> genero = generoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(genero);
    }

    /**
     * {@code DELETE  /generos/:id} : delete the "id" genero.
     *
     * @param id the id of the genero to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generos/{id}")
    public ResponseEntity<Void> deleteGenero(@PathVariable Long id) {
        log.debug("REST request to delete Genero : {}", id);
        generoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
