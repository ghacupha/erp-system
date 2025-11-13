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

describe('LeaseLiability e2e test', () => {
  const leaseLiabilityPageUrl = '/lease-liability';
  const leaseLiabilityPageUrlPattern = new RegExp('/lease-liability(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilitySample = {
    leaseId: 'brand',
    liabilityAmount: 61154,
    startDate: '2024-06-17',
    endDate: '2024-06-18',
    interestRate: 62970,
  };

  let leaseLiability: any;
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
      body: {"bookingId":"hacking Soft","leaseTitle":"Highway Avon Groves","shortTitle":"Chicken","description":"Investment Ball","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"2ba9a72d-d078-4a70-8a60-25deb60a5d24"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liabilities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liabilities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liabilities/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/lease-amortization-calculations', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/ifrs-16-lease-contracts', {
      statusCode: 200,
      body: [iFRS16LeaseContract],
    });

  });
   */

  afterEach(() => {
    if (leaseLiability) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liabilities/${leaseLiability.id}`,
      }).then(() => {
        leaseLiability = undefined;
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

  it('LeaseLiabilities menu should load LeaseLiabilities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiability').should('exist');
    cy.url().should('match', leaseLiabilityPageUrlPattern);
  });

  describe('LeaseLiability page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiability page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiability');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liabilities',
  
          body: {
            ...leaseLiabilitySample,
            leaseContract: iFRS16LeaseContract,
          },
        }).then(({ body }) => {
          leaseLiability = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liabilities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiability],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseLiabilityPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiability page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiability');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiability page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiability');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseLiability', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiability').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityPageUrlPattern);

        leaseLiability = undefined;
      });
    });
  });

  describe('new LeaseLiability page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiability');
    });

    it.skip('should create an instance of LeaseLiability', () => {
      cy.get(`[data-cy="leaseId"]`).type('Granite Markets Centralized').should('have.value', 'Granite Markets Centralized');

      cy.get(`[data-cy="liabilityAmount"]`).type('17610').should('have.value', '17610');

      cy.get(`[data-cy="startDate"]`).type('2024-06-17').should('have.value', '2024-06-17');

      cy.get(`[data-cy="endDate"]`).type('2024-06-18').should('have.value', '2024-06-18');

      cy.get(`[data-cy="interestRate"]`).type('24413').should('have.value', '24413');

      cy.get(`[data-cy="leaseContract"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiability = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityPageUrlPattern);
    });
  });
});
