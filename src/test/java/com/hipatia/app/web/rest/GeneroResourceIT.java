package com.hipatia.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipatia.app.IntegrationTest;
import com.hipatia.app.domain.Genero;
import com.hipatia.app.repository.GeneroRepository;
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
 * Integration tests for the {@link GeneroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneroResourceIT {

    private static final String DEFAULT_NOMBRE_GENERO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_GENERO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION_GENERO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_GENERO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/generos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneroMockMvc;

    private Genero genero;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genero createEntity(EntityManager em) {
        Genero genero = new Genero().nombreGenero(DEFAULT_NOMBRE_GENERO).descripcionGenero(DEFAULT_DESCRIPCION_GENERO);
        return genero;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genero createUpdatedEntity(EntityManager em) {
        Genero genero = new Genero().nombreGenero(UPDATED_NOMBRE_GENERO).descripcionGenero(UPDATED_DESCRIPCION_GENERO);
        return genero;
    }

    @BeforeEach
    public void initTest() {
        genero = createEntity(em);
    }

    @Test
    @Transactional
    void createGenero() throws Exception {
        int databaseSizeBeforeCreate = generoRepository.findAll().size();
        // Create the Genero
        restGeneroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genero)))
            .andExpect(status().isCreated());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeCreate + 1);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombreGenero()).isEqualTo(DEFAULT_NOMBRE_GENERO);
        assertThat(testGenero.getDescripcionGenero()).isEqualTo(DEFAULT_DESCRIPCION_GENERO);
    }

    @Test
    @Transactional
    void createGeneroWithExistingId() throws Exception {
        // Create the Genero with an existing ID
        genero.setId(1L);

        int databaseSizeBeforeCreate = generoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genero)))
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreGeneroIsRequired() throws Exception {
        int databaseSizeBeforeTest = generoRepository.findAll().size();
        // set the field null
        genero.setNombreGenero(null);

        // Create the Genero, which fails.

        restGeneroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genero)))
            .andExpect(status().isBadRequest());

        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGeneros() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get all the generoList
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genero.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreGenero").value(hasItem(DEFAULT_NOMBRE_GENERO)))
            .andExpect(jsonPath("$.[*].descripcionGenero").value(hasItem(DEFAULT_DESCRIPCION_GENERO)));
    }

    @Test
    @Transactional
    void getGenero() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        // Get the genero
        restGeneroMockMvc
            .perform(get(ENTITY_API_URL_ID, genero.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genero.getId().intValue()))
            .andExpect(jsonPath("$.nombreGenero").value(DEFAULT_NOMBRE_GENERO))
            .andExpect(jsonPath("$.descripcionGenero").value(DEFAULT_DESCRIPCION_GENERO));
    }

    @Test
    @Transactional
    void getNonExistingGenero() throws Exception {
        // Get the genero
        restGeneroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGenero() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeUpdate = generoRepository.findAll().size();

        // Update the genero
        Genero updatedGenero = generoRepository.findById(genero.getId()).get();
        // Disconnect from session so that the updates on updatedGenero are not directly saved in db
        em.detach(updatedGenero);
        updatedGenero.nombreGenero(UPDATED_NOMBRE_GENERO).descripcionGenero(UPDATED_DESCRIPCION_GENERO);

        restGeneroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGenero.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGenero))
            )
            .andExpect(status().isOk());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombreGenero()).isEqualTo(UPDATED_NOMBRE_GENERO);
        assertThat(testGenero.getDescripcionGenero()).isEqualTo(UPDATED_DESCRIPCION_GENERO);
    }

    @Test
    @Transactional
    void putNonExistingGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genero.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genero)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneroWithPatch() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeUpdate = generoRepository.findAll().size();

        // Update the genero using partial update
        Genero partialUpdatedGenero = new Genero();
        partialUpdatedGenero.setId(genero.getId());

        partialUpdatedGenero.descripcionGenero(UPDATED_DESCRIPCION_GENERO);

        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenero.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenero))
            )
            .andExpect(status().isOk());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombreGenero()).isEqualTo(DEFAULT_NOMBRE_GENERO);
        assertThat(testGenero.getDescripcionGenero()).isEqualTo(UPDATED_DESCRIPCION_GENERO);
    }

    @Test
    @Transactional
    void fullUpdateGeneroWithPatch() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeUpdate = generoRepository.findAll().size();

        // Update the genero using partial update
        Genero partialUpdatedGenero = new Genero();
        partialUpdatedGenero.setId(genero.getId());

        partialUpdatedGenero.nombreGenero(UPDATED_NOMBRE_GENERO).descripcionGenero(UPDATED_DESCRIPCION_GENERO);

        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenero.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenero))
            )
            .andExpect(status().isOk());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
        Genero testGenero = generoList.get(generoList.size() - 1);
        assertThat(testGenero.getNombreGenero()).isEqualTo(UPDATED_NOMBRE_GENERO);
        assertThat(testGenero.getDescripcionGenero()).isEqualTo(UPDATED_DESCRIPCION_GENERO);
    }

    @Test
    @Transactional
    void patchNonExistingGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genero.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genero))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenero() throws Exception {
        int databaseSizeBeforeUpdate = generoRepository.findAll().size();
        genero.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(genero)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genero in the database
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenero() throws Exception {
        // Initialize the database
        generoRepository.saveAndFlush(genero);

        int databaseSizeBeforeDelete = generoRepository.findAll().size();

        // Delete the genero
        restGeneroMockMvc
            .perform(delete(ENTITY_API_URL_ID, genero.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Genero> generoList = generoRepository.findAll();
        assertThat(generoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
