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

describe('DepreciationPeriod e2e test', () => {
  const depreciationPeriodPageUrl = '/depreciation-period';
  const depreciationPeriodPageUrlPattern = new RegExp('/depreciation-period(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const depreciationPeriodSample = { startDate: '2023-07-03', endDate: '2023-07-04', periodCode: 'Chief Strategist' };

  let depreciationPeriod: any;
  //let fiscalYear: any;
  //let fiscalMonth: any;
  //let fiscalQuarter: any;

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
      url: '/api/fiscal-years',
      body: {"fiscalYearCode":"Loan capacitor Assistant","startDate":"2023-08-15","endDate":"2023-08-16","fiscalYearStatus":"IN_PROGRESS"},
    }).then(({ body }) => {
      fiscalYear = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/fiscal-months',
      body: {"monthNumber":36882,"startDate":"2023-08-16","endDate":"2023-08-15","fiscalMonthCode":"online Home"},
    }).then(({ body }) => {
      fiscalMonth = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/fiscal-quarters',
      body: {"quarterNumber":70955,"startDate":"2023-08-16","endDate":"2023-08-15","fiscalQuarterCode":"B2B Cloned Arizona"},
    }).then(({ body }) => {
      fiscalQuarter = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/depreciation-periods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depreciation-periods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depreciation-periods/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/depreciation-periods', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/fiscal-years', {
      statusCode: 200,
      body: [fiscalYear],
    });

    cy.intercept('GET', '/api/fiscal-months', {
      statusCode: 200,
      body: [fiscalMonth],
    });

    cy.intercept('GET', '/api/fiscal-quarters', {
      statusCode: 200,
      body: [fiscalQuarter],
    });

  });
   */

  afterEach(() => {
    if (depreciationPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-periods/${depreciationPeriod.id}`,
      }).then(() => {
        depreciationPeriod = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (fiscalYear) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-years/${fiscalYear.id}`,
      }).then(() => {
        fiscalYear = undefined;
      });
    }
    if (fiscalMonth) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-months/${fiscalMonth.id}`,
      }).then(() => {
        fiscalMonth = undefined;
      });
    }
    if (fiscalQuarter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fiscal-quarters/${fiscalQuarter.id}`,
      }).then(() => {
        fiscalQuarter = undefined;
      });
    }
  });
   */

  it('DepreciationPeriods menu should load DepreciationPeriods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('depreciation-period');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepreciationPeriod').should('exist');
    cy.url().should('match', depreciationPeriodPageUrlPattern);
  });

  describe('DepreciationPeriod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depreciationPeriodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepreciationPeriod page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/depreciation-period/new$'));
        cy.getEntityCreateUpdateHeading('DepreciationPeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationPeriodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depreciation-periods',
  
          body: {
            ...depreciationPeriodSample,
            fiscalYear: fiscalYear,
            fiscalMonth: fiscalMonth,
            fiscalQuarter: fiscalQuarter,
          },
        }).then(({ body }) => {
          depreciationPeriod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depreciation-periods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [depreciationPeriod],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depreciationPeriodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(depreciationPeriodPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DepreciationPeriod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('depreciationPeriod');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationPeriodPageUrlPattern);
      });

      it('edit button click should load edit DepreciationPeriod page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepreciationPeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationPeriodPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of DepreciationPeriod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('depreciationPeriod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationPeriodPageUrlPattern);

        depreciationPeriod = undefined;
      });
    });
  });

  describe('new DepreciationPeriod page', () => {
    beforeEach(() => {
      cy.visit(`${depreciationPeriodPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepreciationPeriod');
    });

    it.skip('should create an instance of DepreciationPeriod', () => {
      cy.get(`[data-cy="startDate"]`).type('2023-07-03').should('have.value', '2023-07-03');

      cy.get(`[data-cy="endDate"]`).type('2023-07-04').should('have.value', '2023-07-04');

      cy.get(`[data-cy="depreciationPeriodStatus"]`).select('CLOSED');

      cy.get(`[data-cy="periodCode"]`).type('Awesome').should('have.value', 'Awesome');

      cy.get(`[data-cy="processLocked"]`).should('not.be.checked');
      cy.get(`[data-cy="processLocked"]`).click().should('be.checked');

      cy.get(`[data-cy="fiscalYear"]`).select(1);
      cy.get(`[data-cy="fiscalMonth"]`).select(1);
      cy.get(`[data-cy="fiscalQuarter"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        depreciationPeriod = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depreciationPeriodPageUrlPattern);
    });
  });
});
