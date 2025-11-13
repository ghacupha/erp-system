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

describe('QuestionBase e2e test', () => {
  const questionBasePageUrl = '/question-base';
  const questionBasePageUrlPattern = new RegExp('/question-base(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const questionBaseSample = {
    context: 'Automotive',
    serial: 'b500c903-c95a-46ad-a038-7313d1547ceb',
    questionBaseKey: 'Rubber',
    questionBaseLabel: 'blue framework',
    order: 30865,
    controlType: 'TEL',
  };

  let questionBase: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/question-bases+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/question-bases').as('postEntityRequest');
    cy.intercept('DELETE', '/api/question-bases/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (questionBase) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/question-bases/${questionBase.id}`,
      }).then(() => {
        questionBase = undefined;
      });
    }
  });

  it('QuestionBases menu should load QuestionBases page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('question-base');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuestionBase').should('exist');
    cy.url().should('match', questionBasePageUrlPattern);
  });

  describe('QuestionBase page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(questionBasePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuestionBase page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/question-base/new$'));
        cy.getEntityCreateUpdateHeading('QuestionBase');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionBasePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/question-bases',
          body: questionBaseSample,
        }).then(({ body }) => {
          questionBase = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/question-bases+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [questionBase],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(questionBasePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuestionBase page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('questionBase');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionBasePageUrlPattern);
      });

      it('edit button click should load edit QuestionBase page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuestionBase');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionBasePageUrlPattern);
      });

      it('last delete button click should delete instance of QuestionBase', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('questionBase').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', questionBasePageUrlPattern);

        questionBase = undefined;
      });
    });
  });

  describe('new QuestionBase page', () => {
    beforeEach(() => {
      cy.visit(`${questionBasePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('QuestionBase');
    });

    it('should create an instance of QuestionBase', () => {
      cy.get(`[data-cy="context"]`).type('Associate').should('have.value', 'Associate');

      cy.get(`[data-cy="serial"]`)
        .type('138ac7b4-8e07-4aed-ab32-20c1892776f9')
        .invoke('val')
        .should('match', new RegExp('138ac7b4-8e07-4aed-ab32-20c1892776f9'));

      cy.get(`[data-cy="questionBaseValue"]`).type('Distributed').should('have.value', 'Distributed');

      cy.get(`[data-cy="questionBaseKey"]`).type('USB ubiquitous').should('have.value', 'USB ubiquitous');

      cy.get(`[data-cy="questionBaseLabel"]`).type('online bus Applications').should('have.value', 'online bus Applications');

      cy.get(`[data-cy="required"]`).should('not.be.checked');
      cy.get(`[data-cy="required"]`).click().should('be.checked');

      cy.get(`[data-cy="order"]`).type('78232').should('have.value', '78232');

      cy.get(`[data-cy="controlType"]`).select('TEXTBOX');

      cy.get(`[data-cy="placeholder"]`).type('Granite enhance').should('have.value', 'Granite enhance');

      cy.get(`[data-cy="iterable"]`).should('not.be.checked');
      cy.get(`[data-cy="iterable"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        questionBase = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', questionBasePageUrlPattern);
    });
  });
});
