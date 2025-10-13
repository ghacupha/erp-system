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

describe('LeasePeriod e2e test', () => {
  const leasePeriodPageUrl = '/lease-period';
  const leasePeriodPageUrlPattern = new RegExp('/lease-period(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leasePeriodSample = { sequenceNumber: 93505, periodCode: 'Computer best-of-breed' };

  let leasePeriod: any;
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
      body: {"monthNumber":44317,"startDate":"2023-08-15","endDate":"2023-08-16","fiscalMonthCode":"drive"},
    }).then(({ body }) => {
      fiscalMonth = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-periods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-periods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-periods/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/fiscal-months', {
      statusCode: 200,
      body: [fiscalMonth],
    });

  });
   */

  afterEach(() => {
    if (leasePeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-periods/${leasePeriod.id}`,
      }).then(() => {
        leasePeriod = undefined;
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

  it('LeasePeriods menu should load LeasePeriods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-period');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeasePeriod').should('exist');
    cy.url().should('match', leasePeriodPageUrlPattern);
  });

  describe('LeasePeriod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leasePeriodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeasePeriod page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-period/new$'));
        cy.getEntityCreateUpdateHeading('LeasePeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePeriodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-periods',
  
          body: {
            ...leasePeriodSample,
            fiscalMonth: fiscalMonth,
          },
        }).then(({ body }) => {
          leasePeriod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-periods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leasePeriod],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leasePeriodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leasePeriodPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeasePeriod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leasePeriod');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePeriodPageUrlPattern);
      });

      it('edit button click should load edit LeasePeriod page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeasePeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePeriodPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeasePeriod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leasePeriod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leasePeriodPageUrlPattern);

        leasePeriod = undefined;
      });
    });
  });

  describe('new LeasePeriod page', () => {
    beforeEach(() => {
      cy.visit(`${leasePeriodPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeasePeriod');
    });

    it.skip('should create an instance of LeasePeriod', () => {
      cy.get(`[data-cy="sequenceNumber"]`).type('905').should('have.value', '905');

      cy.get(`[data-cy="periodCode"]`).type('Implementation Market Account').should('have.value', 'Implementation Market Account');

      cy.get(`[data-cy="fiscalMonth"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leasePeriod = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leasePeriodPageUrlPattern);
    });
  });
});
