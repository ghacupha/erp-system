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

describe('WIPTransferListReport e2e test', () => {
  const wIPTransferListReportPageUrl = '/wip-transfer-list-report';
  const wIPTransferListReportPageUrlPattern = new RegExp('/wip-transfer-list-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const wIPTransferListReportSample = {
    timeOfRequest: '2025-02-03T01:28:22.527Z',
    requestId: '1ae68dce-00dc-485a-87df-cd9b7577a8b2',
    filename: '9c4bdad1-52f1-4ff3-b1af-477c688b21b3',
  };

  let wIPTransferListReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/wip-transfer-list-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wip-transfer-list-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wip-transfer-list-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (wIPTransferListReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wip-transfer-list-reports/${wIPTransferListReport.id}`,
      }).then(() => {
        wIPTransferListReport = undefined;
      });
    }
  });

  it('WIPTransferListReports menu should load WIPTransferListReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('wip-transfer-list-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WIPTransferListReport').should('exist');
    cy.url().should('match', wIPTransferListReportPageUrlPattern);
  });

  describe('WIPTransferListReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(wIPTransferListReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WIPTransferListReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/wip-transfer-list-report/new$'));
        cy.getEntityCreateUpdateHeading('WIPTransferListReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPTransferListReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wip-transfer-list-reports',
          body: wIPTransferListReportSample,
        }).then(({ body }) => {
          wIPTransferListReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wip-transfer-list-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [wIPTransferListReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(wIPTransferListReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WIPTransferListReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('wIPTransferListReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPTransferListReportPageUrlPattern);
      });

      it('edit button click should load edit WIPTransferListReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WIPTransferListReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPTransferListReportPageUrlPattern);
      });

      it('last delete button click should delete instance of WIPTransferListReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('wIPTransferListReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPTransferListReportPageUrlPattern);

        wIPTransferListReport = undefined;
      });
    });
  });

  describe('new WIPTransferListReport page', () => {
    beforeEach(() => {
      cy.visit(`${wIPTransferListReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WIPTransferListReport');
    });

    it('should create an instance of WIPTransferListReport', () => {
      cy.get(`[data-cy="timeOfRequest"]`).type('2025-02-02T20:26').should('have.value', '2025-02-02T20:26');

      cy.get(`[data-cy="requestId"]`)
        .type('239192fc-01ae-4990-8cd4-31445bcd720c')
        .invoke('val')
        .should('match', new RegExp('239192fc-01ae-4990-8cd4-31445bcd720c'));

      cy.get(`[data-cy="fileChecksum"]`).type('framework Bedfordshire').should('have.value', 'framework Bedfordshire');

      cy.get(`[data-cy="tempered"]`).should('not.be.checked');
      cy.get(`[data-cy="tempered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('2e5f6cee-13fd-4870-ba9f-f7bfb50c6fe5')
        .invoke('val')
        .should('match', new RegExp('2e5f6cee-13fd-4870-ba9f-f7bfb50c6fe5'));

      cy.get(`[data-cy="reportParameters"]`).type('azure Metal').should('have.value', 'azure Metal');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        wIPTransferListReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', wIPTransferListReportPageUrlPattern);
    });
  });
});
