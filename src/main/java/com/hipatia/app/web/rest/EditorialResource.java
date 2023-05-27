package com.hipatia.app.web.rest;

import com.hipatia.app.domain.Editorial;
import com.hipatia.app.repository.EditorialRepository;
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
 * REST controller for managing {@link com.hipatia.app.domain.Editorial}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EditorialResource {

    private final Logger log = LoggerFactory.getLogger(EditorialResource.class);

    private static final String ENTITY_NAME = "editorial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EditorialRepository editorialRepository;

    public EditorialResource(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    /**
     * {@code POST  /editorials} : Create a new editorial.
     *
     * @param editorial the editorial to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new editorial, or with status {@code 400 (Bad Request)} if the editorial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/editorials")
    public ResponseEntity<Editorial> createEditorial(@Valid @RequestBody Editorial editorial) throws URISyntaxException {
        log.debug("REST request to save Editorial : {}", editorial);
        if (editorial.getId() != null) {
            throw new BadRequestAlertException("A new editorial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Editorial result = editorialRepository.save(editorial);
        return ResponseEntity
            .created(new URI("/api/editorials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /editorials/:id} : Updates an existing editorial.
     *
     * @param id the id of the editorial to save.
     * @param editorial the editorial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editorial,
     * or with status {@code 400 (Bad Request)} if the editorial is not valid,
     * or with status {@code 500 (Internal Server Error)} if the editorial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/editorials/{id}")
    public ResponseEntity<Editorial> updateEditorial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Editorial editorial
    ) throws URISyntaxException {
        log.debug("REST request to update Editorial : {}, {}", id, editorial);
        if (editorial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editorial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editorialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Editorial result = editorialRepository.save(editorial);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editorial.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /editorials/:id} : Partial updates given fields of an existing editorial, field will ignore if it is null
     *
     * @param id the id of the editorial to save.
     * @param editorial the editorial to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editorial,
     * or with status {@code 400 (Bad Request)} if the editorial is not valid,
     * or with status {@code 404 (Not Found)} if the editorial is not found,
     * or with status {@code 500 (Internal Server Error)} if the editorial couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/editorials/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Editorial> partialUpdateEditorial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Editorial editorial
    ) throws URISyntaxException {
        log.debug("REST request to partial update Editorial partially : {}, {}", id, editorial);
        if (editorial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editorial.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editorialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Editorial> result = editorialRepository
            .findById(editorial.getId())
            .map(existingEditorial -> {
                if (editorial.getNombreEditorial() != null) {
                    existingEditorial.setNombreEditorial(editorial.getNombreEditorial());
                }
                if (editorial.getCantidadTitulos() != null) {
                    existingEditorial.setCantidadTitulos(editorial.getCantidadTitulos());
                }

                return existingEditorial;
            })
            .map(editorialRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editorial.getId().toString())
        );
    }

    /**
     * {@code GET  /editorials} : get all the editorials.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of editorials in body.
     */
    @GetMapping("/editorials")
    public List<Editorial> getAllEditorials() {
        log.debug("REST request to get all Editorials");
        return editorialRepository.findAll();
    }

    /**
     * {@code GET  /editorials/:id} : get the "id" editorial.
     *
     * @param id the id of the editorial to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the editorial, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/editorials/{id}")
    public ResponseEntity<Editorial> getEditorial(@PathVariable Long id) {
        log.debug("REST request to get Editorial : {}", id);
        Optional<Editorial> editorial = editorialRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(editorial);
    }

    /**
     * {@code DELETE  /editorials/:id} : delete the "id" editorial.
     *
     * @param id the id of the editorial to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/editorials/{id}")
    public ResponseEntity<Void> deleteEditorial(@PathVariable Long id) {
        log.debug("REST request to delete Editorial : {}", id);
        editorialRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
