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

describe('WIPListReport e2e test', () => {
  const wIPListReportPageUrl = '/wip-list-report';
  const wIPListReportPageUrlPattern = new RegExp('/wip-list-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const wIPListReportSample = { timeOfRequest: '2025-02-03T08:47:33.820Z', requestId: 'efbba0e5-4456-4955-bcca-a1b8768f16b9' };

  let wIPListReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/wip-list-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wip-list-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wip-list-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (wIPListReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wip-list-reports/${wIPListReport.id}`,
      }).then(() => {
        wIPListReport = undefined;
      });
    }
  });

  it('WIPListReports menu should load WIPListReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('wip-list-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WIPListReport').should('exist');
    cy.url().should('match', wIPListReportPageUrlPattern);
  });

  describe('WIPListReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(wIPListReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WIPListReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/wip-list-report/new$'));
        cy.getEntityCreateUpdateHeading('WIPListReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPListReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wip-list-reports',
          body: wIPListReportSample,
        }).then(({ body }) => {
          wIPListReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wip-list-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [wIPListReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(wIPListReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WIPListReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('wIPListReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPListReportPageUrlPattern);
      });

      it('edit button click should load edit WIPListReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WIPListReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPListReportPageUrlPattern);
      });

      it('last delete button click should delete instance of WIPListReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('wIPListReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', wIPListReportPageUrlPattern);

        wIPListReport = undefined;
      });
    });
  });

  describe('new WIPListReport page', () => {
    beforeEach(() => {
      cy.visit(`${wIPListReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WIPListReport');
    });

    it('should create an instance of WIPListReport', () => {
      cy.get(`[data-cy="timeOfRequest"]`).type('2025-02-02T16:44').should('have.value', '2025-02-02T16:44');

      cy.get(`[data-cy="requestId"]`)
        .type('e08e9c07-1b1c-4785-a5b2-26b404669e9c')
        .invoke('val')
        .should('match', new RegExp('e08e9c07-1b1c-4785-a5b2-26b404669e9c'));

      cy.get(`[data-cy="fileChecksum"]`).type('conglomeration').should('have.value', 'conglomeration');

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="filename"]`)
        .type('90c4607a-90b7-4ef0-a7fd-b52619d65444')
        .invoke('val')
        .should('match', new RegExp('90c4607a-90b7-4ef0-a7fd-b52619d65444'));

      cy.get(`[data-cy="reportParameters"]`).type('functionalities').should('have.value', 'functionalities');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        wIPListReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', wIPListReportPageUrlPattern);
    });
  });
});
