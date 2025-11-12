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
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('PrepaymentOutstandingOverviewReport e2e test', () => {
  const prepaymentOutstandingOverviewReportPageUrl = '/prepayment-outstanding-overview-report';
  const prepaymentOutstandingOverviewReportPageUrlPattern = new RegExp('/prepayment-outstanding-overview-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const prepaymentOutstandingOverviewReportSample = {};

  let prepaymentOutstandingOverviewReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prepayment-outstanding-overview-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prepayment-outstanding-overview-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prepayment-outstanding-overview-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prepaymentOutstandingOverviewReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-outstanding-overview-reports/${prepaymentOutstandingOverviewReport.id}`,
      }).then(() => {
        prepaymentOutstandingOverviewReport = undefined;
      });
    }
  });

  it('PrepaymentOutstandingOverviewReports menu should load PrepaymentOutstandingOverviewReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prepayment-outstanding-overview-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrepaymentOutstandingOverviewReport').should('exist');
    cy.url().should('match', prepaymentOutstandingOverviewReportPageUrlPattern);
  });

  describe('PrepaymentOutstandingOverviewReport page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(prepaymentOutstandingOverviewReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PrepaymentOutstandingOverviewReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prepaymentOutstandingOverviewReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentOutstandingOverviewReportPageUrlPattern);
      });
    });
  });
});
