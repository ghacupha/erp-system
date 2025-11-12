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

describe('CrbAccountHolderType e2e test', () => {
  const crbAccountHolderTypePageUrl = '/crb-account-holder-type';
  const crbAccountHolderTypePageUrlPattern = new RegExp('/crb-account-holder-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbAccountHolderTypeSample = {
    accountHolderCategoryTypeCode: 'Identity transmitter Maldives',
    accountHolderCategoryType: 'Regional Harbors',
  };

  let crbAccountHolderType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-account-holder-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-account-holder-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-account-holder-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbAccountHolderType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-account-holder-types/${crbAccountHolderType.id}`,
      }).then(() => {
        crbAccountHolderType = undefined;
      });
    }
  });

  it('CrbAccountHolderTypes menu should load CrbAccountHolderTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-account-holder-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbAccountHolderType').should('exist');
    cy.url().should('match', crbAccountHolderTypePageUrlPattern);
  });

  describe('CrbAccountHolderType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbAccountHolderTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbAccountHolderType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-account-holder-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbAccountHolderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountHolderTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-account-holder-types',
          body: crbAccountHolderTypeSample,
        }).then(({ body }) => {
          crbAccountHolderType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-account-holder-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbAccountHolderType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbAccountHolderTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbAccountHolderType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbAccountHolderType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountHolderTypePageUrlPattern);
      });

      it('edit button click should load edit CrbAccountHolderType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbAccountHolderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountHolderTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbAccountHolderType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbAccountHolderType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbAccountHolderTypePageUrlPattern);

        crbAccountHolderType = undefined;
      });
    });
  });

  describe('new CrbAccountHolderType page', () => {
    beforeEach(() => {
      cy.visit(`${crbAccountHolderTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbAccountHolderType');
    });

    it('should create an instance of CrbAccountHolderType', () => {
      cy.get(`[data-cy="accountHolderCategoryTypeCode"]`).type('Bedfordshire Forks').should('have.value', 'Bedfordshire Forks');

      cy.get(`[data-cy="accountHolderCategoryType"]`).type('Computer silver SSL').should('have.value', 'Computer silver SSL');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbAccountHolderType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbAccountHolderTypePageUrlPattern);
    });
  });
});
