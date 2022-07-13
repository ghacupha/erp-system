package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.IntegrationTest;
import io.github.erp.domain.SystemModule;
import io.github.erp.repository.SystemModuleRepository;
import io.github.erp.repository.search.SystemModuleSearchRepository;
import io.github.erp.service.dto.SystemModuleDTO;
import io.github.erp.service.mapper.SystemModuleMapper;
// import io.github.erp.web.rest.SystemModuleResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SystemModuleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SystemModuleResourceIT {

    private static final String DEFAULT_MODULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/system-modules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/system-modules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemModuleRepository systemModuleRepository;

    @Autowired
    private SystemModuleMapper systemModuleMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SystemModuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private SystemModuleSearchRepository mockSystemModuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemModuleMockMvc;

    private SystemModule systemModule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemModule createEntity(EntityManager em) {
        SystemModule systemModule = new SystemModule().moduleName(DEFAULT_MODULE_NAME);
        return systemModule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemModule createUpdatedEntity(EntityManager em) {
        SystemModule systemModule = new SystemModule().moduleName(UPDATED_MODULE_NAME);
        return systemModule;
    }

    @BeforeEach
    public void initTest() {
        systemModule = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemModule() throws Exception {
        int databaseSizeBeforeCreate = systemModuleRepository.findAll().size();
        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);
        restSystemModuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeCreate + 1);
        SystemModule testSystemModule = systemModuleList.get(systemModuleList.size() - 1);
        assertThat(testSystemModule.getModuleName()).isEqualTo(DEFAULT_MODULE_NAME);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(1)).save(testSystemModule);
    }

    @Test
    @Transactional
    void createSystemModuleWithExistingId() throws Exception {
        // Create the SystemModule with an existing ID
        systemModule.setId(1L);
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        int databaseSizeBeforeCreate = systemModuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemModuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void checkModuleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemModuleRepository.findAll().size();
        // set the field null
        systemModule.setModuleName(null);

        // Create the SystemModule, which fails.
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        restSystemModuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemModules() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList
        restSystemModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].moduleName").value(hasItem(DEFAULT_MODULE_NAME)));
    }

    @Test
    @Transactional
    void getSystemModule() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get the systemModule
        restSystemModuleMockMvc
            .perform(get(ENTITY_API_URL_ID, systemModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemModule.getId().intValue()))
            .andExpect(jsonPath("$.moduleName").value(DEFAULT_MODULE_NAME));
    }

    @Test
    @Transactional
    void getSystemModulesByIdFiltering() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        Long id = systemModule.getId();

        defaultSystemModuleShouldBeFound("id.equals=" + id);
        defaultSystemModuleShouldNotBeFound("id.notEquals=" + id);

        defaultSystemModuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemModuleShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemModuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemModuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemModulesByModuleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList where moduleName equals to DEFAULT_MODULE_NAME
        defaultSystemModuleShouldBeFound("moduleName.equals=" + DEFAULT_MODULE_NAME);

        // Get all the systemModuleList where moduleName equals to UPDATED_MODULE_NAME
        defaultSystemModuleShouldNotBeFound("moduleName.equals=" + UPDATED_MODULE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModulesByModuleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList where moduleName not equals to DEFAULT_MODULE_NAME
        defaultSystemModuleShouldNotBeFound("moduleName.notEquals=" + DEFAULT_MODULE_NAME);

        // Get all the systemModuleList where moduleName not equals to UPDATED_MODULE_NAME
        defaultSystemModuleShouldBeFound("moduleName.notEquals=" + UPDATED_MODULE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModulesByModuleNameIsInShouldWork() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList where moduleName in DEFAULT_MODULE_NAME or UPDATED_MODULE_NAME
        defaultSystemModuleShouldBeFound("moduleName.in=" + DEFAULT_MODULE_NAME + "," + UPDATED_MODULE_NAME);

        // Get all the systemModuleList where moduleName equals to UPDATED_MODULE_NAME
        defaultSystemModuleShouldNotBeFound("moduleName.in=" + UPDATED_MODULE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModulesByModuleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList where moduleName is not null
        defaultSystemModuleShouldBeFound("moduleName.specified=true");

        // Get all the systemModuleList where moduleName is null
        defaultSystemModuleShouldNotBeFound("moduleName.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemModulesByModuleNameContainsSomething() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList where moduleName contains DEFAULT_MODULE_NAME
        defaultSystemModuleShouldBeFound("moduleName.contains=" + DEFAULT_MODULE_NAME);

        // Get all the systemModuleList where moduleName contains UPDATED_MODULE_NAME
        defaultSystemModuleShouldNotBeFound("moduleName.contains=" + UPDATED_MODULE_NAME);
    }

    @Test
    @Transactional
    void getAllSystemModulesByModuleNameNotContainsSomething() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        // Get all the systemModuleList where moduleName does not contain DEFAULT_MODULE_NAME
        defaultSystemModuleShouldNotBeFound("moduleName.doesNotContain=" + DEFAULT_MODULE_NAME);

        // Get all the systemModuleList where moduleName does not contain UPDATED_MODULE_NAME
        defaultSystemModuleShouldBeFound("moduleName.doesNotContain=" + UPDATED_MODULE_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemModuleShouldBeFound(String filter) throws Exception {
        restSystemModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].moduleName").value(hasItem(DEFAULT_MODULE_NAME)));

        // Check, that the count call also returns 1
        restSystemModuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemModuleShouldNotBeFound(String filter) throws Exception {
        restSystemModuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemModuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemModule() throws Exception {
        // Get the systemModule
        restSystemModuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemModule() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();

        // Update the systemModule
        SystemModule updatedSystemModule = systemModuleRepository.findById(systemModule.getId()).get();
        // Disconnect from session so that the updates on updatedSystemModule are not directly saved in db
        em.detach(updatedSystemModule);
        updatedSystemModule.moduleName(UPDATED_MODULE_NAME);
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(updatedSystemModule);

        restSystemModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemModuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);
        SystemModule testSystemModule = systemModuleList.get(systemModuleList.size() - 1);
        assertThat(testSystemModule.getModuleName()).isEqualTo(UPDATED_MODULE_NAME);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository).save(testSystemModule);
    }

    @Test
    @Transactional
    void putNonExistingSystemModule() throws Exception {
        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();
        systemModule.setId(count.incrementAndGet());

        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemModuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemModule() throws Exception {
        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();
        systemModule.setId(count.incrementAndGet());

        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemModule() throws Exception {
        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();
        systemModule.setId(count.incrementAndGet());

        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModuleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void partialUpdateSystemModuleWithPatch() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();

        // Update the systemModule using partial update
        SystemModule partialUpdatedSystemModule = new SystemModule();
        partialUpdatedSystemModule.setId(systemModule.getId());

        partialUpdatedSystemModule.moduleName(UPDATED_MODULE_NAME);

        restSystemModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemModule))
            )
            .andExpect(status().isOk());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);
        SystemModule testSystemModule = systemModuleList.get(systemModuleList.size() - 1);
        assertThat(testSystemModule.getModuleName()).isEqualTo(UPDATED_MODULE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSystemModuleWithPatch() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();

        // Update the systemModule using partial update
        SystemModule partialUpdatedSystemModule = new SystemModule();
        partialUpdatedSystemModule.setId(systemModule.getId());

        partialUpdatedSystemModule.moduleName(UPDATED_MODULE_NAME);

        restSystemModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemModule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemModule))
            )
            .andExpect(status().isOk());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);
        SystemModule testSystemModule = systemModuleList.get(systemModuleList.size() - 1);
        assertThat(testSystemModule.getModuleName()).isEqualTo(UPDATED_MODULE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSystemModule() throws Exception {
        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();
        systemModule.setId(count.incrementAndGet());

        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemModuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemModule() throws Exception {
        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();
        systemModule.setId(count.incrementAndGet());

        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemModule() throws Exception {
        int databaseSizeBeforeUpdate = systemModuleRepository.findAll().size();
        systemModule.setId(count.incrementAndGet());

        // Create the SystemModule
        SystemModuleDTO systemModuleDTO = systemModuleMapper.toDto(systemModule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemModuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemModuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemModule in the database
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(0)).save(systemModule);
    }

    @Test
    @Transactional
    void deleteSystemModule() throws Exception {
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);

        int databaseSizeBeforeDelete = systemModuleRepository.findAll().size();

        // Delete the systemModule
        restSystemModuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemModule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemModule> systemModuleList = systemModuleRepository.findAll();
        assertThat(systemModuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SystemModule in Elasticsearch
        verify(mockSystemModuleSearchRepository, times(1)).deleteById(systemModule.getId());
    }

    @Test
    @Transactional
    void searchSystemModule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        systemModuleRepository.saveAndFlush(systemModule);
        when(mockSystemModuleSearchRepository.search("id:" + systemModule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(systemModule), PageRequest.of(0, 1), 1));

        // Search the systemModule
        restSystemModuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + systemModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemModule.getId().intValue())))
            .andExpect(jsonPath("$.[*].moduleName").value(hasItem(DEFAULT_MODULE_NAME)));
    }
}
