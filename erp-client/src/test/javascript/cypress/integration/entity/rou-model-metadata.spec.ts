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

describe('RouModelMetadata e2e test', () => {
  const rouModelMetadataPageUrl = '/rou-model-metadata';
  const rouModelMetadataPageUrlPattern = new RegExp('/rou-model-metadata(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouModelMetadataSample = {
    modelTitle: 'strategy Money',
    modelVersion: 48721,
    leaseTermPeriods: 99244,
    leaseAmount: 16313,
    rouModelReference: '306c4306-0787-42a3-b59a-edc168392b46',
  };

  let rouModelMetadata: any;
  //let iFRS16LeaseContract: any;
  //let transactionAccount: any;

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
      url: '/api/ifrs-16-lease-contracts',
      body: {"bookingId":"virtual Soft","leaseTitle":"Loaf Granite","shortTitle":"Mandatory program","description":"Berkshire Lead Account","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"68bd7655-a93d-49e2-a411-b40686678f4b"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"synthesizing card Kenyan","accountName":"Auto Loan Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"ASSET","accountSubType":"OTHER_COMPREHENSIVE_INCOME","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-model-metadata+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-model-metadata').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-model-metadata/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [transactionAccount],
    });

    cy.intercept('GET', '/api/asset-categories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (rouModelMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-model-metadata/${rouModelMetadata.id}`,
      }).then(() => {
        rouModelMetadata = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (iFRS16LeaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ifrs-16-lease-contracts/${iFRS16LeaseContract.id}`,
      }).then(() => {
        iFRS16LeaseContract = undefined;
      });
    }
    if (transactionAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-accounts/${transactionAccount.id}`,
      }).then(() => {
        transactionAccount = undefined;
      });
    }
  });
   */

  it('RouModelMetadata menu should load RouModelMetadata page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-model-metadata');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouModelMetadata').should('exist');
    cy.url().should('match', rouModelMetadataPageUrlPattern);
  });

  describe('RouModelMetadata page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouModelMetadataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouModelMetadata page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-model-metadata/new$'));
        cy.getEntityCreateUpdateHeading('RouModelMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouModelMetadataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-model-metadata',
  
          body: {
            ...rouModelMetadataSample,
            ifrs16LeaseContract: iFRS16LeaseContract,
            assetAccount: transactionAccount,
            depreciationAccount: transactionAccount,
            accruedDepreciationAccount: transactionAccount,
          },
        }).then(({ body }) => {
          rouModelMetadata = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-model-metadata+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouModelMetadata],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouModelMetadataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(rouModelMetadataPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouModelMetadata page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouModelMetadata');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouModelMetadataPageUrlPattern);
      });

      it('edit button click should load edit RouModelMetadata page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouModelMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouModelMetadataPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of RouModelMetadata', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouModelMetadata').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouModelMetadataPageUrlPattern);

        rouModelMetadata = undefined;
      });
    });
  });

  describe('new RouModelMetadata page', () => {
    beforeEach(() => {
      cy.visit(`${rouModelMetadataPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouModelMetadata');
    });

    it.skip('should create an instance of RouModelMetadata', () => {
      cy.get(`[data-cy="modelTitle"]`).type('orchestrate').should('have.value', 'orchestrate');

      cy.get(`[data-cy="modelVersion"]`).type('6612').should('have.value', '6612');

      cy.get(`[data-cy="description"]`).type('Fresh').should('have.value', 'Fresh');

      cy.get(`[data-cy="leaseTermPeriods"]`).type('36068').should('have.value', '36068');

      cy.get(`[data-cy="leaseAmount"]`).type('52282').should('have.value', '52282');

      cy.get(`[data-cy="rouModelReference"]`)
        .type('dba8087c-189b-4dc5-b268-94098486093e')
        .invoke('val')
        .should('match', new RegExp('dba8087c-189b-4dc5-b268-94098486093e'));

      cy.get(`[data-cy="commencementDate"]`).type('2024-03-06').should('have.value', '2024-03-06');

      cy.get(`[data-cy="expirationDate"]`).type('2024-03-06').should('have.value', '2024-03-06');

      cy.get(`[data-cy="hasBeenFullyAmortised"]`).should('not.be.checked');
      cy.get(`[data-cy="hasBeenFullyAmortised"]`).click().should('be.checked');

      cy.get(`[data-cy="hasBeenDecommissioned"]`).should('not.be.checked');
      cy.get(`[data-cy="hasBeenDecommissioned"]`).click().should('be.checked');

      cy.get(`[data-cy="ifrs16LeaseContract"]`).select(1);
      cy.get(`[data-cy="assetAccount"]`).select(1);
      cy.get(`[data-cy="depreciationAccount"]`).select(1);
      cy.get(`[data-cy="accruedDepreciationAccount"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouModelMetadata = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouModelMetadataPageUrlPattern);
    });
  });
});
