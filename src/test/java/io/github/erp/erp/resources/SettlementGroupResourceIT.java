package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.SettlementGroup;
import io.github.erp.repository.SettlementGroupRepository;
import io.github.erp.repository.search.SettlementGroupSearchRepository;
import io.github.erp.service.dto.SettlementGroupDTO;
import io.github.erp.service.mapper.SettlementGroupMapper;
import io.github.erp.web.rest.TestUtil;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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

/**
 * Integration tests for the {@link io.github.erp.web.rest.SettlementGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SettlementGroupResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/settlement-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/settlement-groups";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettlementGroupRepository settlementGroupRepository;

    @Autowired
    private SettlementGroupMapper settlementGroupMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SettlementGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private SettlementGroupSearchRepository mockSettlementGroupSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettlementGroupMockMvc;

    private SettlementGroup settlementGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettlementGroup createEntity(EntityManager em) {
        SettlementGroup settlementGroup = new SettlementGroup()
            .groupName(DEFAULT_GROUP_NAME)
            .description(DEFAULT_DESCRIPTION)
            .remarks(DEFAULT_REMARKS);
        return settlementGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettlementGroup createUpdatedEntity(EntityManager em) {
        SettlementGroup settlementGroup = new SettlementGroup()
            .groupName(UPDATED_GROUP_NAME)
            .description(UPDATED_DESCRIPTION)
            .remarks(UPDATED_REMARKS);
        return settlementGroup;
    }

    @BeforeEach
    public void initTest() {
        settlementGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createSettlementGroup() throws Exception {
        int databaseSizeBeforeCreate = settlementGroupRepository.findAll().size();
        SettlementGroupDTO settlementGroupDTO = settlementGroupMapper.toDto(settlementGroup);
        restSettlementGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlementGroupDTO))
            )
            .andExpect(status().isCreated());

        List<SettlementGroup> settlementGroupList = settlementGroupRepository.findAll();
        assertThat(settlementGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SettlementGroup testSettlementGroup = settlementGroupList.get(settlementGroupList.size() - 1);
        assertThat(testSettlementGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testSettlementGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSettlementGroup.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        verify(mockSettlementGroupSearchRepository, times(1)).save(testSettlementGroup);
    }

    @Test
    @Transactional
    void getAllSettlementGroups() throws Exception {
        // Initialize the database
        settlementGroupRepository.saveAndFlush(settlementGroup);

        restSettlementGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @Test
    @Transactional
    void getSettlementGroup() throws Exception {
        // Initialize the database
        settlementGroupRepository.saveAndFlush(settlementGroup);

        restSettlementGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, settlementGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settlementGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void searchSettlementGroup() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        settlementGroupRepository.saveAndFlush(settlementGroup);
        when(mockSettlementGroupSearchRepository.search("id:" + settlementGroup.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(settlementGroup), PageRequest.of(0, 1), 1));

        restSettlementGroupMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + settlementGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
