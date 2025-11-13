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

describe('UltimateBeneficiaryTypes e2e test', () => {
  const ultimateBeneficiaryTypesPageUrl = '/ultimate-beneficiary-types';
  const ultimateBeneficiaryTypesPageUrlPattern = new RegExp('/ultimate-beneficiary-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const ultimateBeneficiaryTypesSample = {
    ultimateBeneficiaryTypeCode: 'network salmon Bulgarian',
    ultimateBeneficiaryType: 'Investment Fresh',
  };

  let ultimateBeneficiaryTypes: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ultimate-beneficiary-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ultimate-beneficiary-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ultimate-beneficiary-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ultimateBeneficiaryTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ultimate-beneficiary-types/${ultimateBeneficiaryTypes.id}`,
      }).then(() => {
        ultimateBeneficiaryTypes = undefined;
      });
    }
  });

  it('UltimateBeneficiaryTypes menu should load UltimateBeneficiaryTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ultimate-beneficiary-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UltimateBeneficiaryTypes').should('exist');
    cy.url().should('match', ultimateBeneficiaryTypesPageUrlPattern);
  });

  describe('UltimateBeneficiaryTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ultimateBeneficiaryTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UltimateBeneficiaryTypes page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/ultimate-beneficiary-types/new$'));
        cy.getEntityCreateUpdateHeading('UltimateBeneficiaryTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ultimate-beneficiary-types',
          body: ultimateBeneficiaryTypesSample,
        }).then(({ body }) => {
          ultimateBeneficiaryTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ultimate-beneficiary-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ultimateBeneficiaryTypes],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(ultimateBeneficiaryTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UltimateBeneficiaryTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ultimateBeneficiaryTypes');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryTypesPageUrlPattern);
      });

      it('edit button click should load edit UltimateBeneficiaryTypes page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UltimateBeneficiaryTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of UltimateBeneficiaryTypes', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ultimateBeneficiaryTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', ultimateBeneficiaryTypesPageUrlPattern);

        ultimateBeneficiaryTypes = undefined;
      });
    });
  });

  describe('new UltimateBeneficiaryTypes page', () => {
    beforeEach(() => {
      cy.visit(`${ultimateBeneficiaryTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('UltimateBeneficiaryTypes');
    });

    it('should create an instance of UltimateBeneficiaryTypes', () => {
      cy.get(`[data-cy="ultimateBeneficiaryTypeCode"]`).type('neutral Bacon').should('have.value', 'neutral Bacon');

      cy.get(`[data-cy="ultimateBeneficiaryType"]`).type('Automotive').should('have.value', 'Automotive');

      cy.get(`[data-cy="ultimateBeneficiaryTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        ultimateBeneficiaryTypes = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', ultimateBeneficiaryTypesPageUrlPattern);
    });
  });
});
