package com.hipatia.app.web.rest;

import com.hipatia.app.domain.Ejemplar;
import com.hipatia.app.repository.EjemplarRepository;
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
 * REST controller for managing {@link com.hipatia.app.domain.Ejemplar}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EjemplarResource {

    private final Logger log = LoggerFactory.getLogger(EjemplarResource.class);

    private static final String ENTITY_NAME = "ejemplar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EjemplarRepository ejemplarRepository;

    public EjemplarResource(EjemplarRepository ejemplarRepository) {
        this.ejemplarRepository = ejemplarRepository;
    }

    /**
     * {@code POST  /ejemplars} : Create a new ejemplar.
     *
     * @param ejemplar the ejemplar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ejemplar, or with status {@code 400 (Bad Request)} if the ejemplar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ejemplars")
    public ResponseEntity<Ejemplar> createEjemplar(@Valid @RequestBody Ejemplar ejemplar) throws URISyntaxException {
        log.debug("REST request to save Ejemplar : {}", ejemplar);
        if (ejemplar.getId() != null) {
            throw new BadRequestAlertException("A new ejemplar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ejemplar result = ejemplarRepository.save(ejemplar);
        return ResponseEntity
            .created(new URI("/api/ejemplars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ejemplars/:id} : Updates an existing ejemplar.
     *
     * @param id the id of the ejemplar to save.
     * @param ejemplar the ejemplar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ejemplar,
     * or with status {@code 400 (Bad Request)} if the ejemplar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ejemplar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ejemplars/{id}")
    public ResponseEntity<Ejemplar> updateEjemplar(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ejemplar ejemplar
    ) throws URISyntaxException {
        log.debug("REST request to update Ejemplar : {}, {}", id, ejemplar);
        if (ejemplar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ejemplar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ejemplarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ejemplar result = ejemplarRepository.save(ejemplar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ejemplar.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ejemplars/:id} : Partial updates given fields of an existing ejemplar, field will ignore if it is null
     *
     * @param id the id of the ejemplar to save.
     * @param ejemplar the ejemplar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ejemplar,
     * or with status {@code 400 (Bad Request)} if the ejemplar is not valid,
     * or with status {@code 404 (Not Found)} if the ejemplar is not found,
     * or with status {@code 500 (Internal Server Error)} if the ejemplar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ejemplars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ejemplar> partialUpdateEjemplar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ejemplar ejemplar
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ejemplar partially : {}, {}", id, ejemplar);
        if (ejemplar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ejemplar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ejemplarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ejemplar> result = ejemplarRepository
            .findById(ejemplar.getId())
            .map(existingEjemplar -> {
                if (ejemplar.getEstadoEjemplar() != null) {
                    existingEjemplar.setEstadoEjemplar(ejemplar.getEstadoEjemplar());
                }
                if (ejemplar.getFechaAltaEjemplar() != null) {
                    existingEjemplar.setFechaAltaEjemplar(ejemplar.getFechaAltaEjemplar());
                }

                return existingEjemplar;
            })
            .map(ejemplarRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ejemplar.getId().toString())
        );
    }

    /**
     * {@code GET  /ejemplars} : get all the ejemplars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ejemplars in body.
     */
    @GetMapping("/ejemplars")
    public List<Ejemplar> getAllEjemplars() {
        log.debug("REST request to get all Ejemplars");
        return ejemplarRepository.findAll();
    }

    /**
     * {@code GET  /ejemplars/:id} : get the "id" ejemplar.
     *
     * @param id the id of the ejemplar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ejemplar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ejemplars/{id}")
    public ResponseEntity<Ejemplar> getEjemplar(@PathVariable Long id) {
        log.debug("REST request to get Ejemplar : {}", id);
        Optional<Ejemplar> ejemplar = ejemplarRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ejemplar);
    }

    /**
     * {@code DELETE  /ejemplars/:id} : delete the "id" ejemplar.
     *
     * @param id the id of the ejemplar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ejemplars/{id}")
    public ResponseEntity<Void> deleteEjemplar(@PathVariable Long id) {
        log.debug("REST request to delete Ejemplar : {}", id);
        ejemplarRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
