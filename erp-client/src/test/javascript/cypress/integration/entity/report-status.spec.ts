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

describe('ReportStatus e2e test', () => {
  const reportStatusPageUrl = '/report-status';
  const reportStatusPageUrlPattern = new RegExp('/report-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reportStatusSample = { reportName: 'Future', reportId: '075d3ef6-51fa-4b87-ac40-9ccc89ac6f3b' };

  let reportStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-statuses/${reportStatus.id}`,
      }).then(() => {
        reportStatus = undefined;
      });
    }
  });

  it('ReportStatuses menu should load ReportStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportStatus').should('exist');
    cy.url().should('match', reportStatusPageUrlPattern);
  });

  describe('ReportStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/report-status/new$'));
        cy.getEntityCreateUpdateHeading('ReportStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-statuses',
          body: reportStatusSample,
        }).then(({ body }) => {
          reportStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportStatusPageUrlPattern);
      });

      it('edit button click should load edit ReportStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportStatusPageUrlPattern);

        reportStatus = undefined;
      });
    });
  });

  describe('new ReportStatus page', () => {
    beforeEach(() => {
      cy.visit(`${reportStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReportStatus');
    });

    it('should create an instance of ReportStatus', () => {
      cy.get(`[data-cy="reportName"]`).type('redundant').should('have.value', 'redundant');

      cy.get(`[data-cy="reportId"]`)
        .type('26e9d679-f2e7-4f0a-b71b-ae16aebb1cc0')
        .invoke('val')
        .should('match', new RegExp('26e9d679-f2e7-4f0a-b71b-ae16aebb1cc0'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reportStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reportStatusPageUrlPattern);
    });
  });
});
