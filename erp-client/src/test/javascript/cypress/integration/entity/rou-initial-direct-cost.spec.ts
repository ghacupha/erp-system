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

describe('RouInitialDirectCost e2e test', () => {
  const rouInitialDirectCostPageUrl = '/rou-initial-direct-cost';
  const rouInitialDirectCostPageUrlPattern = new RegExp('/rou-initial-direct-cost(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouInitialDirectCostSample = { transactionDate: '2024-11-03', cost: 67209 };

  let rouInitialDirectCost: any;
  //let iFRS16LeaseContract: any;
  //let settlement: any;
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
      body: {"bookingId":"Supervisor","leaseTitle":"Lead","shortTitle":"multi-state Denar","description":"Island silver morph","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"43ea8379-ca2a-400a-a3f8-f9dae9284dda"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/settlements',
      body: {"paymentNumber":"experiences Balboa","paymentDate":"2022-02-03","paymentAmount":31470,"description":"fuchsia JSON leverage","notes":"bypassing Tasty","calculationFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","calculationFileContentType":"unknown","fileUploadToken":"monetize","compilationToken":"Infrastructure Plastic 1080p","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="},
    }).then(({ body }) => {
      settlement = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"invoice","accountName":"Investment Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"EQUITY","accountSubType":"OTHER_SHORT_LIVED_ASSET","dummyAccount":true},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-initial-direct-costs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-initial-direct-costs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-initial-direct-costs/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

    cy.intercept('GET', '/api/settlements', {
      statusCode: 200,
      body: [settlement],
    });

    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [transactionAccount],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (rouInitialDirectCost) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-initial-direct-costs/${rouInitialDirectCost.id}`,
      }).then(() => {
        rouInitialDirectCost = undefined;
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
    if (settlement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlements/${settlement.id}`,
      }).then(() => {
        settlement = undefined;
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

  it('RouInitialDirectCosts menu should load RouInitialDirectCosts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-initial-direct-cost');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouInitialDirectCost').should('exist');
    cy.url().should('match', rouInitialDirectCostPageUrlPattern);
  });

  describe('RouInitialDirectCost page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouInitialDirectCostPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouInitialDirectCost page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-initial-direct-cost/new$'));
        cy.getEntityCreateUpdateHeading('RouInitialDirectCost');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouInitialDirectCostPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-initial-direct-costs',
  
          body: {
            ...rouInitialDirectCostSample,
            leaseContract: iFRS16LeaseContract,
            settlementDetails: settlement,
            targetROUAccount: transactionAccount,
            transferAccount: transactionAccount,
          },
        }).then(({ body }) => {
          rouInitialDirectCost = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-initial-direct-costs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouInitialDirectCost],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouInitialDirectCostPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(rouInitialDirectCostPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RouInitialDirectCost page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouInitialDirectCost');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouInitialDirectCostPageUrlPattern);
      });

      it('edit button click should load edit RouInitialDirectCost page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouInitialDirectCost');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouInitialDirectCostPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of RouInitialDirectCost', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouInitialDirectCost').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouInitialDirectCostPageUrlPattern);

        rouInitialDirectCost = undefined;
      });
    });
  });

  describe('new RouInitialDirectCost page', () => {
    beforeEach(() => {
      cy.visit(`${rouInitialDirectCostPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouInitialDirectCost');
    });

    it.skip('should create an instance of RouInitialDirectCost', () => {
      cy.get(`[data-cy="transactionDate"]`).type('2024-11-03').should('have.value', '2024-11-03');

      cy.get(`[data-cy="description"]`).type('tertiary').should('have.value', 'tertiary');

      cy.get(`[data-cy="cost"]`).type('73345').should('have.value', '73345');

      cy.get(`[data-cy="referenceNumber"]`).type('36424').should('have.value', '36424');

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="settlementDetails"]`).select(1);
      cy.get(`[data-cy="targetROUAccount"]`).select(1);
      cy.get(`[data-cy="transferAccount"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouInitialDirectCost = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouInitialDirectCostPageUrlPattern);
    });
  });
});
