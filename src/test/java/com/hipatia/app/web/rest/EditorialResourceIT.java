package com.hipatia.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipatia.app.IntegrationTest;
import com.hipatia.app.domain.Editorial;
import com.hipatia.app.repository.EditorialRepository;
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
 * Integration tests for the {@link EditorialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EditorialResourceIT {

    private static final String DEFAULT_NOMBRE_EDITORIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_EDITORIAL = "BBBBBBBBBB";

    private static final Long DEFAULT_CANTIDAD_TITULOS = 100L;
    private static final Long UPDATED_CANTIDAD_TITULOS = 99L;

    private static final String ENTITY_API_URL = "/api/editorials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EditorialRepository editorialRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEditorialMockMvc;

    private Editorial editorial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Editorial createEntity(EntityManager em) {
        Editorial editorial = new Editorial().nombreEditorial(DEFAULT_NOMBRE_EDITORIAL).cantidadTitulos(DEFAULT_CANTIDAD_TITULOS);
        return editorial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Editorial createUpdatedEntity(EntityManager em) {
        Editorial editorial = new Editorial().nombreEditorial(UPDATED_NOMBRE_EDITORIAL).cantidadTitulos(UPDATED_CANTIDAD_TITULOS);
        return editorial;
    }

    @BeforeEach
    public void initTest() {
        editorial = createEntity(em);
    }

    @Test
    @Transactional
    void createEditorial() throws Exception {
        int databaseSizeBeforeCreate = editorialRepository.findAll().size();
        // Create the Editorial
        restEditorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editorial)))
            .andExpect(status().isCreated());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeCreate + 1);
        Editorial testEditorial = editorialList.get(editorialList.size() - 1);
        assertThat(testEditorial.getNombreEditorial()).isEqualTo(DEFAULT_NOMBRE_EDITORIAL);
        assertThat(testEditorial.getCantidadTitulos()).isEqualTo(DEFAULT_CANTIDAD_TITULOS);
    }

    @Test
    @Transactional
    void createEditorialWithExistingId() throws Exception {
        // Create the Editorial with an existing ID
        editorial.setId(1L);

        int databaseSizeBeforeCreate = editorialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEditorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editorial)))
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreEditorialIsRequired() throws Exception {
        int databaseSizeBeforeTest = editorialRepository.findAll().size();
        // set the field null
        editorial.setNombreEditorial(null);

        // Create the Editorial, which fails.

        restEditorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editorial)))
            .andExpect(status().isBadRequest());

        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEditorials() throws Exception {
        // Initialize the database
        editorialRepository.saveAndFlush(editorial);

        // Get all the editorialList
        restEditorialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(editorial.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreEditorial").value(hasItem(DEFAULT_NOMBRE_EDITORIAL)))
            .andExpect(jsonPath("$.[*].cantidadTitulos").value(hasItem(DEFAULT_CANTIDAD_TITULOS.intValue())));
    }

    @Test
    @Transactional
    void getEditorial() throws Exception {
        // Initialize the database
        editorialRepository.saveAndFlush(editorial);

        // Get the editorial
        restEditorialMockMvc
            .perform(get(ENTITY_API_URL_ID, editorial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(editorial.getId().intValue()))
            .andExpect(jsonPath("$.nombreEditorial").value(DEFAULT_NOMBRE_EDITORIAL))
            .andExpect(jsonPath("$.cantidadTitulos").value(DEFAULT_CANTIDAD_TITULOS.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEditorial() throws Exception {
        // Get the editorial
        restEditorialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEditorial() throws Exception {
        // Initialize the database
        editorialRepository.saveAndFlush(editorial);

        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();

        // Update the editorial
        Editorial updatedEditorial = editorialRepository.findById(editorial.getId()).get();
        // Disconnect from session so that the updates on updatedEditorial are not directly saved in db
        em.detach(updatedEditorial);
        updatedEditorial.nombreEditorial(UPDATED_NOMBRE_EDITORIAL).cantidadTitulos(UPDATED_CANTIDAD_TITULOS);

        restEditorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEditorial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEditorial))
            )
            .andExpect(status().isOk());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
        Editorial testEditorial = editorialList.get(editorialList.size() - 1);
        assertThat(testEditorial.getNombreEditorial()).isEqualTo(UPDATED_NOMBRE_EDITORIAL);
        assertThat(testEditorial.getCantidadTitulos()).isEqualTo(UPDATED_CANTIDAD_TITULOS);
    }

    @Test
    @Transactional
    void putNonExistingEditorial() throws Exception {
        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();
        editorial.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, editorial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editorial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEditorial() throws Exception {
        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();
        editorial.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editorial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEditorial() throws Exception {
        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();
        editorial.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editorial)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEditorialWithPatch() throws Exception {
        // Initialize the database
        editorialRepository.saveAndFlush(editorial);

        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();

        // Update the editorial using partial update
        Editorial partialUpdatedEditorial = new Editorial();
        partialUpdatedEditorial.setId(editorial.getId());

        partialUpdatedEditorial.nombreEditorial(UPDATED_NOMBRE_EDITORIAL);

        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEditorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEditorial))
            )
            .andExpect(status().isOk());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
        Editorial testEditorial = editorialList.get(editorialList.size() - 1);
        assertThat(testEditorial.getNombreEditorial()).isEqualTo(UPDATED_NOMBRE_EDITORIAL);
        assertThat(testEditorial.getCantidadTitulos()).isEqualTo(DEFAULT_CANTIDAD_TITULOS);
    }

    @Test
    @Transactional
    void fullUpdateEditorialWithPatch() throws Exception {
        // Initialize the database
        editorialRepository.saveAndFlush(editorial);

        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();

        // Update the editorial using partial update
        Editorial partialUpdatedEditorial = new Editorial();
        partialUpdatedEditorial.setId(editorial.getId());

        partialUpdatedEditorial.nombreEditorial(UPDATED_NOMBRE_EDITORIAL).cantidadTitulos(UPDATED_CANTIDAD_TITULOS);

        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEditorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEditorial))
            )
            .andExpect(status().isOk());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
        Editorial testEditorial = editorialList.get(editorialList.size() - 1);
        assertThat(testEditorial.getNombreEditorial()).isEqualTo(UPDATED_NOMBRE_EDITORIAL);
        assertThat(testEditorial.getCantidadTitulos()).isEqualTo(UPDATED_CANTIDAD_TITULOS);
    }

    @Test
    @Transactional
    void patchNonExistingEditorial() throws Exception {
        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();
        editorial.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, editorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(editorial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEditorial() throws Exception {
        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();
        editorial.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(editorial))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEditorial() throws Exception {
        int databaseSizeBeforeUpdate = editorialRepository.findAll().size();
        editorial.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditorialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(editorial))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Editorial in the database
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEditorial() throws Exception {
        // Initialize the database
        editorialRepository.saveAndFlush(editorial);

        int databaseSizeBeforeDelete = editorialRepository.findAll().size();

        // Delete the editorial
        restEditorialMockMvc
            .perform(delete(ENTITY_API_URL_ID, editorial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Editorial> editorialList = editorialRepository.findAll();
        assertThat(editorialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
