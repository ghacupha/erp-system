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

describe('StringQuestionBase e2e test', () => {
  const stringQuestionBasePageUrl = '/string-question-base';
  const stringQuestionBasePageUrlPattern = new RegExp('/string-question-base(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const stringQuestionBaseSample = { key: 'Home', label: 'Rupee', order: 26653, controlType: 'TEXTAREA' };

  let stringQuestionBase: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/string-question-bases+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/string-question-bases').as('postEntityRequest');
    cy.intercept('DELETE', '/api/string-question-bases/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (stringQuestionBase) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/string-question-bases/${stringQuestionBase.id}`,
      }).then(() => {
        stringQuestionBase = undefined;
      });
    }
  });

  it('StringQuestionBases menu should load StringQuestionBases page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('string-question-base');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StringQuestionBase').should('exist');
    cy.url().should('match', stringQuestionBasePageUrlPattern);
  });

  describe('StringQuestionBase page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(stringQuestionBasePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StringQuestionBase page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/string-question-base/new$'));
        cy.getEntityCreateUpdateHeading('StringQuestionBase');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', stringQuestionBasePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/string-question-bases',
          body: stringQuestionBaseSample,
        }).then(({ body }) => {
          stringQuestionBase = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/string-question-bases+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [stringQuestionBase],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(stringQuestionBasePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StringQuestionBase page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('stringQuestionBase');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', stringQuestionBasePageUrlPattern);
      });

      it('edit button click should load edit StringQuestionBase page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StringQuestionBase');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', stringQuestionBasePageUrlPattern);
      });

      it('last delete button click should delete instance of StringQuestionBase', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('stringQuestionBase').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', stringQuestionBasePageUrlPattern);

        stringQuestionBase = undefined;
      });
    });
  });

  describe('new StringQuestionBase page', () => {
    beforeEach(() => {
      cy.visit(`${stringQuestionBasePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('StringQuestionBase');
    });

    it('should create an instance of StringQuestionBase', () => {
      cy.get(`[data-cy="value"]`).type('Analyst').should('have.value', 'Analyst');

      cy.get(`[data-cy="key"]`).type('South access').should('have.value', 'South access');

      cy.get(`[data-cy="label"]`).type('navigating').should('have.value', 'navigating');

      cy.get(`[data-cy="required"]`).should('not.be.checked');
      cy.get(`[data-cy="required"]`).click().should('be.checked');

      cy.get(`[data-cy="order"]`).type('29928').should('have.value', '29928');

      cy.get(`[data-cy="controlType"]`).select('TEXTBOX');

      cy.get(`[data-cy="placeholder"]`).type('Handcrafted Concrete Jewelery').should('have.value', 'Handcrafted Concrete Jewelery');

      cy.get(`[data-cy="iterable"]`).should('not.be.checked');
      cy.get(`[data-cy="iterable"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        stringQuestionBase = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', stringQuestionBasePageUrlPattern);
    });
  });
});
