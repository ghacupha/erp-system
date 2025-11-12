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

describe('CounterpartyType e2e test', () => {
  const counterpartyTypePageUrl = '/counterparty-type';
  const counterpartyTypePageUrlPattern = new RegExp('/counterparty-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const counterpartyTypeSample = { counterpartyTypeCode: 'Shoes', counterPartyType: 'Music bricks-and-clicks Front-line' };

  let counterpartyType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/counterparty-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/counterparty-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/counterparty-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (counterpartyType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/counterparty-types/${counterpartyType.id}`,
      }).then(() => {
        counterpartyType = undefined;
      });
    }
  });

  it('CounterpartyTypes menu should load CounterpartyTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('counterparty-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CounterpartyType').should('exist');
    cy.url().should('match', counterpartyTypePageUrlPattern);
  });

  describe('CounterpartyType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(counterpartyTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CounterpartyType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/counterparty-type/new$'));
        cy.getEntityCreateUpdateHeading('CounterpartyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterpartyTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/counterparty-types',
          body: counterpartyTypeSample,
        }).then(({ body }) => {
          counterpartyType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/counterparty-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [counterpartyType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(counterpartyTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CounterpartyType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('counterpartyType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterpartyTypePageUrlPattern);
      });

      it('edit button click should load edit CounterpartyType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CounterpartyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterpartyTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CounterpartyType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('counterpartyType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', counterpartyTypePageUrlPattern);

        counterpartyType = undefined;
      });
    });
  });

  describe('new CounterpartyType page', () => {
    beforeEach(() => {
      cy.visit(`${counterpartyTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CounterpartyType');
    });

    it('should create an instance of CounterpartyType', () => {
      cy.get(`[data-cy="counterpartyTypeCode"]`).type('Assistant').should('have.value', 'Assistant');

      cy.get(`[data-cy="counterPartyType"]`).type('transmit Maryland').should('have.value', 'transmit Maryland');

      cy.get(`[data-cy="counterpartyTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        counterpartyType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', counterpartyTypePageUrlPattern);
    });
  });
});
