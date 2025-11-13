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

describe('RouMonthlyDepreciationReport e2e test', () => {
  const rouMonthlyDepreciationReportPageUrl = '/rou-monthly-depreciation-report';
  const rouMonthlyDepreciationReportPageUrlPattern = new RegExp('/rou-monthly-depreciation-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouMonthlyDepreciationReportSample = {
    requestId: 'e719c29f-ed93-4b70-baea-6ab9ca8cf827',
    timeOfRequest: '2024-03-13T04:49:29.879Z',
  };

  let rouMonthlyDepreciationReport: any;
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
      body: { fiscalYearCode: 'Regional Rubber', startDate: '2023-08-15', endDate: '2023-08-16', fiscalYearStatus: 'CLOSED' },
    }).then(({ body }) => {
      fiscalYear = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-monthly-depreciation-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-monthly-depreciation-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-monthly-depreciation-reports/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/fiscal-years', {
      statusCode: 200,
      body: [fiscalYear],
    });
  });

  afterEach(() => {
    if (rouMonthlyDepreciationReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-monthly-depreciation-reports/${rouMonthlyDepreciationReport.id}`,
      }).then(() => {
        rouMonthlyDepreciationReport = undefined;
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

  it('RouMonthlyDepreciationReports menu should load RouMonthlyDepreciationReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-monthly-depreciation-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouMonthlyDepreciationReport').should('exist');
    cy.url().should('match', rouMonthlyDepreciationReportPageUrlPattern);
  });

  describe('RouMonthlyDepreciationReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouMonthlyDepreciationReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouMonthlyDepreciationReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-monthly-depreciation-report/new$'));
        cy.getEntityCreateUpdateHeading('RouMonthlyDepreciationReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-monthly-depreciation-reports',

          body: {
            ...rouMonthlyDepreciationReportSample,
            reportingYear: fiscalYear,
          },
        }).then(({ body }) => {
          rouMonthlyDepreciationReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-monthly-depreciation-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouMonthlyDepreciationReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouMonthlyDepreciationReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RouMonthlyDepreciationReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouMonthlyDepreciationReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportPageUrlPattern);
      });

      it('edit button click should load edit RouMonthlyDepreciationReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouMonthlyDepreciationReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportPageUrlPattern);
      });

      it('last delete button click should delete instance of RouMonthlyDepreciationReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouMonthlyDepreciationReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportPageUrlPattern);

        rouMonthlyDepreciationReport = undefined;
      });
    });
  });

  describe('new RouMonthlyDepreciationReport page', () => {
    beforeEach(() => {
      cy.visit(`${rouMonthlyDepreciationReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouMonthlyDepreciationReport');
    });

    it('should create an instance of RouMonthlyDepreciationReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('54842664-c62a-4e50-93e3-22106a9b6489')
        .invoke('val')
        .should('match', new RegExp('54842664-c62a-4e50-93e3-22106a9b6489'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-12T10:36').should('have.value', '2024-03-12T10:36');

      cy.get(`[data-cy="reportIsCompiled"]`).should('not.be.checked');
      cy.get(`[data-cy="reportIsCompiled"]`).click().should('be.checked');

      cy.get(`[data-cy="fileChecksum"]`).type('seamless navigate Refined').should('have.value', 'seamless navigate Refined');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('2405b72d-8450-46a8-b2c3-2f26b8dec283')
        .invoke('val')
        .should('match', new RegExp('2405b72d-8450-46a8-b2c3-2f26b8dec283'));

      cy.get(`[data-cy="reportParameters"]`).type('Manat').should('have.value', 'Manat');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reportingYear"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouMonthlyDepreciationReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouMonthlyDepreciationReportPageUrlPattern);
    });
  });
});
