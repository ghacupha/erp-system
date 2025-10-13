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

describe('ChartOfAccountsCode e2e test', () => {
  const chartOfAccountsCodePageUrl = '/chart-of-accounts-code';
  const chartOfAccountsCodePageUrlPattern = new RegExp('/chart-of-accounts-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const chartOfAccountsCodeSample = { chartOfAccountsCode: 'system-worthy strategic', chartOfAccountsClass: 'generate 3rd optical' };

  let chartOfAccountsCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chart-of-accounts-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chart-of-accounts-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chart-of-accounts-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (chartOfAccountsCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chart-of-accounts-codes/${chartOfAccountsCode.id}`,
      }).then(() => {
        chartOfAccountsCode = undefined;
      });
    }
  });

  it('ChartOfAccountsCodes menu should load ChartOfAccountsCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chart-of-accounts-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChartOfAccountsCode').should('exist');
    cy.url().should('match', chartOfAccountsCodePageUrlPattern);
  });

  describe('ChartOfAccountsCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chartOfAccountsCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChartOfAccountsCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/chart-of-accounts-code/new$'));
        cy.getEntityCreateUpdateHeading('ChartOfAccountsCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', chartOfAccountsCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chart-of-accounts-codes',
          body: chartOfAccountsCodeSample,
        }).then(({ body }) => {
          chartOfAccountsCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chart-of-accounts-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [chartOfAccountsCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(chartOfAccountsCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChartOfAccountsCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chartOfAccountsCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', chartOfAccountsCodePageUrlPattern);
      });

      it('edit button click should load edit ChartOfAccountsCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChartOfAccountsCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', chartOfAccountsCodePageUrlPattern);
      });

      it('last delete button click should delete instance of ChartOfAccountsCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('chartOfAccountsCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', chartOfAccountsCodePageUrlPattern);

        chartOfAccountsCode = undefined;
      });
    });
  });

  describe('new ChartOfAccountsCode page', () => {
    beforeEach(() => {
      cy.visit(`${chartOfAccountsCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ChartOfAccountsCode');
    });

    it('should create an instance of ChartOfAccountsCode', () => {
      cy.get(`[data-cy="chartOfAccountsCode"]`).type('convergence').should('have.value', 'convergence');

      cy.get(`[data-cy="chartOfAccountsClass"]`).type('backing').should('have.value', 'backing');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        chartOfAccountsCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', chartOfAccountsCodePageUrlPattern);
    });
  });
});
