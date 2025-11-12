///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

describe('TALeaseRecognitionRule e2e test', () => {
  const tALeaseRecognitionRulePageUrl = '/ta-lease-recognition-rule';
  const tALeaseRecognitionRulePageUrlPattern = new RegExp('/ta-lease-recognition-rule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const tALeaseRecognitionRuleSample = { name: 'PNG Forward', identifier: 'a6c79325-96ef-4a87-ba51-10b154dae296' };

  let tALeaseRecognitionRule: any;
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
      body: {"bookingId":"Loan PCI Refined","leaseTitle":"applications","shortTitle":"models 6th","description":"moderator","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"a284d468-4af9-467e-996a-503d197a5162"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-accounts',
      body: {"accountNumber":"withdrawal Serbian","accountName":"Home Loan Account","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","notesContentType":"unknown","accountType":"LIABILITY","accountSubType":"OTHER_COMPREHENSIVE_INCOME","dummyAccount":false},
    }).then(({ body }) => {
      transactionAccount = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/ta-lease-recognition-rules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ta-lease-recognition-rules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ta-lease-recognition-rules/*').as('deleteEntityRequest');
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

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (tALeaseRecognitionRule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ta-lease-recognition-rules/${tALeaseRecognitionRule.id}`,
      }).then(() => {
        tALeaseRecognitionRule = undefined;
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

  it('TALeaseRecognitionRules menu should load TALeaseRecognitionRules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ta-lease-recognition-rule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TALeaseRecognitionRule').should('exist');
    cy.url().should('match', tALeaseRecognitionRulePageUrlPattern);
  });

  describe('TALeaseRecognitionRule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tALeaseRecognitionRulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TALeaseRecognitionRule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ta-lease-recognition-rule/new$'));
        cy.getEntityCreateUpdateHeading('TALeaseRecognitionRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRecognitionRulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ta-lease-recognition-rules',
  
          body: {
            ...tALeaseRecognitionRuleSample,
            leaseContract: iFRS16LeaseContract,
            debit: transactionAccount,
            credit: transactionAccount,
          },
        }).then(({ body }) => {
          tALeaseRecognitionRule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ta-lease-recognition-rules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [tALeaseRecognitionRule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tALeaseRecognitionRulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(tALeaseRecognitionRulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TALeaseRecognitionRule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tALeaseRecognitionRule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRecognitionRulePageUrlPattern);
      });

      it('edit button click should load edit TALeaseRecognitionRule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TALeaseRecognitionRule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRecognitionRulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of TALeaseRecognitionRule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tALeaseRecognitionRule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tALeaseRecognitionRulePageUrlPattern);

        tALeaseRecognitionRule = undefined;
      });
    });
  });

  describe('new TALeaseRecognitionRule page', () => {
    beforeEach(() => {
      cy.visit(`${tALeaseRecognitionRulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TALeaseRecognitionRule');
    });

    it.skip('should create an instance of TALeaseRecognitionRule', () => {
      cy.get(`[data-cy="name"]`).type('B2B Loan').should('have.value', 'B2B Loan');

      cy.get(`[data-cy="identifier"]`)
        .type('0914491f-772a-4fc8-a36c-b9f08dd658bf')
        .invoke('val')
        .should('match', new RegExp('0914491f-772a-4fc8-a36c-b9f08dd658bf'));

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="debit"]`).select(1);
      cy.get(`[data-cy="credit"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tALeaseRecognitionRule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tALeaseRecognitionRulePageUrlPattern);
    });
  });
});
