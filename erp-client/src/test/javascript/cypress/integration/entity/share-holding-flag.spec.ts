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

describe('ShareHoldingFlag e2e test', () => {
  const shareHoldingFlagPageUrl = '/share-holding-flag';
  const shareHoldingFlagPageUrlPattern = new RegExp('/share-holding-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const shareHoldingFlagSample = { shareholdingFlagTypeCode: 'Y', shareholdingFlagType: 'up' };

  let shareHoldingFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/share-holding-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/share-holding-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/share-holding-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shareHoldingFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/share-holding-flags/${shareHoldingFlag.id}`,
      }).then(() => {
        shareHoldingFlag = undefined;
      });
    }
  });

  it('ShareHoldingFlags menu should load ShareHoldingFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('share-holding-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShareHoldingFlag').should('exist');
    cy.url().should('match', shareHoldingFlagPageUrlPattern);
  });

  describe('ShareHoldingFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shareHoldingFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShareHoldingFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/share-holding-flag/new$'));
        cy.getEntityCreateUpdateHeading('ShareHoldingFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareHoldingFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/share-holding-flags',
          body: shareHoldingFlagSample,
        }).then(({ body }) => {
          shareHoldingFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/share-holding-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shareHoldingFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(shareHoldingFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShareHoldingFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shareHoldingFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareHoldingFlagPageUrlPattern);
      });

      it('edit button click should load edit ShareHoldingFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShareHoldingFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareHoldingFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of ShareHoldingFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shareHoldingFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shareHoldingFlagPageUrlPattern);

        shareHoldingFlag = undefined;
      });
    });
  });

  describe('new ShareHoldingFlag page', () => {
    beforeEach(() => {
      cy.visit(`${shareHoldingFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ShareHoldingFlag');
    });

    it('should create an instance of ShareHoldingFlag', () => {
      cy.get(`[data-cy="shareholdingFlagTypeCode"]`).select('N');

      cy.get(`[data-cy="shareholdingFlagType"]`).type('Rupee engage front-end').should('have.value', 'Rupee engage front-end');

      cy.get(`[data-cy="shareholdingTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        shareHoldingFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', shareHoldingFlagPageUrlPattern);
    });
  });
});
