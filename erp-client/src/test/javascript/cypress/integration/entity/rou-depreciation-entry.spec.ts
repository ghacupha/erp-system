///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('RouDepreciationEntry e2e test', () => {
  const rouDepreciationEntryPageUrl = '/rou-depreciation-entry';
  const rouDepreciationEntryPageUrlPattern = new RegExp('/rou-depreciation-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouDepreciationEntrySample = {
    depreciationAmount: 67536,
    outstandingAmount: 5663,
    rouDepreciationIdentifier: 'b2c44037-47af-4365-a7f6-07f801b6ba03',
  };

  let rouDepreciationEntry: any;
  //let transactionAccount: any;
  //let assetCategory: any;
  //let iFRS16LeaseContract: any;
  //let rouModelMetadata: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"invoice Planner","accountName":"Checking Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"LIABILITY","accountSubType":"SETTLEMENT_ASSET","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-categories',
      body: {"assetCategoryName":"FTP Generic Shirt","description":"Home","notes":"Singapore Ameliorated","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","depreciationRateYearly":41207},
    }).then(({ body }) => {
      assetCategory = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/ifrs-16-lease-contracts',
      body: {"bookingId":"incubate Home Direct","leaseTitle":"hard fuchsia","shortTitle":"Books Web","description":"Gorgeous","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"badf0485-da12-4b90-badb-a81157ab3976"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/rou-model-metadata',
      body: {"modelTitle":"Gateway Generic","modelVersion":73149,"description":"Concrete Personal","leaseTermPeriods":49227,"leaseAmount":92933,"rouModelReference":"821cd3ee-5b52-44fd-909e-d41658bb1b0a","commencementDate":"2024-03-07","expirationDate":"2024-03-06","hasBeenFullyAmortised":false,"hasBeenDecommissioned":false},
    }).then(({ body }) => {
      rouModelMetadata = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-depreciation-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-depreciation-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-depreciation-entries/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [transactionAccount],
    });

    cy.intercept('GET', '/api/asset-categories', {
      statusCode: 200,
      body: [assetCategory],
    });

    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

    cy.intercept('GET', '/api/rou-model-metadata', {
      statusCode: 200,
      body: [rouModelMetadata],
    });

  });
   */

  afterEach(() => {
    if (rouDepreciationEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-depreciation-entries/${rouDepreciationEntry.id}`,
      }).then(() => {
        rouDepreciationEntry = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (transactionAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-accounts/${transactionAccount.id}`,
      }).then(() => {
        transactionAccount = undefined;
      });
    }
    if (assetCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-categories/${assetCategory.id}`,
      }).then(() => {
        assetCategory = undefined;
      });
    }
    if (iFRS16LeaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ifrs-16-lease-contracts/${iFRS16LeaseContract.id}`,
      }).then(() => {
        iFRS16LeaseContract = undefined;
      });
    }
    if (rouModelMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-model-metadata/${rouModelMetadata.id}`,
      }).then(() => {
        rouModelMetadata = undefined;
      });
    }
  });
   */

  it('RouDepreciationEntries menu should load RouDepreciationEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-depreciation-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouDepreciationEntry').should('exist');
    cy.url().should('match', rouDepreciationEntryPageUrlPattern);
  });

  describe('RouDepreciationEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouDepreciationEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouDepreciationEntry page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-depreciation-entry/new$'));
        cy.getEntityCreateUpdateHeading('RouDepreciationEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-depreciation-entries',
  
          body: {
            ...rouDepreciationEntrySample,
            debitAccount: transactionAccount,
            creditAccount: transactionAccount,
            assetCategory: assetCategory,
            leaseContract: iFRS16LeaseContract,
            rouMetadata: rouModelMetadata,
          },
        }).then(({ body }) => {
          rouDepreciationEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-depreciation-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouDepreciationEntry],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouDepreciationEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(rouDepreciationEntryPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouDepreciationEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouDepreciationEntry');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryPageUrlPattern);
      });

      it('edit button click should load edit RouDepreciationEntry page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouDepreciationEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of RouDepreciationEntry', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouDepreciationEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryPageUrlPattern);

        rouDepreciationEntry = undefined;
      });
    });
  });

  describe('new RouDepreciationEntry page', () => {
    beforeEach(() => {
      cy.visit(`${rouDepreciationEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouDepreciationEntry');
    });

    it.skip('should create an instance of RouDepreciationEntry', () => {
      cy.get(`[data-cy="description"]`).type('array Wall matrix').should('have.value', 'array Wall matrix');

      cy.get(`[data-cy="depreciationAmount"]`).type('72626').should('have.value', '72626');

      cy.get(`[data-cy="outstandingAmount"]`).type('93012').should('have.value', '93012');

      cy.get(`[data-cy="rouAssetIdentifier"]`)
        .type('2915100f-6dd3-43ba-9c9b-fc92557f9e73')
        .invoke('val')
        .should('match', new RegExp('2915100f-6dd3-43ba-9c9b-fc92557f9e73'));

      cy.get(`[data-cy="rouDepreciationIdentifier"]`)
        .type('d49fbe99-53c7-4615-8c05-0f3bf80be840')
        .invoke('val')
        .should('match', new RegExp('d49fbe99-53c7-4615-8c05-0f3bf80be840'));

      cy.get(`[data-cy="sequenceNumber"]`).type('70969').should('have.value', '70969');

      cy.get(`[data-cy="invalidated"]`).should('not.be.checked');
      cy.get(`[data-cy="invalidated"]`).click().should('be.checked');

      cy.get(`[data-cy="debitAccount"]`).select(1);
      cy.get(`[data-cy="creditAccount"]`).select(1);
      cy.get(`[data-cy="assetCategory"]`).select(1);
      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="rouMetadata"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouDepreciationEntry = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouDepreciationEntryPageUrlPattern);
    });
  });
});
