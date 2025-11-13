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

describe('AmortizationPeriod e2e test', () => {
  const amortizationPeriodPageUrl = '/amortization-period';
  const amortizationPeriodPageUrlPattern = new RegExp('/amortization-period(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const amortizationPeriodSample = { sequenceNumber: 68580, periodCode: 'Towels' };

  let amortizationPeriod: any;
  //let fiscalMonth: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/fiscal-months',
      body: {"monthNumber":57522,"startDate":"2023-08-16","endDate":"2023-08-16","fiscalMonthCode":"Manager"},
    }).then(({ body }) => {
      fiscalMonth = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/amortization-periods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/amortization-periods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/amortization-periods/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/fiscal-months', {
      statusCode: 200,
      body: [fiscalMonth],
    });

    cy.intercept('GET', '/api/amortization-periods', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (amortizationPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/amortization-periods/${amortizationPeriod.id}`,
      }).then(() => {
        amortizationPeriod = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('AmortizationPeriods menu should load AmortizationPeriods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('amortization-period');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AmortizationPeriod').should('exist');
    cy.url().should('match', amortizationPeriodPageUrlPattern);
  });

  describe('AmortizationPeriod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(amortizationPeriodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AmortizationPeriod page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/amortization-period/new$'));
        cy.getEntityCreateUpdateHeading('AmortizationPeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPeriodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/amortization-periods',
  
          body: {
            ...amortizationPeriodSample,
            fiscalMonth: fiscalMonth,
          },
        }).then(({ body }) => {
          amortizationPeriod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/amortization-periods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [amortizationPeriod],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(amortizationPeriodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(amortizationPeriodPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AmortizationPeriod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('amortizationPeriod');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPeriodPageUrlPattern);
      });

      it('edit button click should load edit AmortizationPeriod page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AmortizationPeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPeriodPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AmortizationPeriod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('amortizationPeriod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', amortizationPeriodPageUrlPattern);

        amortizationPeriod = undefined;
      });
    });
  });

  describe('new AmortizationPeriod page', () => {
    beforeEach(() => {
      cy.visit(`${amortizationPeriodPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AmortizationPeriod');
    });

    it.skip('should create an instance of AmortizationPeriod', () => {
      cy.get(`[data-cy="sequenceNumber"]`).type('22477').should('have.value', '22477');

      cy.get(`[data-cy="periodCode"]`).type('lavender').should('have.value', 'lavender');

      cy.get(`[data-cy="fiscalMonth"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        amortizationPeriod = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', amortizationPeriodPageUrlPattern);
    });
  });
});
