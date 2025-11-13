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

describe('CounterPartyDealType e2e test', () => {
  const counterPartyDealTypePageUrl = '/counter-party-deal-type';
  const counterPartyDealTypePageUrlPattern = new RegExp('/counter-party-deal-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const counterPartyDealTypeSample = { counterpartyDealCode: 'Illinois Wooden maximized', counterpartyDealTypeDetails: 'Sports Gorgeous' };

  let counterPartyDealType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/counter-party-deal-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/counter-party-deal-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/counter-party-deal-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (counterPartyDealType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/counter-party-deal-types/${counterPartyDealType.id}`,
      }).then(() => {
        counterPartyDealType = undefined;
      });
    }
  });

  it('CounterPartyDealTypes menu should load CounterPartyDealTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('counter-party-deal-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CounterPartyDealType').should('exist');
    cy.url().should('match', counterPartyDealTypePageUrlPattern);
  });

  describe('CounterPartyDealType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(counterPartyDealTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CounterPartyDealType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/counter-party-deal-type/new$'));
        cy.getEntityCreateUpdateHeading('CounterPartyDealType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyDealTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/counter-party-deal-types',
          body: counterPartyDealTypeSample,
        }).then(({ body }) => {
          counterPartyDealType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/counter-party-deal-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [counterPartyDealType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(counterPartyDealTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CounterPartyDealType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('counterPartyDealType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyDealTypePageUrlPattern);
      });

      it('edit button click should load edit CounterPartyDealType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CounterPartyDealType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyDealTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CounterPartyDealType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('counterPartyDealType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterPartyDealTypePageUrlPattern);

        counterPartyDealType = undefined;
      });
    });
  });

  describe('new CounterPartyDealType page', () => {
    beforeEach(() => {
      cy.visit(`${counterPartyDealTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CounterPartyDealType');
    });

    it('should create an instance of CounterPartyDealType', () => {
      cy.get(`[data-cy="counterpartyDealCode"]`).type('Freeway feed').should('have.value', 'Freeway feed');

      cy.get(`[data-cy="counterpartyDealTypeDetails"]`).type('Licensed Soap Program').should('have.value', 'Licensed Soap Program');

      cy.get(`[data-cy="counterpartyDealTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        counterPartyDealType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', counterPartyDealTypePageUrlPattern);
    });
  });
});
