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

describe('LeasePayment e2e test', () => {
  const leasePaymentPageUrl = '/lease-payment';
  const leasePaymentPageUrlPattern = new RegExp('/lease-payment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leasePaymentSample = {};

  let leasePayment: any;
  //let iFRS16LeaseContract: any;

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
      body: {"bookingId":"seamless","leaseTitle":"paradigms holistic","shortTitle":"architecture","description":"driver Card","inceptionDate":"2024-03-07","commencementDate":"2024-03-07","serialNumber":"940c0eac-7d62-402b-88be-4642f9bc3262"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-payments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-payments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-payments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

  });
   */

  afterEach(() => {
    if (leasePayment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-payments/${leasePayment.id}`,
      }).then(() => {
        leasePayment = undefined;
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
  });
   */

  it('LeasePayments menu should load LeasePayments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-payment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeasePayment').should('exist');
    cy.url().should('match', leasePaymentPageUrlPattern);
  });

  describe('LeasePayment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leasePaymentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeasePayment page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-payment/new$'));
        cy.getEntityCreateUpdateHeading('LeasePayment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePaymentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-payments',
  
          body: {
            ...leasePaymentSample,
            leaseContract: iFRS16LeaseContract,
          },
        }).then(({ body }) => {
          leasePayment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-payments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leasePayment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leasePaymentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leasePaymentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeasePayment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leasePayment');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePaymentPageUrlPattern);
      });

      it('edit button click should load edit LeasePayment page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeasePayment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePaymentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeasePayment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leasePayment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePaymentPageUrlPattern);

        leasePayment = undefined;
      });
    });
  });

  describe('new LeasePayment page', () => {
    beforeEach(() => {
      cy.visit(`${leasePaymentPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeasePayment');
    });

    it.skip('should create an instance of LeasePayment', () => {
      cy.get(`[data-cy="paymentDate"]`).type('2024-06-17').should('have.value', '2024-06-17');

      cy.get(`[data-cy="paymentAmount"]`).type('99826').should('have.value', '99826');

      cy.get(`[data-cy="leaseContract"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leasePayment = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leasePaymentPageUrlPattern);
    });
  });
});
