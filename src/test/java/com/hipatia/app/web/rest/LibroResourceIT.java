package com.hipatia.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipatia.app.IntegrationTest;
import com.hipatia.app.domain.Libro;
import com.hipatia.app.repository.LibroRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LibroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibroResourceIT {

    private static final String DEFAULT_NOMBRE_LIBRO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_LIBRO = "BBBBBBBBBB";

    private static final String DEFAULT_ISBN = "AAAAAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_PUBLICACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PUBLICACION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/libros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibroMockMvc;

    private Libro libro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createEntity(EntityManager em) {
        Libro libro = new Libro().nombreLibro(DEFAULT_NOMBRE_LIBRO).isbn(DEFAULT_ISBN).fechaPublicacion(DEFAULT_FECHA_PUBLICACION);
        return libro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createUpdatedEntity(EntityManager em) {
        Libro libro = new Libro().nombreLibro(UPDATED_NOMBRE_LIBRO).isbn(UPDATED_ISBN).fechaPublicacion(UPDATED_FECHA_PUBLICACION);
        return libro;
    }

    @BeforeEach
    public void initTest() {
        libro = createEntity(em);
    }

    @Test
    @Transactional
    void createLibro() throws Exception {
        int databaseSizeBeforeCreate = libroRepository.findAll().size();
        // Create the Libro
        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isCreated());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeCreate + 1);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNombreLibro()).isEqualTo(DEFAULT_NOMBRE_LIBRO);
        assertThat(testLibro.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testLibro.getFechaPublicacion()).isEqualTo(DEFAULT_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void createLibroWithExistingId() throws Exception {
        // Create the Libro with an existing ID
        libro.setId(1L);

        int databaseSizeBeforeCreate = libroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreLibroIsRequired() throws Exception {
        int databaseSizeBeforeTest = libroRepository.findAll().size();
        // set the field null
        libro.setNombreLibro(null);

        // Create the Libro, which fails.

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isBadRequest());

        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsbnIsRequired() throws Exception {
        int databaseSizeBeforeTest = libroRepository.findAll().size();
        // set the field null
        libro.setIsbn(null);

        // Create the Libro, which fails.

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isBadRequest());

        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaPublicacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = libroRepository.findAll().size();
        // set the field null
        libro.setFechaPublicacion(null);

        // Create the Libro, which fails.

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isBadRequest());

        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLibros() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreLibro").value(hasItem(DEFAULT_NOMBRE_LIBRO)))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].fechaPublicacion").value(hasItem(DEFAULT_FECHA_PUBLICACION.toString())));
    }

    @Test
    @Transactional
    void getLibro() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get the libro
        restLibroMockMvc
            .perform(get(ENTITY_API_URL_ID, libro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libro.getId().intValue()))
            .andExpect(jsonPath("$.nombreLibro").value(DEFAULT_NOMBRE_LIBRO))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN))
            .andExpect(jsonPath("$.fechaPublicacion").value(DEFAULT_FECHA_PUBLICACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLibro() throws Exception {
        // Get the libro
        restLibroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLibro() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro
        Libro updatedLibro = libroRepository.findById(libro.getId()).get();
        // Disconnect from session so that the updates on updatedLibro are not directly saved in db
        em.detach(updatedLibro);
        updatedLibro.nombreLibro(UPDATED_NOMBRE_LIBRO).isbn(UPDATED_ISBN).fechaPublicacion(UPDATED_FECHA_PUBLICACION);

        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLibro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNombreLibro()).isEqualTo(UPDATED_NOMBRE_LIBRO);
        assertThat(testLibro.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testLibro.getFechaPublicacion()).isEqualTo(UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void putNonExistingLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro.nombreLibro(UPDATED_NOMBRE_LIBRO).isbn(UPDATED_ISBN);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNombreLibro()).isEqualTo(UPDATED_NOMBRE_LIBRO);
        assertThat(testLibro.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testLibro.getFechaPublicacion()).isEqualTo(DEFAULT_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void fullUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro.nombreLibro(UPDATED_NOMBRE_LIBRO).isbn(UPDATED_ISBN).fechaPublicacion(UPDATED_FECHA_PUBLICACION);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNombreLibro()).isEqualTo(UPDATED_NOMBRE_LIBRO);
        assertThat(testLibro.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testLibro.getFechaPublicacion()).isEqualTo(UPDATED_FECHA_PUBLICACION);
    }

    @Test
    @Transactional
    void patchNonExistingLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(libro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(libro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(libro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibro() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeDelete = libroRepository.findAll().size();

        // Delete the libro
        restLibroMockMvc
            .perform(delete(ENTITY_API_URL_ID, libro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
