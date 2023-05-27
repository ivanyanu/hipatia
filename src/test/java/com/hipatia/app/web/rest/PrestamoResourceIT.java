package com.hipatia.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipatia.app.IntegrationTest;
import com.hipatia.app.domain.Prestamo;
import com.hipatia.app.domain.enumeration.EstadoPrestamo;
import com.hipatia.app.repository.PrestamoRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PrestamoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrestamoResourceIT {

    private static final EstadoPrestamo DEFAULT_ESTADO_PRESTAMO = EstadoPrestamo.INICIADO;
    private static final EstadoPrestamo UPDATED_ESTADO_PRESTAMO = EstadoPrestamo.DEVUELTO;

    private static final Instant DEFAULT_FECHA_PRESTAMO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_PRESTAMO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_DEVOLUCION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_DEVOLUCION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/prestamos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestamoMockMvc;

    private Prestamo prestamo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestamo createEntity(EntityManager em) {
        Prestamo prestamo = new Prestamo()
            .estadoPrestamo(DEFAULT_ESTADO_PRESTAMO)
            .fechaPrestamo(DEFAULT_FECHA_PRESTAMO)
            .fechaDevolucion(DEFAULT_FECHA_DEVOLUCION);
        return prestamo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestamo createUpdatedEntity(EntityManager em) {
        Prestamo prestamo = new Prestamo()
            .estadoPrestamo(UPDATED_ESTADO_PRESTAMO)
            .fechaPrestamo(UPDATED_FECHA_PRESTAMO)
            .fechaDevolucion(UPDATED_FECHA_DEVOLUCION);
        return prestamo;
    }

    @BeforeEach
    public void initTest() {
        prestamo = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestamo() throws Exception {
        int databaseSizeBeforeCreate = prestamoRepository.findAll().size();
        // Create the Prestamo
        restPrestamoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamo)))
            .andExpect(status().isCreated());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeCreate + 1);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getEstadoPrestamo()).isEqualTo(DEFAULT_ESTADO_PRESTAMO);
        assertThat(testPrestamo.getFechaPrestamo()).isEqualTo(DEFAULT_FECHA_PRESTAMO);
        assertThat(testPrestamo.getFechaDevolucion()).isEqualTo(DEFAULT_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void createPrestamoWithExistingId() throws Exception {
        // Create the Prestamo with an existing ID
        prestamo.setId(1L);

        int databaseSizeBeforeCreate = prestamoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestamoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamo)))
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrestamos() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        // Get all the prestamoList
        restPrestamoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestamo.getId().intValue())))
            .andExpect(jsonPath("$.[*].estadoPrestamo").value(hasItem(DEFAULT_ESTADO_PRESTAMO.toString())))
            .andExpect(jsonPath("$.[*].fechaPrestamo").value(hasItem(DEFAULT_FECHA_PRESTAMO.toString())))
            .andExpect(jsonPath("$.[*].fechaDevolucion").value(hasItem(DEFAULT_FECHA_DEVOLUCION.toString())));
    }

    @Test
    @Transactional
    void getPrestamo() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        // Get the prestamo
        restPrestamoMockMvc
            .perform(get(ENTITY_API_URL_ID, prestamo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestamo.getId().intValue()))
            .andExpect(jsonPath("$.estadoPrestamo").value(DEFAULT_ESTADO_PRESTAMO.toString()))
            .andExpect(jsonPath("$.fechaPrestamo").value(DEFAULT_FECHA_PRESTAMO.toString()))
            .andExpect(jsonPath("$.fechaDevolucion").value(DEFAULT_FECHA_DEVOLUCION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPrestamo() throws Exception {
        // Get the prestamo
        restPrestamoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestamo() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();

        // Update the prestamo
        Prestamo updatedPrestamo = prestamoRepository.findById(prestamo.getId()).get();
        // Disconnect from session so that the updates on updatedPrestamo are not directly saved in db
        em.detach(updatedPrestamo);
        updatedPrestamo
            .estadoPrestamo(UPDATED_ESTADO_PRESTAMO)
            .fechaPrestamo(UPDATED_FECHA_PRESTAMO)
            .fechaDevolucion(UPDATED_FECHA_DEVOLUCION);

        restPrestamoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrestamo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrestamo))
            )
            .andExpect(status().isOk());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getEstadoPrestamo()).isEqualTo(UPDATED_ESTADO_PRESTAMO);
        assertThat(testPrestamo.getFechaPrestamo()).isEqualTo(UPDATED_FECHA_PRESTAMO);
        assertThat(testPrestamo.getFechaDevolucion()).isEqualTo(UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void putNonExistingPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestamo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestamoWithPatch() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();

        // Update the prestamo using partial update
        Prestamo partialUpdatedPrestamo = new Prestamo();
        partialUpdatedPrestamo.setId(prestamo.getId());

        partialUpdatedPrestamo.fechaDevolucion(UPDATED_FECHA_DEVOLUCION);

        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestamo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestamo))
            )
            .andExpect(status().isOk());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getEstadoPrestamo()).isEqualTo(DEFAULT_ESTADO_PRESTAMO);
        assertThat(testPrestamo.getFechaPrestamo()).isEqualTo(DEFAULT_FECHA_PRESTAMO);
        assertThat(testPrestamo.getFechaDevolucion()).isEqualTo(UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void fullUpdatePrestamoWithPatch() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();

        // Update the prestamo using partial update
        Prestamo partialUpdatedPrestamo = new Prestamo();
        partialUpdatedPrestamo.setId(prestamo.getId());

        partialUpdatedPrestamo
            .estadoPrestamo(UPDATED_ESTADO_PRESTAMO)
            .fechaPrestamo(UPDATED_FECHA_PRESTAMO)
            .fechaDevolucion(UPDATED_FECHA_DEVOLUCION);

        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestamo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestamo))
            )
            .andExpect(status().isOk());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getEstadoPrestamo()).isEqualTo(UPDATED_ESTADO_PRESTAMO);
        assertThat(testPrestamo.getFechaPrestamo()).isEqualTo(UPDATED_FECHA_PRESTAMO);
        assertThat(testPrestamo.getFechaDevolucion()).isEqualTo(UPDATED_FECHA_DEVOLUCION);
    }

    @Test
    @Transactional
    void patchNonExistingPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestamo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestamo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestamo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prestamo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestamo() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeDelete = prestamoRepository.findAll().size();

        // Delete the prestamo
        restPrestamoMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestamo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
