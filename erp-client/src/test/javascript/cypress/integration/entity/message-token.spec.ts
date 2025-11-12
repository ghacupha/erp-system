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

describe('MessageToken e2e test', () => {
  const messageTokenPageUrl = '/message-token';
  const messageTokenPageUrlPattern = new RegExp('/message-token(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const messageTokenSample = { timeSent: 97941, tokenValue: 'grey Shoes' };

  let messageToken: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/message-tokens+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/message-tokens').as('postEntityRequest');
    cy.intercept('DELETE', '/api/message-tokens/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (messageToken) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/message-tokens/${messageToken.id}`,
      }).then(() => {
        messageToken = undefined;
      });
    }
  });

  it('MessageTokens menu should load MessageTokens page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('message-token');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MessageToken').should('exist');
    cy.url().should('match', messageTokenPageUrlPattern);
  });

  describe('MessageToken page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(messageTokenPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MessageToken page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/message-token/new$'));
        cy.getEntityCreateUpdateHeading('MessageToken');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', messageTokenPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/message-tokens',
          body: messageTokenSample,
        }).then(({ body }) => {
          messageToken = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/message-tokens+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [messageToken],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(messageTokenPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MessageToken page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('messageToken');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', messageTokenPageUrlPattern);
      });

      it('edit button click should load edit MessageToken page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MessageToken');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', messageTokenPageUrlPattern);
      });

      it('last delete button click should delete instance of MessageToken', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('messageToken').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', messageTokenPageUrlPattern);

        messageToken = undefined;
      });
    });
  });

  describe('new MessageToken page', () => {
    beforeEach(() => {
      cy.visit(`${messageTokenPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('MessageToken');
    });

    it('should create an instance of MessageToken', () => {
      cy.get(`[data-cy="description"]`).type('Baby').should('have.value', 'Baby');

      cy.get(`[data-cy="timeSent"]`).type('33733').should('have.value', '33733');

      cy.get(`[data-cy="tokenValue"]`).type('Fresh').should('have.value', 'Fresh');

      cy.get(`[data-cy="received"]`).should('not.be.checked');
      cy.get(`[data-cy="received"]`).click().should('be.checked');

      cy.get(`[data-cy="actioned"]`).should('not.be.checked');
      cy.get(`[data-cy="actioned"]`).click().should('be.checked');

      cy.get(`[data-cy="contentFullyEnqueued"]`).should('not.be.checked');
      cy.get(`[data-cy="contentFullyEnqueued"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        messageToken = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', messageTokenPageUrlPattern);
    });
  });
});
