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

describe('LeaseAmortizationCalculation e2e test', () => {
  const leaseAmortizationCalculationPageUrl = '/lease-amortization-calculation';
  const leaseAmortizationCalculationPageUrlPattern = new RegExp('/lease-amortization-calculation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseAmortizationCalculationSample = {};

  let leaseAmortizationCalculation: any;
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
      body: {"bookingId":"withdrawal","leaseTitle":"matrix","shortTitle":"deliver Beauty Credit","description":"Swiss","inceptionDate":"2024-03-06","commencementDate":"2024-03-06","serialNumber":"b47a683d-6fca-4f30-a1bc-51016ab4f019"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-amortization-calculations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-amortization-calculations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-amortization-calculations/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/lease-liabilities', {
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
    if (leaseAmortizationCalculation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-amortization-calculations/${leaseAmortizationCalculation.id}`,
      }).then(() => {
        leaseAmortizationCalculation = undefined;
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

  it('LeaseAmortizationCalculations menu should load LeaseAmortizationCalculations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-amortization-calculation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseAmortizationCalculation').should('exist');
    cy.url().should('match', leaseAmortizationCalculationPageUrlPattern);
  });

  describe('LeaseAmortizationCalculation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseAmortizationCalculationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseAmortizationCalculation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-amortization-calculation/new$'));
        cy.getEntityCreateUpdateHeading('LeaseAmortizationCalculation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationCalculationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-amortization-calculations',
  
          body: {
            ...leaseAmortizationCalculationSample,
            leaseContract: iFRS16LeaseContract,
          },
        }).then(({ body }) => {
          leaseAmortizationCalculation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-amortization-calculations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseAmortizationCalculation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseAmortizationCalculationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseAmortizationCalculationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseAmortizationCalculation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseAmortizationCalculation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationCalculationPageUrlPattern);
      });

      it('edit button click should load edit LeaseAmortizationCalculation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseAmortizationCalculation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationCalculationPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseAmortizationCalculation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseAmortizationCalculation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationCalculationPageUrlPattern);

        leaseAmortizationCalculation = undefined;
      });
    });
  });

  describe('new LeaseAmortizationCalculation page', () => {
    beforeEach(() => {
      cy.visit(`${leaseAmortizationCalculationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseAmortizationCalculation');
    });

    it.skip('should create an instance of LeaseAmortizationCalculation', () => {
      cy.get(`[data-cy="interestRate"]`).type('1982').should('have.value', '1982');

      cy.get(`[data-cy="periodicity"]`).type('Account Wooden Integrated').should('have.value', 'Account Wooden Integrated');

      cy.get(`[data-cy="leaseAmount"]`).type('41590').should('have.value', '41590');

      cy.get(`[data-cy="numberOfPeriods"]`).type('36133').should('have.value', '36133');

      cy.get(`[data-cy="leaseContract"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseAmortizationCalculation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseAmortizationCalculationPageUrlPattern);
    });
  });
});
