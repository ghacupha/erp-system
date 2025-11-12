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

describe('RouMonthlyDepreciationReportItem e2e test', () => {
  const rouMonthlyDepreciationReportItemPageUrl = '/rou-monthly-depreciation-report-item';
  const rouMonthlyDepreciationReportItemPageUrlPattern = new RegExp('/rou-monthly-depreciation-report-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const rouMonthlyDepreciationReportItemSample = {};

  let rouMonthlyDepreciationReportItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rou-monthly-depreciation-report-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rou-monthly-depreciation-report-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rou-monthly-depreciation-report-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rouMonthlyDepreciationReportItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rou-monthly-depreciation-report-items/${rouMonthlyDepreciationReportItem.id}`,
      }).then(() => {
        rouMonthlyDepreciationReportItem = undefined;
      });
    }
  });

  it('RouMonthlyDepreciationReportItems menu should load RouMonthlyDepreciationReportItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rou-monthly-depreciation-report-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RouMonthlyDepreciationReportItem').should('exist');
    cy.url().should('match', rouMonthlyDepreciationReportItemPageUrlPattern);
  });

  describe('RouMonthlyDepreciationReportItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rouMonthlyDepreciationReportItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RouMonthlyDepreciationReportItem page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/rou-monthly-depreciation-report-item/new$'));
        cy.getEntityCreateUpdateHeading('RouMonthlyDepreciationReportItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rou-monthly-depreciation-report-items',
          body: rouMonthlyDepreciationReportItemSample,
        }).then(({ body }) => {
          rouMonthlyDepreciationReportItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rou-monthly-depreciation-report-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rouMonthlyDepreciationReportItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rouMonthlyDepreciationReportItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RouMonthlyDepreciationReportItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rouMonthlyDepreciationReportItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportItemPageUrlPattern);
      });

      it('edit button click should load edit RouMonthlyDepreciationReportItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RouMonthlyDepreciationReportItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportItemPageUrlPattern);
      });

      it('last delete button click should delete instance of RouMonthlyDepreciationReportItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rouMonthlyDepreciationReportItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rouMonthlyDepreciationReportItemPageUrlPattern);

        rouMonthlyDepreciationReportItem = undefined;
      });
    });
  });

  describe('new RouMonthlyDepreciationReportItem page', () => {
    beforeEach(() => {
      cy.visit(`${rouMonthlyDepreciationReportItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RouMonthlyDepreciationReportItem');
    });

    it('should create an instance of RouMonthlyDepreciationReportItem', () => {
      cy.get(`[data-cy="fiscalMonthStartDate"]`).type('2024-03-13').should('have.value', '2024-03-13');

      cy.get(`[data-cy="fiscalMonthEndDate"]`).type('2024-03-13').should('have.value', '2024-03-13');

      cy.get(`[data-cy="totalDepreciationAmount"]`).type('53291').should('have.value', '53291');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        rouMonthlyDepreciationReportItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rouMonthlyDepreciationReportItemPageUrlPattern);
    });
  });
});
