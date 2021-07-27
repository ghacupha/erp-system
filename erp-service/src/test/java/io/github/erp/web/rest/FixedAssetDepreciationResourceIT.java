package io.github.erp.web.rest;

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.FixedAssetDepreciation;
import io.github.erp.repository.FixedAssetDepreciationRepository;
import io.github.erp.repository.search.FixedAssetDepreciationSearchRepository;
import io.github.erp.service.FixedAssetDepreciationService;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import io.github.erp.service.mapper.FixedAssetDepreciationMapper;
import io.github.erp.service.dto.FixedAssetDepreciationCriteria;
import io.github.erp.service.FixedAssetDepreciationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.domain.enumeration.DepreciationRegime;
/**
 * Integration tests for the {@link FixedAssetDepreciationResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FixedAssetDepreciationResourceIT {

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;
    private static final Long SMALLER_ASSET_NUMBER = 1L - 1L;

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_ASSET_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEPRECIATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPRECIATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DEPRECIATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPRECIATION_AMOUNT = new BigDecimal(1 - 1);

    private static final DepreciationRegime DEFAULT_DEPRECIATION_REGIME = DepreciationRegime.STRAIGHT_LINE_BASIS;
    private static final DepreciationRegime UPDATED_DEPRECIATION_REGIME = DepreciationRegime.DECLINING_BALANCE_BASIS;

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    @Autowired
    private FixedAssetDepreciationRepository fixedAssetDepreciationRepository;

    @Autowired
    private FixedAssetDepreciationMapper fixedAssetDepreciationMapper;

    @Autowired
    private FixedAssetDepreciationService fixedAssetDepreciationService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FixedAssetDepreciationSearchRepositoryMockConfiguration
     */
    @Autowired
    private FixedAssetDepreciationSearchRepository mockFixedAssetDepreciationSearchRepository;

    @Autowired
    private FixedAssetDepreciationQueryService fixedAssetDepreciationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFixedAssetDepreciationMockMvc;

    private FixedAssetDepreciation fixedAssetDepreciation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetDepreciation createEntity(EntityManager em) {
        FixedAssetDepreciation fixedAssetDepreciation = new FixedAssetDepreciation();
        fixedAssetDepreciation.setAssetNumber(DEFAULT_ASSET_NUMBER);
        fixedAssetDepreciation.setServiceOutletCode(DEFAULT_SERVICE_OUTLET_CODE);
        fixedAssetDepreciation.setAssetTag(DEFAULT_ASSET_TAG);
        fixedAssetDepreciation.setAssetDescription(DEFAULT_ASSET_DESCRIPTION);
        fixedAssetDepreciation.setDepreciationDate(DEFAULT_DEPRECIATION_DATE);
        fixedAssetDepreciation.setAssetCategory(DEFAULT_ASSET_CATEGORY);
        fixedAssetDepreciation.setDepreciationAmount(DEFAULT_DEPRECIATION_AMOUNT);
        fixedAssetDepreciation.setDepreciationRegime(DEFAULT_DEPRECIATION_REGIME);
        fixedAssetDepreciation.setFileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN);
        fixedAssetDepreciation.setCompilationToken(DEFAULT_COMPILATION_TOKEN);
        return fixedAssetDepreciation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetDepreciation createUpdatedEntity(EntityManager em) {
        FixedAssetDepreciation fixedAssetDepreciation = new FixedAssetDepreciation();
        fixedAssetDepreciation.setAssetNumber(UPDATED_ASSET_NUMBER);
        fixedAssetDepreciation.setServiceOutletCode(UPDATED_SERVICE_OUTLET_CODE);
        fixedAssetDepreciation.setAssetTag(UPDATED_ASSET_TAG);
        fixedAssetDepreciation.setAssetDescription(UPDATED_ASSET_DESCRIPTION);
        fixedAssetDepreciation.setDepreciationDate(UPDATED_DEPRECIATION_DATE);
        fixedAssetDepreciation.setAssetCategory(UPDATED_ASSET_CATEGORY);
        fixedAssetDepreciation.setDepreciationAmount(UPDATED_DEPRECIATION_AMOUNT);
        fixedAssetDepreciation.setDepreciationRegime(UPDATED_DEPRECIATION_REGIME);
        fixedAssetDepreciation.setFileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);
        fixedAssetDepreciation.setCompilationToken(UPDATED_COMPILATION_TOKEN);
        return fixedAssetDepreciation;
    }

    @BeforeEach
    public void initTest() {
        fixedAssetDepreciation = createEntity(em);
    }

    @Test
    @Transactional
    public void createFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetDepreciationRepository.findAll().size();
        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);
        restFixedAssetDepreciationMockMvc.perform(post("/api/fixed-asset-depreciations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO)))
            .andExpect(status().isCreated());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeCreate + 1);
        FixedAssetDepreciation testFixedAssetDepreciation = fixedAssetDepreciationList.get(fixedAssetDepreciationList.size() - 1);
        assertThat(testFixedAssetDepreciation.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testFixedAssetDepreciation.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetDepreciation.getAssetTag()).isEqualTo(DEFAULT_ASSET_TAG);
        assertThat(testFixedAssetDepreciation.getAssetDescription()).isEqualTo(DEFAULT_ASSET_DESCRIPTION);
        assertThat(testFixedAssetDepreciation.getDepreciationDate()).isEqualTo(DEFAULT_DEPRECIATION_DATE);
        assertThat(testFixedAssetDepreciation.getAssetCategory()).isEqualTo(DEFAULT_ASSET_CATEGORY);
        assertThat(testFixedAssetDepreciation.getDepreciationAmount()).isEqualTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testFixedAssetDepreciation.getDepreciationRegime()).isEqualTo(DEFAULT_DEPRECIATION_REGIME);
        assertThat(testFixedAssetDepreciation.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetDepreciation.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(1)).save(testFixedAssetDepreciation);
    }

    @Test
    @Transactional
    public void createFixedAssetDepreciationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetDepreciationRepository.findAll().size();

        // Create the FixedAssetDepreciation with an existing ID
        fixedAssetDepreciation.setId(1L);
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixedAssetDepreciationMockMvc.perform(post("/api/fixed-asset-depreciations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeCreate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciations() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetDepreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(DEFAULT_DEPRECIATION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
    
    @Test
    @Transactional
    public void getFixedAssetDepreciation() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations/{id}", fixedAssetDepreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fixedAssetDepreciation.getId().intValue()))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.assetTag").value(DEFAULT_ASSET_TAG))
            .andExpect(jsonPath("$.assetDescription").value(DEFAULT_ASSET_DESCRIPTION))
            .andExpect(jsonPath("$.depreciationDate").value(DEFAULT_DEPRECIATION_DATE.toString()))
            .andExpect(jsonPath("$.assetCategory").value(DEFAULT_ASSET_CATEGORY))
            .andExpect(jsonPath("$.depreciationAmount").value(DEFAULT_DEPRECIATION_AMOUNT.intValue()))
            .andExpect(jsonPath("$.depreciationRegime").value(DEFAULT_DEPRECIATION_REGIME.toString()))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }


    @Test
    @Transactional
    public void getFixedAssetDepreciationsByIdFiltering() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        Long id = fixedAssetDepreciation.getId();

        defaultFixedAssetDepreciationShouldBeFound("id.equals=" + id);
        defaultFixedAssetDepreciationShouldNotBeFound("id.notEquals=" + id);

        defaultFixedAssetDepreciationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFixedAssetDepreciationShouldNotBeFound("id.greaterThan=" + id);

        defaultFixedAssetDepreciationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFixedAssetDepreciationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.equals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.equals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber not equals to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.notEquals=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber not equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.notEquals=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber in DEFAULT_ASSET_NUMBER or UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.in=" + DEFAULT_ASSET_NUMBER + "," + UPDATED_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber equals to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.in=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is not null
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.specified=true");

        // Get all the fixedAssetDepreciationList where assetNumber is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.greaterThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than or equal to UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.greaterThanOrEqual=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is less than or equal to DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.lessThanOrEqual=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is less than or equal to SMALLER_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.lessThanOrEqual=" + SMALLER_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is less than DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.lessThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is less than UPDATED_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.lessThan=" + UPDATED_ASSET_NUMBER);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than DEFAULT_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldNotBeFound("assetNumber.greaterThan=" + DEFAULT_ASSET_NUMBER);

        // Get all the fixedAssetDepreciationList where assetNumber is greater than SMALLER_ASSET_NUMBER
        defaultFixedAssetDepreciationShouldBeFound("assetNumber.greaterThan=" + SMALLER_ASSET_NUMBER);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode is not null
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.specified=true");

        // Get all the fixedAssetDepreciationList where serviceOutletCode is null
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the fixedAssetDepreciationList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultFixedAssetDepreciationShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetTagIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag equals to DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.equals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.equals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag not equals to DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.notEquals=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag not equals to UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.notEquals=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetTagIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag in DEFAULT_ASSET_TAG or UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.in=" + DEFAULT_ASSET_TAG + "," + UPDATED_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag equals to UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.in=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag is not null
        defaultFixedAssetDepreciationShouldBeFound("assetTag.specified=true");

        // Get all the fixedAssetDepreciationList where assetTag is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetTagContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag contains DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.contains=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag contains UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.contains=" + UPDATED_ASSET_TAG);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetTagNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetTag does not contain DEFAULT_ASSET_TAG
        defaultFixedAssetDepreciationShouldNotBeFound("assetTag.doesNotContain=" + DEFAULT_ASSET_TAG);

        // Get all the fixedAssetDepreciationList where assetTag does not contain UPDATED_ASSET_TAG
        defaultFixedAssetDepreciationShouldBeFound("assetTag.doesNotContain=" + UPDATED_ASSET_TAG);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.equals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.equals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription not equals to DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.notEquals=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription not equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.notEquals=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription in DEFAULT_ASSET_DESCRIPTION or UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.in=" + DEFAULT_ASSET_DESCRIPTION + "," + UPDATED_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription equals to UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.in=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription is not null
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.specified=true");

        // Get all the fixedAssetDepreciationList where assetDescription is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription contains DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.contains=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription contains UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.contains=" + UPDATED_ASSET_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetDescription does not contain DEFAULT_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldNotBeFound("assetDescription.doesNotContain=" + DEFAULT_ASSET_DESCRIPTION);

        // Get all the fixedAssetDepreciationList where assetDescription does not contain UPDATED_ASSET_DESCRIPTION
        defaultFixedAssetDepreciationShouldBeFound("assetDescription.doesNotContain=" + UPDATED_ASSET_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate equals to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.equals=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate equals to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.equals=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate not equals to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.notEquals=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate not equals to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.notEquals=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate in DEFAULT_DEPRECIATION_DATE or UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.in=" + DEFAULT_DEPRECIATION_DATE + "," + UPDATED_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate equals to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.in=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is not null
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.specified=true");

        // Get all the fixedAssetDepreciationList where depreciationDate is null
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than or equal to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than or equal to UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than or equal to DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than or equal to SMALLER_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.lessThanOrEqual=" + SMALLER_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.lessThan=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is less than UPDATED_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.lessThan=" + UPDATED_DEPRECIATION_DATE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than DEFAULT_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationDate.greaterThan=" + DEFAULT_DEPRECIATION_DATE);

        // Get all the fixedAssetDepreciationList where depreciationDate is greater than SMALLER_DEPRECIATION_DATE
        defaultFixedAssetDepreciationShouldBeFound("depreciationDate.greaterThan=" + SMALLER_DEPRECIATION_DATE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.equals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.equals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory not equals to DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.notEquals=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory not equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.notEquals=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory in DEFAULT_ASSET_CATEGORY or UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.in=" + DEFAULT_ASSET_CATEGORY + "," + UPDATED_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory equals to UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.in=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory is not null
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.specified=true");

        // Get all the fixedAssetDepreciationList where assetCategory is null
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetCategoryContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory contains DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.contains=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory contains UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.contains=" + UPDATED_ASSET_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByAssetCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where assetCategory does not contain DEFAULT_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldNotBeFound("assetCategory.doesNotContain=" + DEFAULT_ASSET_CATEGORY);

        // Get all the fixedAssetDepreciationList where assetCategory does not contain UPDATED_ASSET_CATEGORY
        defaultFixedAssetDepreciationShouldBeFound("assetCategory.doesNotContain=" + UPDATED_ASSET_CATEGORY);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.equals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.equals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount not equals to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.notEquals=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount not equals to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.notEquals=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount in DEFAULT_DEPRECIATION_AMOUNT or UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.in=" + DEFAULT_DEPRECIATION_AMOUNT + "," + UPDATED_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount equals to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.in=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is not null
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.specified=true");

        // Get all the fixedAssetDepreciationList where depreciationAmount is null
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than or equal to UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.greaterThanOrEqual=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than or equal to DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.lessThanOrEqual=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than or equal to SMALLER_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.lessThanOrEqual=" + SMALLER_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.lessThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is less than UPDATED_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.lessThan=" + UPDATED_DEPRECIATION_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than DEFAULT_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationAmount.greaterThan=" + DEFAULT_DEPRECIATION_AMOUNT);

        // Get all the fixedAssetDepreciationList where depreciationAmount is greater than SMALLER_DEPRECIATION_AMOUNT
        defaultFixedAssetDepreciationShouldBeFound("depreciationAmount.greaterThan=" + SMALLER_DEPRECIATION_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationRegimeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime equals to DEFAULT_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.equals=" + DEFAULT_DEPRECIATION_REGIME);

        // Get all the fixedAssetDepreciationList where depreciationRegime equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.equals=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationRegimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime not equals to DEFAULT_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.notEquals=" + DEFAULT_DEPRECIATION_REGIME);

        // Get all the fixedAssetDepreciationList where depreciationRegime not equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.notEquals=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationRegimeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime in DEFAULT_DEPRECIATION_REGIME or UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.in=" + DEFAULT_DEPRECIATION_REGIME + "," + UPDATED_DEPRECIATION_REGIME);

        // Get all the fixedAssetDepreciationList where depreciationRegime equals to UPDATED_DEPRECIATION_REGIME
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.in=" + UPDATED_DEPRECIATION_REGIME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByDepreciationRegimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where depreciationRegime is not null
        defaultFixedAssetDepreciationShouldBeFound("depreciationRegime.specified=true");

        // Get all the fixedAssetDepreciationList where depreciationRegime is null
        defaultFixedAssetDepreciationShouldNotBeFound("depreciationRegime.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken is not null
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.specified=true");

        // Get all the fixedAssetDepreciationList where fileUploadToken is null
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the fixedAssetDepreciationList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }


    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken is not null
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.specified=true");

        // Get all the fixedAssetDepreciationList where compilationToken is null
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    public void getAllFixedAssetDepreciationsByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        // Get all the fixedAssetDepreciationList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the fixedAssetDepreciationList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultFixedAssetDepreciationShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixedAssetDepreciationShouldBeFound(String filter) throws Exception {
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetDepreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(DEFAULT_DEPRECIATION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixedAssetDepreciationShouldNotBeFound(String filter) throws Exception {
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFixedAssetDepreciation() throws Exception {
        // Get the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc.perform(get("/api/fixed-asset-depreciations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFixedAssetDepreciation() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();

        // Update the fixedAssetDepreciation
        FixedAssetDepreciation updatedFixedAssetDepreciation = fixedAssetDepreciationRepository.findById(fixedAssetDepreciation.getId()).get();
        // Disconnect from session so that the updates on updatedFixedAssetDepreciation are not directly saved in db
        em.detach(updatedFixedAssetDepreciation);
        updatedFixedAssetDepreciation.setAssetNumber(UPDATED_ASSET_NUMBER);
        updatedFixedAssetDepreciation.setServiceOutletCode(UPDATED_SERVICE_OUTLET_CODE);
        updatedFixedAssetDepreciation.setAssetTag(UPDATED_ASSET_TAG);
        updatedFixedAssetDepreciation.setAssetDescription(UPDATED_ASSET_DESCRIPTION);
        updatedFixedAssetDepreciation.setDepreciationDate(UPDATED_DEPRECIATION_DATE);
        updatedFixedAssetDepreciation.setAssetCategory(UPDATED_ASSET_CATEGORY);
        updatedFixedAssetDepreciation.setDepreciationAmount(UPDATED_DEPRECIATION_AMOUNT);
        updatedFixedAssetDepreciation.setDepreciationRegime(UPDATED_DEPRECIATION_REGIME);
        updatedFixedAssetDepreciation.setFileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);
        updatedFixedAssetDepreciation.setCompilationToken(UPDATED_COMPILATION_TOKEN);
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(updatedFixedAssetDepreciation);

        restFixedAssetDepreciationMockMvc.perform(put("/api/fixed-asset-depreciations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO)))
            .andExpect(status().isOk());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetDepreciation testFixedAssetDepreciation = fixedAssetDepreciationList.get(fixedAssetDepreciationList.size() - 1);
        assertThat(testFixedAssetDepreciation.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testFixedAssetDepreciation.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testFixedAssetDepreciation.getAssetTag()).isEqualTo(UPDATED_ASSET_TAG);
        assertThat(testFixedAssetDepreciation.getAssetDescription()).isEqualTo(UPDATED_ASSET_DESCRIPTION);
        assertThat(testFixedAssetDepreciation.getDepreciationDate()).isEqualTo(UPDATED_DEPRECIATION_DATE);
        assertThat(testFixedAssetDepreciation.getAssetCategory()).isEqualTo(UPDATED_ASSET_CATEGORY);
        assertThat(testFixedAssetDepreciation.getDepreciationAmount()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testFixedAssetDepreciation.getDepreciationRegime()).isEqualTo(UPDATED_DEPRECIATION_REGIME);
        assertThat(testFixedAssetDepreciation.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testFixedAssetDepreciation.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(1)).save(testFixedAssetDepreciation);
    }

    @Test
    @Transactional
    public void updateNonExistingFixedAssetDepreciation() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetDepreciationRepository.findAll().size();

        // Create the FixedAssetDepreciation
        FixedAssetDepreciationDTO fixedAssetDepreciationDTO = fixedAssetDepreciationMapper.toDto(fixedAssetDepreciation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetDepreciationMockMvc.perform(put("/api/fixed-asset-depreciations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetDepreciationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetDepreciation in the database
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(0)).save(fixedAssetDepreciation);
    }

    @Test
    @Transactional
    public void deleteFixedAssetDepreciation() throws Exception {
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);

        int databaseSizeBeforeDelete = fixedAssetDepreciationRepository.findAll().size();

        // Delete the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc.perform(delete("/api/fixed-asset-depreciations/{id}", fixedAssetDepreciation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FixedAssetDepreciation> fixedAssetDepreciationList = fixedAssetDepreciationRepository.findAll();
        assertThat(fixedAssetDepreciationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FixedAssetDepreciation in Elasticsearch
        verify(mockFixedAssetDepreciationSearchRepository, times(1)).deleteById(fixedAssetDepreciation.getId());
    }

    @Test
    @Transactional
    public void searchFixedAssetDepreciation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fixedAssetDepreciationRepository.saveAndFlush(fixedAssetDepreciation);
        when(mockFixedAssetDepreciationSearchRepository.search(queryStringQuery("id:" + fixedAssetDepreciation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fixedAssetDepreciation), PageRequest.of(0, 1), 1));

        // Search the fixedAssetDepreciation
        restFixedAssetDepreciationMockMvc.perform(get("/api/_search/fixed-asset-depreciations?query=id:" + fixedAssetDepreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetDepreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].assetTag").value(hasItem(DEFAULT_ASSET_TAG)))
            .andExpect(jsonPath("$.[*].assetDescription").value(hasItem(DEFAULT_ASSET_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].depreciationDate").value(hasItem(DEFAULT_DEPRECIATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assetCategory").value(hasItem(DEFAULT_ASSET_CATEGORY)))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(DEFAULT_DEPRECIATION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].depreciationRegime").value(hasItem(DEFAULT_DEPRECIATION_REGIME.toString())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
