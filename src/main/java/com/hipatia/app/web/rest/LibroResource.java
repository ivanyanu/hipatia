package com.hipatia.app.web.rest;

import com.hipatia.app.domain.Libro;
import com.hipatia.app.repository.LibroRepository;
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
 * REST controller for managing {@link com.hipatia.app.domain.Libro}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LibroResource {

    private final Logger log = LoggerFactory.getLogger(LibroResource.class);

    private static final String ENTITY_NAME = "libro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LibroRepository libroRepository;

    public LibroResource(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    /**
     * {@code POST  /libros} : Create a new libro.
     *
     * @param libro the libro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libro, or with status {@code 400 (Bad Request)} if the libro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/libros")
    public ResponseEntity<Libro> createLibro(@Valid @RequestBody Libro libro) throws URISyntaxException {
        log.debug("REST request to save Libro : {}", libro);
        if (libro.getId() != null) {
            throw new BadRequestAlertException("A new libro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Libro result = libroRepository.save(libro);
        return ResponseEntity
            .created(new URI("/api/libros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /libros/:id} : Updates an existing libro.
     *
     * @param id the id of the libro to save.
     * @param libro the libro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libro,
     * or with status {@code 400 (Bad Request)} if the libro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/libros/{id}")
    public ResponseEntity<Libro> updateLibro(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Libro libro)
        throws URISyntaxException {
        log.debug("REST request to update Libro : {}, {}", id, libro);
        if (libro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Libro result = libroRepository.save(libro);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libro.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /libros/:id} : Partial updates given fields of an existing libro, field will ignore if it is null
     *
     * @param id the id of the libro to save.
     * @param libro the libro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libro,
     * or with status {@code 400 (Bad Request)} if the libro is not valid,
     * or with status {@code 404 (Not Found)} if the libro is not found,
     * or with status {@code 500 (Internal Server Error)} if the libro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/libros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Libro> partialUpdateLibro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Libro libro
    ) throws URISyntaxException {
        log.debug("REST request to partial update Libro partially : {}, {}", id, libro);
        if (libro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Libro> result = libroRepository
            .findById(libro.getId())
            .map(existingLibro -> {
                if (libro.getNombreLibro() != null) {
                    existingLibro.setNombreLibro(libro.getNombreLibro());
                }
                if (libro.getIsbn() != null) {
                    existingLibro.setIsbn(libro.getIsbn());
                }
                if (libro.getFechaPublicacion() != null) {
                    existingLibro.setFechaPublicacion(libro.getFechaPublicacion());
                }

                return existingLibro;
            })
            .map(libroRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libro.getId().toString())
        );
    }

    /**
     * {@code GET  /libros} : get all the libros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of libros in body.
     */
    @GetMapping("/libros")
    public List<Libro> getAllLibros() {
        log.debug("REST request to get all Libros");
        return libroRepository.findAll();
    }

    /**
     * {@code GET  /libros/:id} : get the "id" libro.
     *
     * @param id the id of the libro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> getLibro(@PathVariable Long id) {
        log.debug("REST request to get Libro : {}", id);
        Optional<Libro> libro = libroRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(libro);
    }

    /**
     * {@code DELETE  /libros/:id} : delete the "id" libro.
     *
     * @param id the id of the libro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/libros/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        log.debug("REST request to delete Libro : {}", id);
        libroRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
