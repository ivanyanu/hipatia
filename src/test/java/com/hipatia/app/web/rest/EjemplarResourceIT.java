package com.hipatia.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipatia.app.IntegrationTest;
import com.hipatia.app.domain.Ejemplar;
import com.hipatia.app.domain.enumeration.EstadoEjemplar;
import com.hipatia.app.repository.EjemplarRepository;
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
 * Integration tests for the {@link EjemplarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EjemplarResourceIT {

    private static final EstadoEjemplar DEFAULT_ESTADO_EJEMPLAR = EstadoEjemplar.DISPONIBLE;
    private static final EstadoEjemplar UPDATED_ESTADO_EJEMPLAR = EstadoEjemplar.PRESTADO;

    private static final String DEFAULT_FECHA_ALTA_EJEMPLAR = "AAAAAAAAAA";
    private static final String UPDATED_FECHA_ALTA_EJEMPLAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ejemplars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEjemplarMockMvc;

    private Ejemplar ejemplar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ejemplar createEntity(EntityManager em) {
        Ejemplar ejemplar = new Ejemplar().estadoEjemplar(DEFAULT_ESTADO_EJEMPLAR).fechaAltaEjemplar(DEFAULT_FECHA_ALTA_EJEMPLAR);
        return ejemplar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ejemplar createUpdatedEntity(EntityManager em) {
        Ejemplar ejemplar = new Ejemplar().estadoEjemplar(UPDATED_ESTADO_EJEMPLAR).fechaAltaEjemplar(UPDATED_FECHA_ALTA_EJEMPLAR);
        return ejemplar;
    }

    @BeforeEach
    public void initTest() {
        ejemplar = createEntity(em);
    }

    @Test
    @Transactional
    void createEjemplar() throws Exception {
        int databaseSizeBeforeCreate = ejemplarRepository.findAll().size();
        // Create the Ejemplar
        restEjemplarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ejemplar)))
            .andExpect(status().isCreated());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeCreate + 1);
        Ejemplar testEjemplar = ejemplarList.get(ejemplarList.size() - 1);
        assertThat(testEjemplar.getEstadoEjemplar()).isEqualTo(DEFAULT_ESTADO_EJEMPLAR);
        assertThat(testEjemplar.getFechaAltaEjemplar()).isEqualTo(DEFAULT_FECHA_ALTA_EJEMPLAR);
    }

    @Test
    @Transactional
    void createEjemplarWithExistingId() throws Exception {
        // Create the Ejemplar with an existing ID
        ejemplar.setId(1L);

        int databaseSizeBeforeCreate = ejemplarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEjemplarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ejemplar)))
            .andExpect(status().isBadRequest());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstadoEjemplarIsRequired() throws Exception {
        int databaseSizeBeforeTest = ejemplarRepository.findAll().size();
        // set the field null
        ejemplar.setEstadoEjemplar(null);

        // Create the Ejemplar, which fails.

        restEjemplarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ejemplar)))
            .andExpect(status().isBadRequest());

        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaAltaEjemplarIsRequired() throws Exception {
        int databaseSizeBeforeTest = ejemplarRepository.findAll().size();
        // set the field null
        ejemplar.setFechaAltaEjemplar(null);

        // Create the Ejemplar, which fails.

        restEjemplarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ejemplar)))
            .andExpect(status().isBadRequest());

        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEjemplars() throws Exception {
        // Initialize the database
        ejemplarRepository.saveAndFlush(ejemplar);

        // Get all the ejemplarList
        restEjemplarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ejemplar.getId().intValue())))
            .andExpect(jsonPath("$.[*].estadoEjemplar").value(hasItem(DEFAULT_ESTADO_EJEMPLAR.toString())))
            .andExpect(jsonPath("$.[*].fechaAltaEjemplar").value(hasItem(DEFAULT_FECHA_ALTA_EJEMPLAR)));
    }

    @Test
    @Transactional
    void getEjemplar() throws Exception {
        // Initialize the database
        ejemplarRepository.saveAndFlush(ejemplar);

        // Get the ejemplar
        restEjemplarMockMvc
            .perform(get(ENTITY_API_URL_ID, ejemplar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ejemplar.getId().intValue()))
            .andExpect(jsonPath("$.estadoEjemplar").value(DEFAULT_ESTADO_EJEMPLAR.toString()))
            .andExpect(jsonPath("$.fechaAltaEjemplar").value(DEFAULT_FECHA_ALTA_EJEMPLAR));
    }

    @Test
    @Transactional
    void getNonExistingEjemplar() throws Exception {
        // Get the ejemplar
        restEjemplarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEjemplar() throws Exception {
        // Initialize the database
        ejemplarRepository.saveAndFlush(ejemplar);

        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();

        // Update the ejemplar
        Ejemplar updatedEjemplar = ejemplarRepository.findById(ejemplar.getId()).get();
        // Disconnect from session so that the updates on updatedEjemplar are not directly saved in db
        em.detach(updatedEjemplar);
        updatedEjemplar.estadoEjemplar(UPDATED_ESTADO_EJEMPLAR).fechaAltaEjemplar(UPDATED_FECHA_ALTA_EJEMPLAR);

        restEjemplarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEjemplar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEjemplar))
            )
            .andExpect(status().isOk());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
        Ejemplar testEjemplar = ejemplarList.get(ejemplarList.size() - 1);
        assertThat(testEjemplar.getEstadoEjemplar()).isEqualTo(UPDATED_ESTADO_EJEMPLAR);
        assertThat(testEjemplar.getFechaAltaEjemplar()).isEqualTo(UPDATED_FECHA_ALTA_EJEMPLAR);
    }

    @Test
    @Transactional
    void putNonExistingEjemplar() throws Exception {
        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();
        ejemplar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEjemplarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ejemplar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ejemplar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEjemplar() throws Exception {
        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();
        ejemplar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjemplarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ejemplar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEjemplar() throws Exception {
        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();
        ejemplar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjemplarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ejemplar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEjemplarWithPatch() throws Exception {
        // Initialize the database
        ejemplarRepository.saveAndFlush(ejemplar);

        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();

        // Update the ejemplar using partial update
        Ejemplar partialUpdatedEjemplar = new Ejemplar();
        partialUpdatedEjemplar.setId(ejemplar.getId());

        partialUpdatedEjemplar.fechaAltaEjemplar(UPDATED_FECHA_ALTA_EJEMPLAR);

        restEjemplarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEjemplar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEjemplar))
            )
            .andExpect(status().isOk());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
        Ejemplar testEjemplar = ejemplarList.get(ejemplarList.size() - 1);
        assertThat(testEjemplar.getEstadoEjemplar()).isEqualTo(DEFAULT_ESTADO_EJEMPLAR);
        assertThat(testEjemplar.getFechaAltaEjemplar()).isEqualTo(UPDATED_FECHA_ALTA_EJEMPLAR);
    }

    @Test
    @Transactional
    void fullUpdateEjemplarWithPatch() throws Exception {
        // Initialize the database
        ejemplarRepository.saveAndFlush(ejemplar);

        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();

        // Update the ejemplar using partial update
        Ejemplar partialUpdatedEjemplar = new Ejemplar();
        partialUpdatedEjemplar.setId(ejemplar.getId());

        partialUpdatedEjemplar.estadoEjemplar(UPDATED_ESTADO_EJEMPLAR).fechaAltaEjemplar(UPDATED_FECHA_ALTA_EJEMPLAR);

        restEjemplarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEjemplar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEjemplar))
            )
            .andExpect(status().isOk());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
        Ejemplar testEjemplar = ejemplarList.get(ejemplarList.size() - 1);
        assertThat(testEjemplar.getEstadoEjemplar()).isEqualTo(UPDATED_ESTADO_EJEMPLAR);
        assertThat(testEjemplar.getFechaAltaEjemplar()).isEqualTo(UPDATED_FECHA_ALTA_EJEMPLAR);
    }

    @Test
    @Transactional
    void patchNonExistingEjemplar() throws Exception {
        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();
        ejemplar.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEjemplarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ejemplar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ejemplar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEjemplar() throws Exception {
        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();
        ejemplar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjemplarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ejemplar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEjemplar() throws Exception {
        int databaseSizeBeforeUpdate = ejemplarRepository.findAll().size();
        ejemplar.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEjemplarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ejemplar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ejemplar in the database
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEjemplar() throws Exception {
        // Initialize the database
        ejemplarRepository.saveAndFlush(ejemplar);

        int databaseSizeBeforeDelete = ejemplarRepository.findAll().size();

        // Delete the ejemplar
        restEjemplarMockMvc
            .perform(delete(ENTITY_API_URL_ID, ejemplar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ejemplar> ejemplarList = ejemplarRepository.findAll();
        assertThat(ejemplarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
