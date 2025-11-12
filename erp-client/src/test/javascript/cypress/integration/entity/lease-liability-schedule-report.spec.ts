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

describe('LeaseLiabilityScheduleReport e2e test', () => {
  const leaseLiabilityScheduleReportPageUrl = '/lease-liability-schedule-report';
  const leaseLiabilityScheduleReportPageUrlPattern = new RegExp('/lease-liability-schedule-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseLiabilityScheduleReportSample = {
    requestId: '112d7113-ea18-4527-b7a6-72ecf7237be5',
    timeOfRequest: '2024-08-28T01:35:33.251Z',
  };

  let leaseLiabilityScheduleReport: any;
  //let leaseAmortizationSchedule: any;

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
      url: '/api/lease-amortization-schedules',
      body: {"identifier":"4aebbb14-df96-4e9c-90a6-7daf31881509"},
    }).then(({ body }) => {
      leaseAmortizationSchedule = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-liability-schedule-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-liability-schedule-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-liability-schedule-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lease-amortization-schedules', {
      statusCode: 200,
      body: [leaseAmortizationSchedule],
    });

  });
   */

  afterEach(() => {
    if (leaseLiabilityScheduleReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-liability-schedule-reports/${leaseLiabilityScheduleReport.id}`,
      }).then(() => {
        leaseLiabilityScheduleReport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('LeaseLiabilityScheduleReports menu should load LeaseLiabilityScheduleReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-liability-schedule-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseLiabilityScheduleReport').should('exist');
    cy.url().should('match', leaseLiabilityScheduleReportPageUrlPattern);
  });

  describe('LeaseLiabilityScheduleReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseLiabilityScheduleReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseLiabilityScheduleReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-liability-schedule-report/new$'));
        cy.getEntityCreateUpdateHeading('LeaseLiabilityScheduleReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-liability-schedule-reports',
  
          body: {
            ...leaseLiabilityScheduleReportSample,
            leaseAmortizationSchedule: leaseAmortizationSchedule,
          },
        }).then(({ body }) => {
          leaseLiabilityScheduleReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-liability-schedule-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseLiabilityScheduleReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseLiabilityScheduleReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseLiabilityScheduleReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseLiabilityScheduleReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseLiabilityScheduleReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleReportPageUrlPattern);
      });

      it('edit button click should load edit LeaseLiabilityScheduleReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseLiabilityScheduleReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseLiabilityScheduleReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseLiabilityScheduleReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseLiabilityScheduleReportPageUrlPattern);

        leaseLiabilityScheduleReport = undefined;
      });
    });
  });

  describe('new LeaseLiabilityScheduleReport page', () => {
    beforeEach(() => {
      cy.visit(`${leaseLiabilityScheduleReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseLiabilityScheduleReport');
    });

    it.skip('should create an instance of LeaseLiabilityScheduleReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('d3075001-4b7e-4380-b24b-9086671730a8')
        .invoke('val')
        .should('match', new RegExp('d3075001-4b7e-4380-b24b-9086671730a8'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-08-27T12:05').should('have.value', '2024-08-27T12:05');

      cy.get(`[data-cy="fileChecksum"]`).type('Metal algorithm').should('have.value', 'Metal algorithm');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('35768c53-5efe-4e2c-99d7-7afc7ffef911')
        .invoke('val')
        .should('match', new RegExp('35768c53-5efe-4e2c-99d7-7afc7ffef911'));

      cy.get(`[data-cy="reportParameters"]`).type('installation').should('have.value', 'installation');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="leaseAmortizationSchedule"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseLiabilityScheduleReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseLiabilityScheduleReportPageUrlPattern);
    });
  });
});
