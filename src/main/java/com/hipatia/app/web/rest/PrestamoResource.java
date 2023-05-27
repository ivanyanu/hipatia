package com.hipatia.app.web.rest;

import com.hipatia.app.domain.Prestamo;
import com.hipatia.app.repository.PrestamoRepository;
import com.hipatia.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hipatia.app.domain.Prestamo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrestamoResource {

    private final Logger log = LoggerFactory.getLogger(PrestamoResource.class);

    private static final String ENTITY_NAME = "prestamo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrestamoRepository prestamoRepository;

    public PrestamoResource(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * {@code POST  /prestamos} : Create a new prestamo.
     *
     * @param prestamo the prestamo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestamo, or with status {@code 400 (Bad Request)} if the prestamo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prestamos")
    public ResponseEntity<Prestamo> createPrestamo(@RequestBody Prestamo prestamo) throws URISyntaxException {
        log.debug("REST request to save Prestamo : {}", prestamo);
        if (prestamo.getId() != null) {
            throw new BadRequestAlertException("A new prestamo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prestamo result = prestamoRepository.save(prestamo);
        return ResponseEntity
            .created(new URI("/api/prestamos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prestamos/:id} : Updates an existing prestamo.
     *
     * @param id the id of the prestamo to save.
     * @param prestamo the prestamo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestamo,
     * or with status {@code 400 (Bad Request)} if the prestamo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestamo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prestamos/{id}")
    public ResponseEntity<Prestamo> updatePrestamo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prestamo prestamo
    ) throws URISyntaxException {
        log.debug("REST request to update Prestamo : {}, {}", id, prestamo);
        if (prestamo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestamo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestamoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prestamo result = prestamoRepository.save(prestamo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestamo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prestamos/:id} : Partial updates given fields of an existing prestamo, field will ignore if it is null
     *
     * @param id the id of the prestamo to save.
     * @param prestamo the prestamo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestamo,
     * or with status {@code 400 (Bad Request)} if the prestamo is not valid,
     * or with status {@code 404 (Not Found)} if the prestamo is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestamo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prestamos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prestamo> partialUpdatePrestamo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prestamo prestamo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prestamo partially : {}, {}", id, prestamo);
        if (prestamo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestamo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestamoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prestamo> result = prestamoRepository
            .findById(prestamo.getId())
            .map(existingPrestamo -> {
                if (prestamo.getEstadoPrestamo() != null) {
                    existingPrestamo.setEstadoPrestamo(prestamo.getEstadoPrestamo());
                }
                if (prestamo.getFechaPrestamo() != null) {
                    existingPrestamo.setFechaPrestamo(prestamo.getFechaPrestamo());
                }
                if (prestamo.getFechaDevolucion() != null) {
                    existingPrestamo.setFechaDevolucion(prestamo.getFechaDevolucion());
                }

                return existingPrestamo;
            })
            .map(prestamoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prestamo.getId().toString())
        );
    }

    /**
     * {@code GET  /prestamos} : get all the prestamos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prestamos in body.
     */
    @GetMapping("/prestamos")
    public List<Prestamo> getAllPrestamos() {
        log.debug("REST request to get all Prestamos");
        return prestamoRepository.findAll();
    }

    /**
     * {@code GET  /prestamos/:id} : get the "id" prestamo.
     *
     * @param id the id of the prestamo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestamo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prestamos/{id}")
    public ResponseEntity<Prestamo> getPrestamo(@PathVariable Long id) {
        log.debug("REST request to get Prestamo : {}", id);
        Optional<Prestamo> prestamo = prestamoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(prestamo);
    }

    /**
     * {@code DELETE  /prestamos/:id} : delete the "id" prestamo.
     *
     * @param id the id of the prestamo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prestamos/{id}")
    public ResponseEntity<Void> deletePrestamo(@PathVariable Long id) {
        log.debug("REST request to delete Prestamo : {}", id);
        prestamoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
