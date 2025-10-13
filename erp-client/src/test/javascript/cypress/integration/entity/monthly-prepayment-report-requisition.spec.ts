///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

describe('MonthlyPrepaymentReportRequisition e2e test', () => {
  const monthlyPrepaymentReportRequisitionPageUrl = '/monthly-prepayment-report-requisition';
  const monthlyPrepaymentReportRequisitionPageUrlPattern = new RegExp('/monthly-prepayment-report-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const monthlyPrepaymentReportRequisitionSample = { timeOfRequisition: '2024-03-05T03:21:46.024Z' };

  let monthlyPrepaymentReportRequisition: any;
  let fiscalYear: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/fiscal-years',
      body: { fiscalYearCode: 'withdrawal', startDate: '2023-08-15', endDate: '2023-08-15', fiscalYearStatus: 'IN_PROGRESS' },
    }).then(({ body }) => {
      fiscalYear = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/monthly-prepayment-report-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/monthly-prepayment-report-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/monthly-prepayment-report-requisitions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/fiscal-years', {
      statusCode: 200,
      body: [fiscalYear],
    });
  });

  afterEach(() => {
    if (monthlyPrepaymentReportRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/monthly-prepayment-report-requisitions/${monthlyPrepaymentReportRequisition.id}`,
      }).then(() => {
        monthlyPrepaymentReportRequisition = undefined;
      });
    }
  });

  afterEach(() => {
    if (fiscalYear) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-years/${fiscalYear.id}`,
      }).then(() => {
        fiscalYear = undefined;
      });
    }
  });

  it('MonthlyPrepaymentReportRequisitions menu should load MonthlyPrepaymentReportRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('monthly-prepayment-report-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MonthlyPrepaymentReportRequisition').should('exist');
    cy.url().should('match', monthlyPrepaymentReportRequisitionPageUrlPattern);
  });

  describe('MonthlyPrepaymentReportRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(monthlyPrepaymentReportRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MonthlyPrepaymentReportRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/monthly-prepayment-report-requisition/new$'));
        cy.getEntityCreateUpdateHeading('MonthlyPrepaymentReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyPrepaymentReportRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/monthly-prepayment-report-requisitions',

          body: {
            ...monthlyPrepaymentReportRequisitionSample,
            fiscalYear: fiscalYear,
          },
        }).then(({ body }) => {
          monthlyPrepaymentReportRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/monthly-prepayment-report-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [monthlyPrepaymentReportRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(monthlyPrepaymentReportRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MonthlyPrepaymentReportRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('monthlyPrepaymentReportRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyPrepaymentReportRequisitionPageUrlPattern);
      });

      it('edit button click should load edit MonthlyPrepaymentReportRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MonthlyPrepaymentReportRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyPrepaymentReportRequisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of MonthlyPrepaymentReportRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('monthlyPrepaymentReportRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyPrepaymentReportRequisitionPageUrlPattern);

        monthlyPrepaymentReportRequisition = undefined;
      });
    });
  });

  describe('new MonthlyPrepaymentReportRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${monthlyPrepaymentReportRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('MonthlyPrepaymentReportRequisition');
    });

    it('should create an instance of MonthlyPrepaymentReportRequisition', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('1a9b4d4f-cb82-4bd3-99f5-b9c2667566f7')
        .invoke('val')
        .should('match', new RegExp('1a9b4d4f-cb82-4bd3-99f5-b9c2667566f7'));

      cy.get(`[data-cy="timeOfRequisition"]`).type('2024-03-05T03:00').should('have.value', '2024-03-05T03:00');

      cy.get(`[data-cy="fileChecksum"]`).type('UIC-Franc syndicate').should('have.value', 'UIC-Franc syndicate');

      cy.get(`[data-cy="filename"]`)
        .type('1a91880d-cb73-4fa1-88b1-9cece1510683')
        .invoke('val')
        .should('match', new RegExp('1a91880d-cb73-4fa1-88b1-9cece1510683'));

      cy.get(`[data-cy="reportParameters"]`).type('Handcrafted Tasty').should('have.value', 'Handcrafted Tasty');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="fiscalYear"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        monthlyPrepaymentReportRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', monthlyPrepaymentReportRequisitionPageUrlPattern);
    });
  });
});
