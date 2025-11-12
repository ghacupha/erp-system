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

describe('RouAssetListReport e2e test', () => {
  const rouAssetListReportPageUrl = '/rou-asset-list-report';
  const rouAssetListReportPageUrlPattern = new RegExp('/rou-asset-list-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouAssetListReportSample = { requestId: 'a048f1e4-7636-4c80-9f50-0eb1fbf8ac9e' };

  let rouAssetListReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-asset-list-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-asset-list-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-asset-list-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rouAssetListReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-asset-list-reports/${rouAssetListReport.id}`,
      }).then(() => {
        rouAssetListReport = undefined;
      });
    }
  });

  it('RouAssetListReports menu should load RouAssetListReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-asset-list-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouAssetListReport').should('exist');
    cy.url().should('match', rouAssetListReportPageUrlPattern);
  });

  describe('RouAssetListReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouAssetListReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouAssetListReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-asset-list-report/new$'));
        cy.getEntityCreateUpdateHeading('RouAssetListReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetListReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-asset-list-reports',
          body: rouAssetListReportSample,
        }).then(({ body }) => {
          rouAssetListReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-asset-list-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouAssetListReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouAssetListReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RouAssetListReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouAssetListReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetListReportPageUrlPattern);
      });

      it('edit button click should load edit RouAssetListReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouAssetListReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetListReportPageUrlPattern);
      });

      it('last delete button click should delete instance of RouAssetListReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouAssetListReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouAssetListReportPageUrlPattern);

        rouAssetListReport = undefined;
      });
    });
  });

  describe('new RouAssetListReport page', () => {
    beforeEach(() => {
      cy.visit(`${rouAssetListReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouAssetListReport');
    });

    it('should create an instance of RouAssetListReport', () => {
      cy.get(`[data-cy="requestId"]`)
        .type('1a90b40e-dead-4679-a1df-3ef8c53d31ba')
        .invoke('val')
        .should('match', new RegExp('1a90b40e-dead-4679-a1df-3ef8c53d31ba'));

      cy.get(`[data-cy="timeOfRequest"]`).type('2024-03-13T08:34').should('have.value', '2024-03-13T08:34');

      cy.get(`[data-cy="fileChecksum"]`).type('Sausages interfaces Sleek').should('have.value', 'Sausages interfaces Sleek');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('9f437f41-5e58-4352-93ba-545aec2af7a3')
        .invoke('val')
        .should('match', new RegExp('9f437f41-5e58-4352-93ba-545aec2af7a3'));

      cy.get(`[data-cy="reportParameters"]`).type('Books').should('have.value', 'Books');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouAssetListReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouAssetListReportPageUrlPattern);
    });
  });
});
