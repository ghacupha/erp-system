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

describe('PartyRelationType e2e test', () => {
  const partyRelationTypePageUrl = '/party-relation-type';
  const partyRelationTypePageUrlPattern = new RegExp('/party-relation-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const partyRelationTypeSample = { partyRelationTypeCode: 'bandwidth-monitored', partyRelationType: 'Tools hub' };

  let partyRelationType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-relation-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-relation-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-relation-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (partyRelationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-relation-types/${partyRelationType.id}`,
      }).then(() => {
        partyRelationType = undefined;
      });
    }
  });

  it('PartyRelationTypes menu should load PartyRelationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-relation-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyRelationType').should('exist');
    cy.url().should('match', partyRelationTypePageUrlPattern);
  });

  describe('PartyRelationType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyRelationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyRelationType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/party-relation-type/new$'));
        cy.getEntityCreateUpdateHeading('PartyRelationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', partyRelationTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-relation-types',
          body: partyRelationTypeSample,
        }).then(({ body }) => {
          partyRelationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-relation-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [partyRelationType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyRelationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyRelationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyRelationType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', partyRelationTypePageUrlPattern);
      });

      it('edit button click should load edit PartyRelationType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRelationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', partyRelationTypePageUrlPattern);
      });

      it('last delete button click should delete instance of PartyRelationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyRelationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', partyRelationTypePageUrlPattern);

        partyRelationType = undefined;
      });
    });
  });

  describe('new PartyRelationType page', () => {
    beforeEach(() => {
      cy.visit(`${partyRelationTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PartyRelationType');
    });

    it('should create an instance of PartyRelationType', () => {
      cy.get(`[data-cy="partyRelationTypeCode"]`).type('hack knowledge').should('have.value', 'hack knowledge');

      cy.get(`[data-cy="partyRelationType"]`).type('withdrawal').should('have.value', 'withdrawal');

      cy.get(`[data-cy="partyRelationTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        partyRelationType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', partyRelationTypePageUrlPattern);
    });
  });
});
