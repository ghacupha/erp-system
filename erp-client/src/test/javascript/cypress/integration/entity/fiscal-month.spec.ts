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

describe('FiscalMonth e2e test', () => {
  const fiscalMonthPageUrl = '/fiscal-month';
  const fiscalMonthPageUrlPattern = new RegExp('/fiscal-month(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fiscalMonthSample = { monthNumber: 71770, startDate: '2023-08-16', endDate: '2023-08-15', fiscalMonthCode: 'Islands Refined' };

  let fiscalMonth: any;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/fiscal-years',
      body: { fiscalYearCode: 'systemic', startDate: '2023-08-16', endDate: '2023-08-15', fiscalYearStatus: 'IN_PROGRESS' },
    }).then(({ body }) => {
      fiscalYear = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fiscal-months+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fiscal-months').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fiscal-months/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/fiscal-years', {
      statusCode: 200,
      body: [fiscalYear],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/fiscal-quarters', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (fiscalMonth) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-months/${fiscalMonth.id}`,
      }).then(() => {
        fiscalMonth = undefined;
      });
    }
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

  it('FiscalMonths menu should load FiscalMonths page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fiscal-month');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FiscalMonth').should('exist');
    cy.url().should('match', fiscalMonthPageUrlPattern);
  });

  describe('FiscalMonth page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fiscalMonthPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FiscalMonth page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fiscal-month/new$'));
        cy.getEntityCreateUpdateHeading('FiscalMonth');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalMonthPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fiscal-months',

          body: {
            ...fiscalMonthSample,
            fiscalYear: fiscalYear,
          },
        }).then(({ body }) => {
          fiscalMonth = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fiscal-months+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fiscalMonth],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fiscalMonthPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FiscalMonth page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fiscalMonth');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalMonthPageUrlPattern);
      });

      it('edit button click should load edit FiscalMonth page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FiscalMonth');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalMonthPageUrlPattern);
      });

      it('last delete button click should delete instance of FiscalMonth', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fiscalMonth').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalMonthPageUrlPattern);

        fiscalMonth = undefined;
      });
    });
  });

  describe('new FiscalMonth page', () => {
    beforeEach(() => {
      cy.visit(`${fiscalMonthPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FiscalMonth');
    });

    it('should create an instance of FiscalMonth', () => {
      cy.get(`[data-cy="monthNumber"]`).type('36527').should('have.value', '36527');

      cy.get(`[data-cy="startDate"]`).type('2023-08-16').should('have.value', '2023-08-16');

      cy.get(`[data-cy="endDate"]`).type('2023-08-15').should('have.value', '2023-08-15');

      cy.get(`[data-cy="fiscalMonthCode"]`).type('edge').should('have.value', 'edge');

      cy.get(`[data-cy="fiscalYear"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fiscalMonth = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fiscalMonthPageUrlPattern);
    });
  });
});
