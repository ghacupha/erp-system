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

describe('AccountAttributeMetadata e2e test', () => {
  const accountAttributeMetadataPageUrl = '/account-attribute-metadata';
  const accountAttributeMetadataPageUrlPattern = new RegExp('/account-attribute-metadata(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const accountAttributeMetadataSample = {
    precedence: 22428,
    columnName: 'parse Music olive',
    shortName: 'cyan',
    dataType: 'Somali synthesizing',
    mandatoryFieldFlag: 'N',
  };

  let accountAttributeMetadata: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-attribute-metadata+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-attribute-metadata').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-attribute-metadata/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountAttributeMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-attribute-metadata/${accountAttributeMetadata.id}`,
      }).then(() => {
        accountAttributeMetadata = undefined;
      });
    }
  });

  it('AccountAttributeMetadata menu should load AccountAttributeMetadata page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-attribute-metadata');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountAttributeMetadata').should('exist');
    cy.url().should('match', accountAttributeMetadataPageUrlPattern);
  });

  describe('AccountAttributeMetadata page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountAttributeMetadataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountAttributeMetadata page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/account-attribute-metadata/new$'));
        cy.getEntityCreateUpdateHeading('AccountAttributeMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributeMetadataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/account-attribute-metadata',
          body: accountAttributeMetadataSample,
        }).then(({ body }) => {
          accountAttributeMetadata = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/account-attribute-metadata+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accountAttributeMetadata],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountAttributeMetadataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountAttributeMetadata page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountAttributeMetadata');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributeMetadataPageUrlPattern);
      });

      it('edit button click should load edit AccountAttributeMetadata page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountAttributeMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributeMetadataPageUrlPattern);
      });

      it('last delete button click should delete instance of AccountAttributeMetadata', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountAttributeMetadata').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountAttributeMetadataPageUrlPattern);

        accountAttributeMetadata = undefined;
      });
    });
  });

  describe('new AccountAttributeMetadata page', () => {
    beforeEach(() => {
      cy.visit(`${accountAttributeMetadataPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AccountAttributeMetadata');
    });

    it('should create an instance of AccountAttributeMetadata', () => {
      cy.get(`[data-cy="precedence"]`).type('61668').should('have.value', '61668');

      cy.get(`[data-cy="columnName"]`).type('Islands payment Tools').should('have.value', 'Islands payment Tools');

      cy.get(`[data-cy="shortName"]`).type('Security Games').should('have.value', 'Security Games');

      cy.get(`[data-cy="detailedDefinition"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="dataType"]`).type('deposit').should('have.value', 'deposit');

      cy.get(`[data-cy="length"]`).type('32739').should('have.value', '32739');

      cy.get(`[data-cy="columnIndex"]`).type('bypassing').should('have.value', 'bypassing');

      cy.get(`[data-cy="mandatoryFieldFlag"]`).select('Y');

      cy.get(`[data-cy="businessValidation"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="technicalValidation"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="dbColumnName"]`).type('(Vatican withdrawal interactive').should('have.value', '(Vatican withdrawal interactive');

      cy.get(`[data-cy="metadataVersion"]`).type('99095').should('have.value', '99095');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        accountAttributeMetadata = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', accountAttributeMetadataPageUrlPattern);
    });
  });
});
