///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

describe('CommitteeType e2e test', () => {
  const committeeTypePageUrl = '/committee-type';
  const committeeTypePageUrlPattern = new RegExp('/committee-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const committeeTypeSample = { committeeTypeCode: 'IB' };

  let committeeType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/committee-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/committee-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/committee-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (committeeType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/committee-types/${committeeType.id}`,
      }).then(() => {
        committeeType = undefined;
      });
    }
  });

  it('CommitteeTypes menu should load CommitteeTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('committee-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommitteeType').should('exist');
    cy.url().should('match', committeeTypePageUrlPattern);
  });

  describe('CommitteeType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(committeeTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommitteeType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/committee-type/new$'));
        cy.getEntityCreateUpdateHeading('CommitteeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', committeeTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/committee-types',
          body: committeeTypeSample,
        }).then(({ body }) => {
          committeeType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/committee-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [committeeType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(committeeTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommitteeType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('committeeType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', committeeTypePageUrlPattern);
      });

      it('edit button click should load edit CommitteeType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommitteeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', committeeTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CommitteeType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('committeeType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', committeeTypePageUrlPattern);

        committeeType = undefined;
      });
    });
  });

  describe('new CommitteeType page', () => {
    beforeEach(() => {
      cy.visit(`${committeeTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CommitteeType');
    });

    it('should create an instance of CommitteeType', () => {
      cy.get(`[data-cy="committeeTypeCode"]`).type('Vatu').should('have.value', 'Vatu');

      cy.get(`[data-cy="committeeType"]`)
        .type('function De-engineered directional')
        .should('have.value', 'function De-engineered directional');

      cy.get(`[data-cy="committeeTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        committeeType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', committeeTypePageUrlPattern);
    });
  });
});
