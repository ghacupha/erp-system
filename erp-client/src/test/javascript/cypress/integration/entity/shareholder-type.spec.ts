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

describe('ShareholderType e2e test', () => {
  const shareholderTypePageUrl = '/shareholder-type';
  const shareholderTypePageUrlPattern = new RegExp('/shareholder-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const shareholderTypeSample = { shareHolderTypeCode: 'initiatives Unbranded', shareHolderType: 'PARTNERSHIP' };

  let shareholderType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shareholder-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shareholder-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shareholder-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shareholderType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shareholder-types/${shareholderType.id}`,
      }).then(() => {
        shareholderType = undefined;
      });
    }
  });

  it('ShareholderTypes menu should load ShareholderTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shareholder-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShareholderType').should('exist');
    cy.url().should('match', shareholderTypePageUrlPattern);
  });

  describe('ShareholderType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shareholderTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShareholderType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/shareholder-type/new$'));
        cy.getEntityCreateUpdateHeading('ShareholderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareholderTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shareholder-types',
          body: shareholderTypeSample,
        }).then(({ body }) => {
          shareholderType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shareholder-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shareholderType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(shareholderTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShareholderType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shareholderType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareholderTypePageUrlPattern);
      });

      it('edit button click should load edit ShareholderType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShareholderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareholderTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ShareholderType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shareholderType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareholderTypePageUrlPattern);

        shareholderType = undefined;
      });
    });
  });

  describe('new ShareholderType page', () => {
    beforeEach(() => {
      cy.visit(`${shareholderTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ShareholderType');
    });

    it('should create an instance of ShareholderType', () => {
      cy.get(`[data-cy="shareHolderTypeCode"]`).type('revolutionary Cheese Shoes').should('have.value', 'revolutionary Cheese Shoes');

      cy.get(`[data-cy="shareHolderType"]`).select('CORPORATE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        shareholderType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', shareholderTypePageUrlPattern);
    });
  });
});
