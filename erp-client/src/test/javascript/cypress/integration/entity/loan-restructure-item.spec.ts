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

describe('LoanRestructureItem e2e test', () => {
  const loanRestructureItemPageUrl = '/loan-restructure-item';
  const loanRestructureItemPageUrlPattern = new RegExp('/loan-restructure-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanRestructureItemSample = { loanRestructureItemCode: 'Stravenue', loanRestructureItemType: 'Outdoors CSS generating' };

  let loanRestructureItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-restructure-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-restructure-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-restructure-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanRestructureItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-restructure-items/${loanRestructureItem.id}`,
      }).then(() => {
        loanRestructureItem = undefined;
      });
    }
  });

  it('LoanRestructureItems menu should load LoanRestructureItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-restructure-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanRestructureItem').should('exist');
    cy.url().should('match', loanRestructureItemPageUrlPattern);
  });

  describe('LoanRestructureItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanRestructureItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanRestructureItem page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-restructure-item/new$'));
        cy.getEntityCreateUpdateHeading('LoanRestructureItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-restructure-items',
          body: loanRestructureItemSample,
        }).then(({ body }) => {
          loanRestructureItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-restructure-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanRestructureItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanRestructureItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanRestructureItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanRestructureItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureItemPageUrlPattern);
      });

      it('edit button click should load edit LoanRestructureItem page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanRestructureItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureItemPageUrlPattern);
      });

      it('last delete button click should delete instance of LoanRestructureItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanRestructureItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanRestructureItemPageUrlPattern);

        loanRestructureItem = undefined;
      });
    });
  });

  describe('new LoanRestructureItem page', () => {
    beforeEach(() => {
      cy.visit(`${loanRestructureItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanRestructureItem');
    });

    it('should create an instance of LoanRestructureItem', () => {
      cy.get(`[data-cy="loanRestructureItemCode"]`).type('help-desk ROI methodologies').should('have.value', 'help-desk ROI methodologies');

      cy.get(`[data-cy="loanRestructureItemType"]`).type('Rial Mexican').should('have.value', 'Rial Mexican');

      cy.get(`[data-cy="loanRestructureItemDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanRestructureItem = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanRestructureItemPageUrlPattern);
    });
  });
});
