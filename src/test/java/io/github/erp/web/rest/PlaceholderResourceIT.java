package io.github.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
import java.util.Collections;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlaceholderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlaceholderResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/placeholders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/placeholders";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlaceholderRepository placeholderRepository;

    @Autowired
    private PlaceholderMapper placeholderMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PlaceholderSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlaceholderSearchRepository mockPlaceholderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaceholderMockMvc;

    private Placeholder placeholder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Placeholder createEntity(EntityManager em) {
        Placeholder placeholder = new Placeholder().description(DEFAULT_DESCRIPTION).token(DEFAULT_TOKEN);
        return placeholder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Placeholder createUpdatedEntity(EntityManager em) {
        Placeholder placeholder = new Placeholder().description(UPDATED_DESCRIPTION).token(UPDATED_TOKEN);
        return placeholder;
    }

    @BeforeEach
    public void initTest() {
        placeholder = createEntity(em);
    }

    @Test
    @Transactional
    void createPlaceholder() throws Exception {
        int databaseSizeBeforeCreate = placeholderRepository.findAll().size();
        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);
        restPlaceholderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeCreate + 1);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlaceholder.getToken()).isEqualTo(DEFAULT_TOKEN);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(1)).save(testPlaceholder);
    }

    @Test
    @Transactional
    void createPlaceholderWithExistingId() throws Exception {
        // Create the Placeholder with an existing ID
        placeholder.setId(1L);
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        int databaseSizeBeforeCreate = placeholderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceholderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeCreate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = placeholderRepository.findAll().size();
        // set the field null
        placeholder.setDescription(null);

        // Create the Placeholder, which fails.
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        restPlaceholderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlaceholders() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeholder.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));
    }

    @Test
    @Transactional
    void getPlaceholder() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get the placeholder
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL_ID, placeholder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(placeholder.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN));
    }

    @Test
    @Transactional
    void getPlaceholdersByIdFiltering() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        Long id = placeholder.getId();

        defaultPlaceholderShouldBeFound("id.equals=" + id);
        defaultPlaceholderShouldNotBeFound("id.notEquals=" + id);

        defaultPlaceholderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlaceholderShouldNotBeFound("id.greaterThan=" + id);

        defaultPlaceholderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlaceholderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description equals to DEFAULT_DESCRIPTION
        defaultPlaceholderShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the placeholderList where description equals to UPDATED_DESCRIPTION
        defaultPlaceholderShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description not equals to DEFAULT_DESCRIPTION
        defaultPlaceholderShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the placeholderList where description not equals to UPDATED_DESCRIPTION
        defaultPlaceholderShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPlaceholderShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the placeholderList where description equals to UPDATED_DESCRIPTION
        defaultPlaceholderShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description is not null
        defaultPlaceholderShouldBeFound("description.specified=true");

        // Get all the placeholderList where description is null
        defaultPlaceholderShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description contains DEFAULT_DESCRIPTION
        defaultPlaceholderShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the placeholderList where description contains UPDATED_DESCRIPTION
        defaultPlaceholderShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where description does not contain DEFAULT_DESCRIPTION
        defaultPlaceholderShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the placeholderList where description does not contain UPDATED_DESCRIPTION
        defaultPlaceholderShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token equals to DEFAULT_TOKEN
        defaultPlaceholderShouldBeFound("token.equals=" + DEFAULT_TOKEN);

        // Get all the placeholderList where token equals to UPDATED_TOKEN
        defaultPlaceholderShouldNotBeFound("token.equals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token not equals to DEFAULT_TOKEN
        defaultPlaceholderShouldNotBeFound("token.notEquals=" + DEFAULT_TOKEN);

        // Get all the placeholderList where token not equals to UPDATED_TOKEN
        defaultPlaceholderShouldBeFound("token.notEquals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsInShouldWork() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token in DEFAULT_TOKEN or UPDATED_TOKEN
        defaultPlaceholderShouldBeFound("token.in=" + DEFAULT_TOKEN + "," + UPDATED_TOKEN);

        // Get all the placeholderList where token equals to UPDATED_TOKEN
        defaultPlaceholderShouldNotBeFound("token.in=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token is not null
        defaultPlaceholderShouldBeFound("token.specified=true");

        // Get all the placeholderList where token is null
        defaultPlaceholderShouldNotBeFound("token.specified=false");
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenContainsSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token contains DEFAULT_TOKEN
        defaultPlaceholderShouldBeFound("token.contains=" + DEFAULT_TOKEN);

        // Get all the placeholderList where token contains UPDATED_TOKEN
        defaultPlaceholderShouldNotBeFound("token.contains=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTokenNotContainsSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        // Get all the placeholderList where token does not contain DEFAULT_TOKEN
        defaultPlaceholderShouldNotBeFound("token.doesNotContain=" + DEFAULT_TOKEN);

        // Get all the placeholderList where token does not contain UPDATED_TOKEN
        defaultPlaceholderShouldBeFound("token.doesNotContain=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllPlaceholdersByContainingPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        Placeholder containingPlaceholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            containingPlaceholder = PlaceholderResourceIT.createEntity(em);
            em.persist(containingPlaceholder);
            em.flush();
        } else {
            containingPlaceholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(containingPlaceholder);
        em.flush();
        placeholder.setContainingPlaceholder(containingPlaceholder);
        placeholderRepository.saveAndFlush(placeholder);
        Long containingPlaceholderId = containingPlaceholder.getId();

        // Get all the placeholderList where containingPlaceholder equals to containingPlaceholderId
        defaultPlaceholderShouldBeFound("containingPlaceholderId.equals=" + containingPlaceholderId);

        // Get all the placeholderList where containingPlaceholder equals to (containingPlaceholderId + 1)
        defaultPlaceholderShouldNotBeFound("containingPlaceholderId.equals=" + (containingPlaceholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlaceholderShouldBeFound(String filter) throws Exception {
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeholder.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));

        // Check, that the count call also returns 1
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlaceholderShouldNotBeFound(String filter) throws Exception {
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlaceholderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlaceholder() throws Exception {
        // Get the placeholder
        restPlaceholderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlaceholder() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();

        // Update the placeholder
        Placeholder updatedPlaceholder = placeholderRepository.findById(placeholder.getId()).get();
        // Disconnect from session so that the updates on updatedPlaceholder are not directly saved in db
        em.detach(updatedPlaceholder);
        updatedPlaceholder.description(UPDATED_DESCRIPTION).token(UPDATED_TOKEN);
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(updatedPlaceholder);

        restPlaceholderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeholderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlaceholder.getToken()).isEqualTo(UPDATED_TOKEN);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository).save(testPlaceholder);
    }

    @Test
    @Transactional
    void putNonExistingPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeholderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(placeholderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void partialUpdatePlaceholderWithPatch() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();

        // Update the placeholder using partial update
        Placeholder partialUpdatedPlaceholder = new Placeholder();
        partialUpdatedPlaceholder.setId(placeholder.getId());

        partialUpdatedPlaceholder.description(UPDATED_DESCRIPTION);

        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaceholder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaceholder))
            )
            .andExpect(status().isOk());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlaceholder.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdatePlaceholderWithPatch() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();

        // Update the placeholder using partial update
        Placeholder partialUpdatedPlaceholder = new Placeholder();
        partialUpdatedPlaceholder.setId(placeholder.getId());

        partialUpdatedPlaceholder.description(UPDATED_DESCRIPTION).token(UPDATED_TOKEN);

        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaceholder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaceholder))
            )
            .andExpect(status().isOk());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);
        Placeholder testPlaceholder = placeholderList.get(placeholderList.size() - 1);
        assertThat(testPlaceholder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlaceholder.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, placeholderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlaceholder() throws Exception {
        int databaseSizeBeforeUpdate = placeholderRepository.findAll().size();
        placeholder.setId(count.incrementAndGet());

        // Create the Placeholder
        PlaceholderDTO placeholderDTO = placeholderMapper.toDto(placeholder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceholderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(placeholderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Placeholder in the database
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(0)).save(placeholder);
    }

    @Test
    @Transactional
    void deletePlaceholder() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);

        int databaseSizeBeforeDelete = placeholderRepository.findAll().size();

        // Delete the placeholder
        restPlaceholderMockMvc
            .perform(delete(ENTITY_API_URL_ID, placeholder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Placeholder> placeholderList = placeholderRepository.findAll();
        assertThat(placeholderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Placeholder in Elasticsearch
        verify(mockPlaceholderSearchRepository, times(1)).deleteById(placeholder.getId());
    }

    @Test
    @Transactional
    void searchPlaceholder() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        when(mockPlaceholderSearchRepository.search("id:" + placeholder.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(placeholder), PageRequest.of(0, 1), 1));

        // Search the placeholder
        restPlaceholderMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + placeholder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(placeholder.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)));
    }
}
