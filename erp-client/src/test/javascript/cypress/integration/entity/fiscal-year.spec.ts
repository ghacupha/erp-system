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

describe('FiscalYear e2e test', () => {
  const fiscalYearPageUrl = '/fiscal-year';
  const fiscalYearPageUrlPattern = new RegExp('/fiscal-year(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fiscalYearSample = { fiscalYearCode: 'strategic', startDate: '2023-08-16', endDate: '2023-08-15' };

  let fiscalYear: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fiscal-years+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fiscal-years').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fiscal-years/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fiscalYear) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-years/${fiscalYear.id}`,
      }).then(() => {
        fiscalYear = undefined;
      });
    }
  });

  it('FiscalYears menu should load FiscalYears page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fiscal-year');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FiscalYear').should('exist');
    cy.url().should('match', fiscalYearPageUrlPattern);
  });

  describe('FiscalYear page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fiscalYearPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FiscalYear page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fiscal-year/new$'));
        cy.getEntityCreateUpdateHeading('FiscalYear');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalYearPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fiscal-years',
          body: fiscalYearSample,
        }).then(({ body }) => {
          fiscalYear = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fiscal-years+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fiscalYear],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fiscalYearPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FiscalYear page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fiscalYear');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalYearPageUrlPattern);
      });

      it('edit button click should load edit FiscalYear page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FiscalYear');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalYearPageUrlPattern);
      });

      it('last delete button click should delete instance of FiscalYear', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fiscalYear').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalYearPageUrlPattern);

        fiscalYear = undefined;
      });
    });
  });

  describe('new FiscalYear page', () => {
    beforeEach(() => {
      cy.visit(`${fiscalYearPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FiscalYear');
    });

    it('should create an instance of FiscalYear', () => {
      cy.get(`[data-cy="fiscalYearCode"]`).type('Kansas').should('have.value', 'Kansas');

      cy.get(`[data-cy="startDate"]`).type('2023-08-16').should('have.value', '2023-08-16');

      cy.get(`[data-cy="endDate"]`).type('2023-08-15').should('have.value', '2023-08-15');

      cy.get(`[data-cy="fiscalYearStatus"]`).select('OPEN');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fiscalYear = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fiscalYearPageUrlPattern);
    });
  });
});
