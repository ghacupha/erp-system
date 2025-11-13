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

describe('WeeklyCounterfeitHolding e2e test', () => {
  const weeklyCounterfeitHoldingPageUrl = '/weekly-counterfeit-holding';
  const weeklyCounterfeitHoldingPageUrlPattern = new RegExp('/weekly-counterfeit-holding(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const weeklyCounterfeitHoldingSample = {
    reportingDate: '2023-10-03',
    dateConfiscated: '2023-10-03',
    serialNumber: 'expedite Rubber',
    depositorsNames: 'array',
    tellersNames: 'multi-byte monetize purple',
    dateSubmittedToCBK: '2023-10-04',
  };

  let weeklyCounterfeitHolding: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/weekly-counterfeit-holdings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/weekly-counterfeit-holdings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/weekly-counterfeit-holdings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (weeklyCounterfeitHolding) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/weekly-counterfeit-holdings/${weeklyCounterfeitHolding.id}`,
      }).then(() => {
        weeklyCounterfeitHolding = undefined;
      });
    }
  });

  it('WeeklyCounterfeitHoldings menu should load WeeklyCounterfeitHoldings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('weekly-counterfeit-holding');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WeeklyCounterfeitHolding').should('exist');
    cy.url().should('match', weeklyCounterfeitHoldingPageUrlPattern);
  });

  describe('WeeklyCounterfeitHolding page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(weeklyCounterfeitHoldingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WeeklyCounterfeitHolding page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/weekly-counterfeit-holding/new$'));
        cy.getEntityCreateUpdateHeading('WeeklyCounterfeitHolding');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCounterfeitHoldingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/weekly-counterfeit-holdings',
          body: weeklyCounterfeitHoldingSample,
        }).then(({ body }) => {
          weeklyCounterfeitHolding = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/weekly-counterfeit-holdings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [weeklyCounterfeitHolding],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(weeklyCounterfeitHoldingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WeeklyCounterfeitHolding page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('weeklyCounterfeitHolding');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCounterfeitHoldingPageUrlPattern);
      });

      it('edit button click should load edit WeeklyCounterfeitHolding page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WeeklyCounterfeitHolding');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCounterfeitHoldingPageUrlPattern);
      });

      it('last delete button click should delete instance of WeeklyCounterfeitHolding', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('weeklyCounterfeitHolding').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCounterfeitHoldingPageUrlPattern);

        weeklyCounterfeitHolding = undefined;
      });
    });
  });

  describe('new WeeklyCounterfeitHolding page', () => {
    beforeEach(() => {
      cy.visit(`${weeklyCounterfeitHoldingPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WeeklyCounterfeitHolding');
    });

    it('should create an instance of WeeklyCounterfeitHolding', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="dateConfiscated"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="serialNumber"]`).type('compressing').should('have.value', 'compressing');

      cy.get(`[data-cy="depositorsNames"]`).type('payment moratorium Balanced').should('have.value', 'payment moratorium Balanced');

      cy.get(`[data-cy="tellersNames"]`).type('Dollar').should('have.value', 'Dollar');

      cy.get(`[data-cy="dateSubmittedToCBK"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        weeklyCounterfeitHolding = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', weeklyCounterfeitHoldingPageUrlPattern);
    });
  });
});
