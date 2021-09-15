package io.github.erp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.FileType;
import io.github.erp.domain.FileUpload;
import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.domain.FixedAssetDepreciation;
import io.github.erp.domain.FixedAssetNetBookValue;
import io.github.erp.domain.Invoice;
import io.github.erp.domain.MessageToken;
import io.github.erp.domain.Payment;
import io.github.erp.domain.PaymentCalculation;
import io.github.erp.domain.PaymentCategory;
import io.github.erp.domain.PaymentRequisition;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TaxReference;
import io.github.erp.domain.TaxRule;
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

    @Test
    @Transactional
    void getAllPlaceholdersByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        placeholder.addDealer(dealer);
        placeholderRepository.saveAndFlush(placeholder);
        Long dealerId = dealer.getId();

        // Get all the placeholderList where dealer equals to dealerId
        defaultPlaceholderShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the placeholderList where dealer equals to (dealerId + 1)
        defaultPlaceholderShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        FileType fileType;
        if (TestUtil.findAll(em, FileType.class).isEmpty()) {
            fileType = FileTypeResourceIT.createEntity(em);
            em.persist(fileType);
            em.flush();
        } else {
            fileType = TestUtil.findAll(em, FileType.class).get(0);
        }
        em.persist(fileType);
        em.flush();
        placeholder.addFileType(fileType);
        placeholderRepository.saveAndFlush(placeholder);
        Long fileTypeId = fileType.getId();

        // Get all the placeholderList where fileType equals to fileTypeId
        defaultPlaceholderShouldBeFound("fileTypeId.equals=" + fileTypeId);

        // Get all the placeholderList where fileType equals to (fileTypeId + 1)
        defaultPlaceholderShouldNotBeFound("fileTypeId.equals=" + (fileTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFileUploadIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        FileUpload fileUpload;
        if (TestUtil.findAll(em, FileUpload.class).isEmpty()) {
            fileUpload = FileUploadResourceIT.createEntity(em);
            em.persist(fileUpload);
            em.flush();
        } else {
            fileUpload = TestUtil.findAll(em, FileUpload.class).get(0);
        }
        em.persist(fileUpload);
        em.flush();
        placeholder.addFileUpload(fileUpload);
        placeholderRepository.saveAndFlush(placeholder);
        Long fileUploadId = fileUpload.getId();

        // Get all the placeholderList where fileUpload equals to fileUploadId
        defaultPlaceholderShouldBeFound("fileUploadId.equals=" + fileUploadId);

        // Get all the placeholderList where fileUpload equals to (fileUploadId + 1)
        defaultPlaceholderShouldNotBeFound("fileUploadId.equals=" + (fileUploadId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFixedAssetAcquisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        FixedAssetAcquisition fixedAssetAcquisition;
        if (TestUtil.findAll(em, FixedAssetAcquisition.class).isEmpty()) {
            fixedAssetAcquisition = FixedAssetAcquisitionResourceIT.createEntity(em);
            em.persist(fixedAssetAcquisition);
            em.flush();
        } else {
            fixedAssetAcquisition = TestUtil.findAll(em, FixedAssetAcquisition.class).get(0);
        }
        em.persist(fixedAssetAcquisition);
        em.flush();
        placeholder.addFixedAssetAcquisition(fixedAssetAcquisition);
        placeholderRepository.saveAndFlush(placeholder);
        Long fixedAssetAcquisitionId = fixedAssetAcquisition.getId();

        // Get all the placeholderList where fixedAssetAcquisition equals to fixedAssetAcquisitionId
        defaultPlaceholderShouldBeFound("fixedAssetAcquisitionId.equals=" + fixedAssetAcquisitionId);

        // Get all the placeholderList where fixedAssetAcquisition equals to (fixedAssetAcquisitionId + 1)
        defaultPlaceholderShouldNotBeFound("fixedAssetAcquisitionId.equals=" + (fixedAssetAcquisitionId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFixedAssetDepreciationIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        FixedAssetDepreciation fixedAssetDepreciation;
        if (TestUtil.findAll(em, FixedAssetDepreciation.class).isEmpty()) {
            fixedAssetDepreciation = FixedAssetDepreciationResourceIT.createEntity(em);
            em.persist(fixedAssetDepreciation);
            em.flush();
        } else {
            fixedAssetDepreciation = TestUtil.findAll(em, FixedAssetDepreciation.class).get(0);
        }
        em.persist(fixedAssetDepreciation);
        em.flush();
        placeholder.addFixedAssetDepreciation(fixedAssetDepreciation);
        placeholderRepository.saveAndFlush(placeholder);
        Long fixedAssetDepreciationId = fixedAssetDepreciation.getId();

        // Get all the placeholderList where fixedAssetDepreciation equals to fixedAssetDepreciationId
        defaultPlaceholderShouldBeFound("fixedAssetDepreciationId.equals=" + fixedAssetDepreciationId);

        // Get all the placeholderList where fixedAssetDepreciation equals to (fixedAssetDepreciationId + 1)
        defaultPlaceholderShouldNotBeFound("fixedAssetDepreciationId.equals=" + (fixedAssetDepreciationId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByFixedAssetNetBookValueIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        FixedAssetNetBookValue fixedAssetNetBookValue;
        if (TestUtil.findAll(em, FixedAssetNetBookValue.class).isEmpty()) {
            fixedAssetNetBookValue = FixedAssetNetBookValueResourceIT.createEntity(em);
            em.persist(fixedAssetNetBookValue);
            em.flush();
        } else {
            fixedAssetNetBookValue = TestUtil.findAll(em, FixedAssetNetBookValue.class).get(0);
        }
        em.persist(fixedAssetNetBookValue);
        em.flush();
        placeholder.addFixedAssetNetBookValue(fixedAssetNetBookValue);
        placeholderRepository.saveAndFlush(placeholder);
        Long fixedAssetNetBookValueId = fixedAssetNetBookValue.getId();

        // Get all the placeholderList where fixedAssetNetBookValue equals to fixedAssetNetBookValueId
        defaultPlaceholderShouldBeFound("fixedAssetNetBookValueId.equals=" + fixedAssetNetBookValueId);

        // Get all the placeholderList where fixedAssetNetBookValue equals to (fixedAssetNetBookValueId + 1)
        defaultPlaceholderShouldNotBeFound("fixedAssetNetBookValueId.equals=" + (fixedAssetNetBookValueId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        Invoice invoice;
        if (TestUtil.findAll(em, Invoice.class).isEmpty()) {
            invoice = InvoiceResourceIT.createEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, Invoice.class).get(0);
        }
        em.persist(invoice);
        em.flush();
        placeholder.addInvoice(invoice);
        placeholderRepository.saveAndFlush(placeholder);
        Long invoiceId = invoice.getId();

        // Get all the placeholderList where invoice equals to invoiceId
        defaultPlaceholderShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the placeholderList where invoice equals to (invoiceId + 1)
        defaultPlaceholderShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByMessageTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        MessageToken messageToken;
        if (TestUtil.findAll(em, MessageToken.class).isEmpty()) {
            messageToken = MessageTokenResourceIT.createEntity(em);
            em.persist(messageToken);
            em.flush();
        } else {
            messageToken = TestUtil.findAll(em, MessageToken.class).get(0);
        }
        em.persist(messageToken);
        em.flush();
        placeholder.addMessageToken(messageToken);
        placeholderRepository.saveAndFlush(placeholder);
        Long messageTokenId = messageToken.getId();

        // Get all the placeholderList where messageToken equals to messageTokenId
        defaultPlaceholderShouldBeFound("messageTokenId.equals=" + messageTokenId);

        // Get all the placeholderList where messageToken equals to (messageTokenId + 1)
        defaultPlaceholderShouldNotBeFound("messageTokenId.equals=" + (messageTokenId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        em.persist(payment);
        em.flush();
        placeholder.addPayment(payment);
        placeholderRepository.saveAndFlush(placeholder);
        Long paymentId = payment.getId();

        // Get all the placeholderList where payment equals to paymentId
        defaultPlaceholderShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the placeholderList where payment equals to (paymentId + 1)
        defaultPlaceholderShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByPaymentCalculationIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        PaymentCalculation paymentCalculation;
        if (TestUtil.findAll(em, PaymentCalculation.class).isEmpty()) {
            paymentCalculation = PaymentCalculationResourceIT.createEntity(em);
            em.persist(paymentCalculation);
            em.flush();
        } else {
            paymentCalculation = TestUtil.findAll(em, PaymentCalculation.class).get(0);
        }
        em.persist(paymentCalculation);
        em.flush();
        placeholder.addPaymentCalculation(paymentCalculation);
        placeholderRepository.saveAndFlush(placeholder);
        Long paymentCalculationId = paymentCalculation.getId();

        // Get all the placeholderList where paymentCalculation equals to paymentCalculationId
        defaultPlaceholderShouldBeFound("paymentCalculationId.equals=" + paymentCalculationId);

        // Get all the placeholderList where paymentCalculation equals to (paymentCalculationId + 1)
        defaultPlaceholderShouldNotBeFound("paymentCalculationId.equals=" + (paymentCalculationId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByPaymentRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        PaymentRequisition paymentRequisition;
        if (TestUtil.findAll(em, PaymentRequisition.class).isEmpty()) {
            paymentRequisition = PaymentRequisitionResourceIT.createEntity(em);
            em.persist(paymentRequisition);
            em.flush();
        } else {
            paymentRequisition = TestUtil.findAll(em, PaymentRequisition.class).get(0);
        }
        em.persist(paymentRequisition);
        em.flush();
        placeholder.addPaymentRequisition(paymentRequisition);
        placeholderRepository.saveAndFlush(placeholder);
        Long paymentRequisitionId = paymentRequisition.getId();

        // Get all the placeholderList where paymentRequisition equals to paymentRequisitionId
        defaultPlaceholderShouldBeFound("paymentRequisitionId.equals=" + paymentRequisitionId);

        // Get all the placeholderList where paymentRequisition equals to (paymentRequisitionId + 1)
        defaultPlaceholderShouldNotBeFound("paymentRequisitionId.equals=" + (paymentRequisitionId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByPaymentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        PaymentCategory paymentCategory;
        if (TestUtil.findAll(em, PaymentCategory.class).isEmpty()) {
            paymentCategory = PaymentCategoryResourceIT.createEntity(em);
            em.persist(paymentCategory);
            em.flush();
        } else {
            paymentCategory = TestUtil.findAll(em, PaymentCategory.class).get(0);
        }
        em.persist(paymentCategory);
        em.flush();
        placeholder.addPaymentCategory(paymentCategory);
        placeholderRepository.saveAndFlush(placeholder);
        Long paymentCategoryId = paymentCategory.getId();

        // Get all the placeholderList where paymentCategory equals to paymentCategoryId
        defaultPlaceholderShouldBeFound("paymentCategoryId.equals=" + paymentCategoryId);

        // Get all the placeholderList where paymentCategory equals to (paymentCategoryId + 1)
        defaultPlaceholderShouldNotBeFound("paymentCategoryId.equals=" + (paymentCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTaxReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        TaxReference taxReference;
        if (TestUtil.findAll(em, TaxReference.class).isEmpty()) {
            taxReference = TaxReferenceResourceIT.createEntity(em);
            em.persist(taxReference);
            em.flush();
        } else {
            taxReference = TestUtil.findAll(em, TaxReference.class).get(0);
        }
        em.persist(taxReference);
        em.flush();
        placeholder.addTaxReference(taxReference);
        placeholderRepository.saveAndFlush(placeholder);
        Long taxReferenceId = taxReference.getId();

        // Get all the placeholderList where taxReference equals to taxReferenceId
        defaultPlaceholderShouldBeFound("taxReferenceId.equals=" + taxReferenceId);

        // Get all the placeholderList where taxReference equals to (taxReferenceId + 1)
        defaultPlaceholderShouldNotBeFound("taxReferenceId.equals=" + (taxReferenceId + 1));
    }

    @Test
    @Transactional
    void getAllPlaceholdersByTaxRuleIsEqualToSomething() throws Exception {
        // Initialize the database
        placeholderRepository.saveAndFlush(placeholder);
        TaxRule taxRule;
        if (TestUtil.findAll(em, TaxRule.class).isEmpty()) {
            taxRule = TaxRuleResourceIT.createEntity(em);
            em.persist(taxRule);
            em.flush();
        } else {
            taxRule = TestUtil.findAll(em, TaxRule.class).get(0);
        }
        em.persist(taxRule);
        em.flush();
        placeholder.addTaxRule(taxRule);
        placeholderRepository.saveAndFlush(placeholder);
        Long taxRuleId = taxRule.getId();

        // Get all the placeholderList where taxRule equals to taxRuleId
        defaultPlaceholderShouldBeFound("taxRuleId.equals=" + taxRuleId);

        // Get all the placeholderList where taxRule equals to (taxRuleId + 1)
        defaultPlaceholderShouldNotBeFound("taxRuleId.equals=" + (taxRuleId + 1));
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
