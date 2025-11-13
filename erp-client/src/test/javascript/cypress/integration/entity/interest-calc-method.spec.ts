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

describe('InterestCalcMethod e2e test', () => {
  const interestCalcMethodPageUrl = '/interest-calc-method';
  const interestCalcMethodPageUrlPattern = new RegExp('/interest-calc-method(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const interestCalcMethodSample = { interestCalculationMethodCode: 'user', interestCalculationMthodType: 'mobile e-tailers utilize' };

  let interestCalcMethod: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/interest-calc-methods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/interest-calc-methods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/interest-calc-methods/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (interestCalcMethod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/interest-calc-methods/${interestCalcMethod.id}`,
      }).then(() => {
        interestCalcMethod = undefined;
      });
    }
  });

  it('InterestCalcMethods menu should load InterestCalcMethods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('interest-calc-method');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InterestCalcMethod').should('exist');
    cy.url().should('match', interestCalcMethodPageUrlPattern);
  });

  describe('InterestCalcMethod page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(interestCalcMethodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InterestCalcMethod page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/interest-calc-method/new$'));
        cy.getEntityCreateUpdateHeading('InterestCalcMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interestCalcMethodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/interest-calc-methods',
          body: interestCalcMethodSample,
        }).then(({ body }) => {
          interestCalcMethod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/interest-calc-methods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [interestCalcMethod],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(interestCalcMethodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InterestCalcMethod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('interestCalcMethod');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interestCalcMethodPageUrlPattern);
      });

      it('edit button click should load edit InterestCalcMethod page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InterestCalcMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interestCalcMethodPageUrlPattern);
      });

      it('last delete button click should delete instance of InterestCalcMethod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('interestCalcMethod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', interestCalcMethodPageUrlPattern);

        interestCalcMethod = undefined;
      });
    });
  });

  describe('new InterestCalcMethod page', () => {
    beforeEach(() => {
      cy.visit(`${interestCalcMethodPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('InterestCalcMethod');
    });

    it('should create an instance of InterestCalcMethod', () => {
      cy.get(`[data-cy="interestCalculationMethodCode"]`)
        .type('schemas interfaces Keyboard')
        .should('have.value', 'schemas interfaces Keyboard');

      cy.get(`[data-cy="interestCalculationMthodType"]`).type('Bedfordshire Manager').should('have.value', 'Bedfordshire Manager');

      cy.get(`[data-cy="interestCalculationMethodDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        interestCalcMethod = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', interestCalcMethodPageUrlPattern);
    });
  });
});
