package com.hipatia.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hipatia.app.IntegrationTest;
import com.hipatia.app.domain.Autor;
import com.hipatia.app.repository.AutorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AutorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AutorResourceIT {

    private static final String DEFAULT_NOMBRE_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGEN_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_ORIGEN_AUTOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/autors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AutorRepository autorRepository;

    @Mock
    private AutorRepository autorRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutorMockMvc;

    private Autor autor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autor createEntity(EntityManager em) {
        Autor autor = new Autor().nombreAutor(DEFAULT_NOMBRE_AUTOR).apellidoAutor(DEFAULT_APELLIDO_AUTOR).origenAutor(DEFAULT_ORIGEN_AUTOR);
        return autor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autor createUpdatedEntity(EntityManager em) {
        Autor autor = new Autor().nombreAutor(UPDATED_NOMBRE_AUTOR).apellidoAutor(UPDATED_APELLIDO_AUTOR).origenAutor(UPDATED_ORIGEN_AUTOR);
        return autor;
    }

    @BeforeEach
    public void initTest() {
        autor = createEntity(em);
    }

    @Test
    @Transactional
    void createAutor() throws Exception {
        int databaseSizeBeforeCreate = autorRepository.findAll().size();
        // Create the Autor
        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isCreated());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeCreate + 1);
        Autor testAutor = autorList.get(autorList.size() - 1);
        assertThat(testAutor.getNombreAutor()).isEqualTo(DEFAULT_NOMBRE_AUTOR);
        assertThat(testAutor.getApellidoAutor()).isEqualTo(DEFAULT_APELLIDO_AUTOR);
        assertThat(testAutor.getOrigenAutor()).isEqualTo(DEFAULT_ORIGEN_AUTOR);
    }

    @Test
    @Transactional
    void createAutorWithExistingId() throws Exception {
        // Create the Autor with an existing ID
        autor.setId(1L);

        int databaseSizeBeforeCreate = autorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreAutorIsRequired() throws Exception {
        int databaseSizeBeforeTest = autorRepository.findAll().size();
        // set the field null
        autor.setNombreAutor(null);

        // Create the Autor, which fails.

        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isBadRequest());

        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidoAutorIsRequired() throws Exception {
        int databaseSizeBeforeTest = autorRepository.findAll().size();
        // set the field null
        autor.setApellidoAutor(null);

        // Create the Autor, which fails.

        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isBadRequest());

        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrigenAutorIsRequired() throws Exception {
        int databaseSizeBeforeTest = autorRepository.findAll().size();
        // set the field null
        autor.setOrigenAutor(null);

        // Create the Autor, which fails.

        restAutorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isBadRequest());

        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAutors() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        // Get all the autorList
        restAutorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreAutor").value(hasItem(DEFAULT_NOMBRE_AUTOR)))
            .andExpect(jsonPath("$.[*].apellidoAutor").value(hasItem(DEFAULT_APELLIDO_AUTOR)))
            .andExpect(jsonPath("$.[*].origenAutor").value(hasItem(DEFAULT_ORIGEN_AUTOR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAutorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(autorRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAutorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(autorRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAutorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(autorRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAutorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(autorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAutor() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        // Get the autor
        restAutorMockMvc
            .perform(get(ENTITY_API_URL_ID, autor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autor.getId().intValue()))
            .andExpect(jsonPath("$.nombreAutor").value(DEFAULT_NOMBRE_AUTOR))
            .andExpect(jsonPath("$.apellidoAutor").value(DEFAULT_APELLIDO_AUTOR))
            .andExpect(jsonPath("$.origenAutor").value(DEFAULT_ORIGEN_AUTOR));
    }

    @Test
    @Transactional
    void getNonExistingAutor() throws Exception {
        // Get the autor
        restAutorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAutor() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        int databaseSizeBeforeUpdate = autorRepository.findAll().size();

        // Update the autor
        Autor updatedAutor = autorRepository.findById(autor.getId()).get();
        // Disconnect from session so that the updates on updatedAutor are not directly saved in db
        em.detach(updatedAutor);
        updatedAutor.nombreAutor(UPDATED_NOMBRE_AUTOR).apellidoAutor(UPDATED_APELLIDO_AUTOR).origenAutor(UPDATED_ORIGEN_AUTOR);

        restAutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAutor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAutor))
            )
            .andExpect(status().isOk());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
        Autor testAutor = autorList.get(autorList.size() - 1);
        assertThat(testAutor.getNombreAutor()).isEqualTo(UPDATED_NOMBRE_AUTOR);
        assertThat(testAutor.getApellidoAutor()).isEqualTo(UPDATED_APELLIDO_AUTOR);
        assertThat(testAutor.getOrigenAutor()).isEqualTo(UPDATED_ORIGEN_AUTOR);
    }

    @Test
    @Transactional
    void putNonExistingAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();
        autor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();
        autor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(autor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();
        autor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAutorWithPatch() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        int databaseSizeBeforeUpdate = autorRepository.findAll().size();

        // Update the autor using partial update
        Autor partialUpdatedAutor = new Autor();
        partialUpdatedAutor.setId(autor.getId());

        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAutor))
            )
            .andExpect(status().isOk());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
        Autor testAutor = autorList.get(autorList.size() - 1);
        assertThat(testAutor.getNombreAutor()).isEqualTo(DEFAULT_NOMBRE_AUTOR);
        assertThat(testAutor.getApellidoAutor()).isEqualTo(DEFAULT_APELLIDO_AUTOR);
        assertThat(testAutor.getOrigenAutor()).isEqualTo(DEFAULT_ORIGEN_AUTOR);
    }

    @Test
    @Transactional
    void fullUpdateAutorWithPatch() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        int databaseSizeBeforeUpdate = autorRepository.findAll().size();

        // Update the autor using partial update
        Autor partialUpdatedAutor = new Autor();
        partialUpdatedAutor.setId(autor.getId());

        partialUpdatedAutor.nombreAutor(UPDATED_NOMBRE_AUTOR).apellidoAutor(UPDATED_APELLIDO_AUTOR).origenAutor(UPDATED_ORIGEN_AUTOR);

        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAutor))
            )
            .andExpect(status().isOk());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
        Autor testAutor = autorList.get(autorList.size() - 1);
        assertThat(testAutor.getNombreAutor()).isEqualTo(UPDATED_NOMBRE_AUTOR);
        assertThat(testAutor.getApellidoAutor()).isEqualTo(UPDATED_APELLIDO_AUTOR);
        assertThat(testAutor.getOrigenAutor()).isEqualTo(UPDATED_ORIGEN_AUTOR);
    }

    @Test
    @Transactional
    void patchNonExistingAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();
        autor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();
        autor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(autor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();
        autor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(autor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAutor() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        int databaseSizeBeforeDelete = autorRepository.findAll().size();

        // Delete the autor
        restAutorMockMvc
            .perform(delete(ENTITY_API_URL_ID, autor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
