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

describe('LeaseRepaymentPeriod e2e test', () => {
  const leaseRepaymentPeriodPageUrl = '/lease-repayment-period';
  const leaseRepaymentPeriodPageUrlPattern = new RegExp('/lease-repayment-period(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseRepaymentPeriodSample = { sequenceNumber: 6350, periodCode: 'online' };

  let leaseRepaymentPeriod: any;
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
      body: {"monthNumber":62682,"startDate":"2023-08-15","endDate":"2023-08-15","fiscalMonthCode":"logistical Manager Cross-group"},
    }).then(({ body }) => {
      fiscalMonth = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-repayment-periods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-repayment-periods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-repayment-periods/*').as('deleteEntityRequest');
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
    if (leaseRepaymentPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-repayment-periods/${leaseRepaymentPeriod.id}`,
      }).then(() => {
        leaseRepaymentPeriod = undefined;
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

  it('LeaseRepaymentPeriods menu should load LeaseRepaymentPeriods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-repayment-period');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseRepaymentPeriod').should('exist');
    cy.url().should('match', leaseRepaymentPeriodPageUrlPattern);
  });

  describe('LeaseRepaymentPeriod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseRepaymentPeriodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseRepaymentPeriod page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-repayment-period/new$'));
        cy.getEntityCreateUpdateHeading('LeaseRepaymentPeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseRepaymentPeriodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-repayment-periods',
  
          body: {
            ...leaseRepaymentPeriodSample,
            fiscalMonth: fiscalMonth,
          },
        }).then(({ body }) => {
          leaseRepaymentPeriod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-repayment-periods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseRepaymentPeriod],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseRepaymentPeriodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(leaseRepaymentPeriodPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details LeaseRepaymentPeriod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseRepaymentPeriod');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseRepaymentPeriodPageUrlPattern);
      });

      it('edit button click should load edit LeaseRepaymentPeriod page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseRepaymentPeriod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseRepaymentPeriodPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of LeaseRepaymentPeriod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseRepaymentPeriod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseRepaymentPeriodPageUrlPattern);

        leaseRepaymentPeriod = undefined;
      });
    });
  });

  describe('new LeaseRepaymentPeriod page', () => {
    beforeEach(() => {
      cy.visit(`${leaseRepaymentPeriodPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseRepaymentPeriod');
    });

    it.skip('should create an instance of LeaseRepaymentPeriod', () => {
      cy.get(`[data-cy="sequenceNumber"]`).type('68811').should('have.value', '68811');

      cy.get(`[data-cy="startDate"]`).type('2024-07-21').should('have.value', '2024-07-21');

      cy.get(`[data-cy="endDate"]`).type('2024-07-21').should('have.value', '2024-07-21');

      cy.get(`[data-cy="periodCode"]`).type('Cheese dedicated').should('have.value', 'Cheese dedicated');

      cy.get(`[data-cy="fiscalMonth"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseRepaymentPeriod = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseRepaymentPeriodPageUrlPattern);
    });
  });
});
