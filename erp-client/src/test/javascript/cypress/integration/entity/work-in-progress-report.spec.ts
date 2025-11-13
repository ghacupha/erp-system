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
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('WorkInProgressReport e2e test', () => {
  const workInProgressReportPageUrl = '/work-in-progress-report';
  const workInProgressReportPageUrlPattern = new RegExp('/work-in-progress-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const workInProgressReportSample = {};

  let workInProgressReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/work-in-progress-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/work-in-progress-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/work-in-progress-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (workInProgressReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/work-in-progress-reports/${workInProgressReport.id}`,
      }).then(() => {
        workInProgressReport = undefined;
      });
    }
  });

  it('WorkInProgressReports menu should load WorkInProgressReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('work-in-progress-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkInProgressReport').should('exist');
    cy.url().should('match', workInProgressReportPageUrlPattern);
  });

  describe('WorkInProgressReport page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(workInProgressReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details WorkInProgressReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('workInProgressReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressReportPageUrlPattern);
      });
    });
  });
});
