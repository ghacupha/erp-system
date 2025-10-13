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

describe('FraudType e2e test', () => {
  const fraudTypePageUrl = '/fraud-type';
  const fraudTypePageUrlPattern = new RegExp('/fraud-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fraudTypeSample = { fraudTypeCode: '(Malvinas) connecting Maryland', fraudType: 'Dollar' };

  let fraudType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fraud-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fraud-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fraud-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fraudType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fraud-types/${fraudType.id}`,
      }).then(() => {
        fraudType = undefined;
      });
    }
  });

  it('FraudTypes menu should load FraudTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fraud-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FraudType').should('exist');
    cy.url().should('match', fraudTypePageUrlPattern);
  });

  describe('FraudType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fraudTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FraudType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fraud-type/new$'));
        cy.getEntityCreateUpdateHeading('FraudType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fraud-types',
          body: fraudTypeSample,
        }).then(({ body }) => {
          fraudType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fraud-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fraudType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fraudTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FraudType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fraudType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudTypePageUrlPattern);
      });

      it('edit button click should load edit FraudType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FraudType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FraudType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fraudType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fraudTypePageUrlPattern);

        fraudType = undefined;
      });
    });
  });

  describe('new FraudType page', () => {
    beforeEach(() => {
      cy.visit(`${fraudTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FraudType');
    });

    it('should create an instance of FraudType', () => {
      cy.get(`[data-cy="fraudTypeCode"]`).type('Mountains Associate').should('have.value', 'Mountains Associate');

      cy.get(`[data-cy="fraudType"]`).type('implement').should('have.value', 'implement');

      cy.get(`[data-cy="fraudTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fraudType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fraudTypePageUrlPattern);
    });
  });
});
