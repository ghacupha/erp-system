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

describe('CreditCardOwnership e2e test', () => {
  const creditCardOwnershipPageUrl = '/credit-card-ownership';
  const creditCardOwnershipPageUrlPattern = new RegExp('/credit-card-ownership(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const creditCardOwnershipSample = { creditCardOwnershipCategoryCode: 'Metal Plastic', creditCardOwnershipCategoryType: 'INDIVIDUAL' };

  let creditCardOwnership: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/credit-card-ownerships+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/credit-card-ownerships').as('postEntityRequest');
    cy.intercept('DELETE', '/api/credit-card-ownerships/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (creditCardOwnership) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/credit-card-ownerships/${creditCardOwnership.id}`,
      }).then(() => {
        creditCardOwnership = undefined;
      });
    }
  });

  it('CreditCardOwnerships menu should load CreditCardOwnerships page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('credit-card-ownership');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CreditCardOwnership').should('exist');
    cy.url().should('match', creditCardOwnershipPageUrlPattern);
  });

  describe('CreditCardOwnership page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(creditCardOwnershipPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CreditCardOwnership page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/credit-card-ownership/new$'));
        cy.getEntityCreateUpdateHeading('CreditCardOwnership');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardOwnershipPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/credit-card-ownerships',
          body: creditCardOwnershipSample,
        }).then(({ body }) => {
          creditCardOwnership = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/credit-card-ownerships+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [creditCardOwnership],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(creditCardOwnershipPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CreditCardOwnership page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('creditCardOwnership');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardOwnershipPageUrlPattern);
      });

      it('edit button click should load edit CreditCardOwnership page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CreditCardOwnership');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardOwnershipPageUrlPattern);
      });

      it('last delete button click should delete instance of CreditCardOwnership', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('creditCardOwnership').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardOwnershipPageUrlPattern);

        creditCardOwnership = undefined;
      });
    });
  });

  describe('new CreditCardOwnership page', () => {
    beforeEach(() => {
      cy.visit(`${creditCardOwnershipPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CreditCardOwnership');
    });

    it('should create an instance of CreditCardOwnership', () => {
      cy.get(`[data-cy="creditCardOwnershipCategoryCode"]`).type('Dinar toolset').should('have.value', 'Dinar toolset');

      cy.get(`[data-cy="creditCardOwnershipCategoryType"]`).select('NONRESIDENTS');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        creditCardOwnership = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', creditCardOwnershipPageUrlPattern);
    });
  });
});
