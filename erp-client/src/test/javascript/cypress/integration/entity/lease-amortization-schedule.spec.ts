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

describe('LeaseAmortizationSchedule e2e test', () => {
  const leaseAmortizationSchedulePageUrl = '/lease-amortization-schedule';
  const leaseAmortizationSchedulePageUrlPattern = new RegExp('/lease-amortization-schedule(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseAmortizationScheduleSample = { identifier: 'cca5d61c-d2df-4c20-a9f9-b33de9533146' };

  let leaseAmortizationSchedule: any;
  //let leaseLiability: any;
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
      url: '/api/lease-liabilities',
      body: {"leaseId":"Australian","liabilityAmount":60274,"startDate":"2024-06-17","endDate":"2024-06-18","interestRate":34192},
    }).then(({ body }) => {
      leaseLiability = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/ifrs-16-lease-contracts',
      body: {"bookingId":"monitoring","leaseTitle":"digital Fish bypass","shortTitle":"Cheese","description":"Pre-emptive Investor Shoes","inceptionDate":"2024-03-07","commencementDate":"2024-03-06","serialNumber":"ee29e79e-858d-4737-8b5a-eacdaf644c9d"},
    }).then(({ body }) => {
      iFRS16LeaseContract = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-amortization-schedules+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-amortization-schedules').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-amortization-schedules/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/lease-liabilities', {
      statusCode: 200,
      body: [leaseLiability],
    });

    cy.intercept('GET', '/api/lease-liability-schedule-items', {
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
    if (leaseAmortizationSchedule) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-amortization-schedules/${leaseAmortizationSchedule.id}`,
      }).then(() => {
        leaseAmortizationSchedule = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (leaseLiability) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liabilities/${leaseLiability.id}`,
      }).then(() => {
        leaseLiability = undefined;
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
  });
   */

  it('LeaseAmortizationSchedules menu should load LeaseAmortizationSchedules page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-amortization-schedule');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseAmortizationSchedule').should('exist');
    cy.url().should('match', leaseAmortizationSchedulePageUrlPattern);
  });

  describe('LeaseAmortizationSchedule page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseAmortizationSchedulePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseAmortizationSchedule page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-amortization-schedule/new$'));
        cy.getEntityCreateUpdateHeading('LeaseAmortizationSchedule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationSchedulePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-amortization-schedules',
  
          body: {
            ...leaseAmortizationScheduleSample,
            leaseLiability: leaseLiability,
            leaseContract: iFRS16LeaseContract,
          },
        }).then(({ body }) => {
          leaseAmortizationSchedule = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-amortization-schedules+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseAmortizationSchedule],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseAmortizationSchedulePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseAmortizationSchedulePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseAmortizationSchedule page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseAmortizationSchedule');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationSchedulePageUrlPattern);
      });

      it('edit button click should load edit LeaseAmortizationSchedule page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseAmortizationSchedule');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationSchedulePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseAmortizationSchedule', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseAmortizationSchedule').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseAmortizationSchedulePageUrlPattern);

        leaseAmortizationSchedule = undefined;
      });
    });
  });

  describe('new LeaseAmortizationSchedule page', () => {
    beforeEach(() => {
      cy.visit(`${leaseAmortizationSchedulePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseAmortizationSchedule');
    });

    it.skip('should create an instance of LeaseAmortizationSchedule', () => {
      cy.get(`[data-cy="identifier"]`)
        .type('e510f66b-f86c-405b-ae97-1daaf8388522')
        .invoke('val')
        .should('match', new RegExp('e510f66b-f86c-405b-ae97-1daaf8388522'));

      cy.get(`[data-cy="leaseLiability"]`).select(1);
      cy.get(`[data-cy="leaseContract"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseAmortizationSchedule = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseAmortizationSchedulePageUrlPattern);
    });
  });
});
