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

describe('FiscalQuarter e2e test', () => {
  const fiscalQuarterPageUrl = '/fiscal-quarter';
  const fiscalQuarterPageUrlPattern = new RegExp('/fiscal-quarter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fiscalQuarterSample = {
    quarterNumber: 68994,
    startDate: '2023-08-15',
    endDate: '2023-08-15',
    fiscalQuarterCode: 'Unit Oklahoma Liaison',
  };

  let fiscalQuarter: any;
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
      body: { fiscalYearCode: 'technologies Lesotho', startDate: '2023-08-16', endDate: '2023-08-16', fiscalYearStatus: 'CLOSED' },
    }).then(({ body }) => {
      fiscalYear = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fiscal-quarters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fiscal-quarters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fiscal-quarters/*').as('deleteEntityRequest');
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
  });

  afterEach(() => {
    if (fiscalQuarter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-quarters/${fiscalQuarter.id}`,
      }).then(() => {
        fiscalQuarter = undefined;
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

  it('FiscalQuarters menu should load FiscalQuarters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fiscal-quarter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FiscalQuarter').should('exist');
    cy.url().should('match', fiscalQuarterPageUrlPattern);
  });

  describe('FiscalQuarter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fiscalQuarterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FiscalQuarter page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fiscal-quarter/new$'));
        cy.getEntityCreateUpdateHeading('FiscalQuarter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalQuarterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fiscal-quarters',

          body: {
            ...fiscalQuarterSample,
            fiscalYear: fiscalYear,
          },
        }).then(({ body }) => {
          fiscalQuarter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fiscal-quarters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fiscalQuarter],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fiscalQuarterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FiscalQuarter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fiscalQuarter');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalQuarterPageUrlPattern);
      });

      it('edit button click should load edit FiscalQuarter page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FiscalQuarter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalQuarterPageUrlPattern);
      });

      it('last delete button click should delete instance of FiscalQuarter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fiscalQuarter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fiscalQuarterPageUrlPattern);

        fiscalQuarter = undefined;
      });
    });
  });

  describe('new FiscalQuarter page', () => {
    beforeEach(() => {
      cy.visit(`${fiscalQuarterPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FiscalQuarter');
    });

    it('should create an instance of FiscalQuarter', () => {
      cy.get(`[data-cy="quarterNumber"]`).type('96883').should('have.value', '96883');

      cy.get(`[data-cy="startDate"]`).type('2023-08-16').should('have.value', '2023-08-16');

      cy.get(`[data-cy="endDate"]`).type('2023-08-16').should('have.value', '2023-08-16');

      cy.get(`[data-cy="fiscalQuarterCode"]`).type('synergies').should('have.value', 'synergies');

      cy.get(`[data-cy="fiscalYear"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fiscalQuarter = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fiscalQuarterPageUrlPattern);
    });
  });
});
