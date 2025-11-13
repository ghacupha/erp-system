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

describe('GenderType e2e test', () => {
  const genderTypePageUrl = '/gender-type';
  const genderTypePageUrlPattern = new RegExp('/gender-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const genderTypeSample = { genderCode: 'Garden', genderType: 'MALE' };

  let genderType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gender-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gender-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gender-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (genderType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gender-types/${genderType.id}`,
      }).then(() => {
        genderType = undefined;
      });
    }
  });

  it('GenderTypes menu should load GenderTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gender-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GenderType').should('exist');
    cy.url().should('match', genderTypePageUrlPattern);
  });

  describe('GenderType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(genderTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GenderType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/gender-type/new$'));
        cy.getEntityCreateUpdateHeading('GenderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genderTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gender-types',
          body: genderTypeSample,
        }).then(({ body }) => {
          genderType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gender-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [genderType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(genderTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GenderType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('genderType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genderTypePageUrlPattern);
      });

      it('edit button click should load edit GenderType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GenderType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genderTypePageUrlPattern);
      });

      it('last delete button click should delete instance of GenderType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('genderType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genderTypePageUrlPattern);

        genderType = undefined;
      });
    });
  });

  describe('new GenderType page', () => {
    beforeEach(() => {
      cy.visit(`${genderTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('GenderType');
    });

    it('should create an instance of GenderType', () => {
      cy.get(`[data-cy="genderCode"]`).type('Money Trail').should('have.value', 'Money Trail');

      cy.get(`[data-cy="genderType"]`).select('OTHERS');

      cy.get(`[data-cy="genderDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        genderType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', genderTypePageUrlPattern);
    });
  });
});
