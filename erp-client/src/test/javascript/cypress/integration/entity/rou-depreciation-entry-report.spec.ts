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

describe('RouDepreciationEntryReport e2e test', () => {
  const rouDepreciationEntryReportPageUrl = '/rou-depreciation-entry-report';
  const rouDepreciationEntryReportPageUrlPattern = new RegExp('/rou-depreciation-entry-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouDepreciationEntryReportSample = { requestId: '8a6d3509-6272-42a4-bbe9-da528d479270' };

  let rouDepreciationEntryReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-depreciation-entry-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-depreciation-entry-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-depreciation-entry-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rouDepreciationEntryReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-depreciation-entry-reports/${rouDepreciationEntryReport.id}`,
      }).then(() => {
        rouDepreciationEntryReport = undefined;
      });
    }
  });

  it('RouDepreciationEntryReports menu should load RouDepreciationEntryReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-depreciation-entry-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouDepreciationEntryReport').should('exist');
    cy.url().should('match', rouDepreciationEntryReportPageUrlPattern);
  });

  describe('RouDepreciationEntryReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouDepreciationEntryReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouDepreciationEntryReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-depreciation-entry-report/new$'));
        cy.getEntityCreateUpdateHeading('RouDepreciationEntryReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-depreciation-entry-reports',
          body: rouDepreciationEntryReportSample,
        }).then(({ body }) => {
          rouDepreciationEntryReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-depreciation-entry-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouDepreciationEntryReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouDepreciationEntryReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RouDepreciationEntryReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouDepreciationEntryReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryReportPageUrlPattern);
      });

      it('edit button click should load edit RouDepreciationEntryReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouDepreciationEntryReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryReportPageUrlPattern);
      });

      it('last delete button click should delete instance of RouDepreciationEntryReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouDepreciationEntryReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouDepreciationEntryReportPageUrlPattern);

        rouDepreciationEntryReport = undefined;
      });
    });
  });

  describe('new RouDepreciationEntryReport page', () => {
    beforeEach(() => {
      cy.visit(`${rouDepreciationEntryReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouDepreciationEntryReport');
    });

    it('should create an instance of RouDepreciationEntryReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('e9ea203a-356f-4f2c-9149-03020390bc5d')
        .invoke('val')
        .should('match', new RegExp('e9ea203a-356f-4f2c-9149-03020390bc5d'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-13T07:00').should('have.value', '2024-03-13T07:00');

      cy.get(`[data-cy="reportIsCompiled"]`).should('not.be.checked');
      cy.get(`[data-cy="reportIsCompiled"]`).click().should('be.checked');

      cy.get(`[data-cy="periodStartDate"]`).type('2024-03-12').should('have.value', '2024-03-12');

      cy.get(`[data-cy="periodEndDate"]`).type('2024-03-12').should('have.value', '2024-03-12');

      cy.get(`[data-cy="fileChecksum"]`).type('circuit wireless').should('have.value', 'circuit wireless');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('1cdd3258-f3c2-4e66-bbc1-452ff2f2f9c2')
        .invoke('val')
        .should('match', new RegExp('1cdd3258-f3c2-4e66-bbc1-452ff2f2f9c2'));

      cy.get(`[data-cy="reportParameters"]`).type('Keyboard withdrawal withdrawal').should('have.value', 'Keyboard withdrawal withdrawal');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouDepreciationEntryReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouDepreciationEntryReportPageUrlPattern);
    });
  });
});
