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

describe('AnticipatedMaturityPeriood e2e test', () => {
  const anticipatedMaturityPerioodPageUrl = '/anticipated-maturity-periood';
  const anticipatedMaturityPerioodPageUrlPattern = new RegExp('/anticipated-maturity-periood(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const anticipatedMaturityPerioodSample = {
    anticipatedMaturityTenorCode: 'Borders Dakota Avon',
    aniticipatedMaturityTenorType: 'interface Forward Arizona',
  };

  let anticipatedMaturityPeriood: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/anticipated-maturity-perioods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/anticipated-maturity-perioods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/anticipated-maturity-perioods/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (anticipatedMaturityPeriood) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/anticipated-maturity-perioods/${anticipatedMaturityPeriood.id}`,
      }).then(() => {
        anticipatedMaturityPeriood = undefined;
      });
    }
  });

  it('AnticipatedMaturityPerioods menu should load AnticipatedMaturityPerioods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('anticipated-maturity-periood');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnticipatedMaturityPeriood').should('exist');
    cy.url().should('match', anticipatedMaturityPerioodPageUrlPattern);
  });

  describe('AnticipatedMaturityPeriood page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(anticipatedMaturityPerioodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AnticipatedMaturityPeriood page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/anticipated-maturity-periood/new$'));
        cy.getEntityCreateUpdateHeading('AnticipatedMaturityPeriood');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', anticipatedMaturityPerioodPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/anticipated-maturity-perioods',
          body: anticipatedMaturityPerioodSample,
        }).then(({ body }) => {
          anticipatedMaturityPeriood = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/anticipated-maturity-perioods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [anticipatedMaturityPeriood],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(anticipatedMaturityPerioodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AnticipatedMaturityPeriood page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('anticipatedMaturityPeriood');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', anticipatedMaturityPerioodPageUrlPattern);
      });

      it('edit button click should load edit AnticipatedMaturityPeriood page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnticipatedMaturityPeriood');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', anticipatedMaturityPerioodPageUrlPattern);
      });

      it('last delete button click should delete instance of AnticipatedMaturityPeriood', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('anticipatedMaturityPeriood').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', anticipatedMaturityPerioodPageUrlPattern);

        anticipatedMaturityPeriood = undefined;
      });
    });
  });

  describe('new AnticipatedMaturityPeriood page', () => {
    beforeEach(() => {
      cy.visit(`${anticipatedMaturityPerioodPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AnticipatedMaturityPeriood');
    });

    it('should create an instance of AnticipatedMaturityPeriood', () => {
      cy.get(`[data-cy="anticipatedMaturityTenorCode"]`).type('Virgin Jewelery').should('have.value', 'Virgin Jewelery');

      cy.get(`[data-cy="aniticipatedMaturityTenorType"]`).type('fresh-thinking').should('have.value', 'fresh-thinking');

      cy.get(`[data-cy="anticipatedMaturityTenorDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        anticipatedMaturityPeriood = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', anticipatedMaturityPerioodPageUrlPattern);
    });
  });
});
