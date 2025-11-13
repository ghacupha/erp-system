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

describe('DepreciationReport e2e test', () => {
  const depreciationReportPageUrl = '/depreciation-report';
  const depreciationReportPageUrlPattern = new RegExp('/depreciation-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const depreciationReportSample = { reportName: 'capacitor well-modulated parse', timeOfReportRequest: '2024-01-14T23:58:23.239Z' };

  let depreciationReport: any;
  //let depreciationPeriod: any;

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
      url: '/api/depreciation-periods',
      body: {"startDate":"2023-07-03","endDate":"2023-07-04","depreciationPeriodStatus":"CLOSED","periodCode":"PNG up","processLocked":true},
    }).then(({ body }) => {
      depreciationPeriod = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/depreciation-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depreciation-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depreciation-reports/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/depreciation-periods', {
      statusCode: 200,
      body: [depreciationPeriod],
    });

    cy.intercept('GET', '/api/service-outlets', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-categories', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (depreciationReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-reports/${depreciationReport.id}`,
      }).then(() => {
        depreciationReport = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (depreciationPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-periods/${depreciationPeriod.id}`,
      }).then(() => {
        depreciationPeriod = undefined;
      });
    }
  });
   */

  it('DepreciationReports menu should load DepreciationReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('depreciation-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepreciationReport').should('exist');
    cy.url().should('match', depreciationReportPageUrlPattern);
  });

  describe('DepreciationReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depreciationReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepreciationReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/depreciation-report/new$'));
        cy.getEntityCreateUpdateHeading('DepreciationReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depreciation-reports',
  
          body: {
            ...depreciationReportSample,
            depreciationPeriod: depreciationPeriod,
          },
        }).then(({ body }) => {
          depreciationReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depreciation-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [depreciationReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depreciationReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(depreciationReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DepreciationReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('depreciationReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationReportPageUrlPattern);
      });

      it('edit button click should load edit DepreciationReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepreciationReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationReportPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DepreciationReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('depreciationReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationReportPageUrlPattern);

        depreciationReport = undefined;
      });
    });
  });

  describe('new DepreciationReport page', () => {
    beforeEach(() => {
      cy.visit(`${depreciationReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepreciationReport');
    });

    it.skip('should create an instance of DepreciationReport', () => {
      cy.get(`[data-cy="reportName"]`).type('Object-based directional').should('have.value', 'Object-based directional');

      cy.get(`[data-cy="timeOfReportRequest"]`).type('2024-01-15T01:14').should('have.value', '2024-01-15T01:14');

      cy.get(`[data-cy="fileChecksum"]`).type('Japan deposit').should('have.value', 'Japan deposit');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('e4dba2bc-024e-49b7-8c08-6905e17b5e15')
        .invoke('val')
        .should('match', new RegExp('e4dba2bc-024e-49b7-8c08-6905e17b5e15'));

      cy.get(`[data-cy="reportParameters"]`).type('Borders calculate Organic').should('have.value', 'Borders calculate Organic');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="depreciationPeriod"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        depreciationReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depreciationReportPageUrlPattern);
    });
  });
});
