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

describe('TaxReference e2e test', () => {
  const taxReferencePageUrl = '/tax-reference';
  const taxReferencePageUrlPattern = new RegExp('/tax-reference(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const taxReferenceSample = { taxPercentage: 46356, taxReferenceType: 'SERVICE_CHARGE' };

  let taxReference: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tax-references+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tax-references').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tax-references/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxReference) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tax-references/${taxReference.id}`,
      }).then(() => {
        taxReference = undefined;
      });
    }
  });

  it('TaxReferences menu should load TaxReferences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tax-reference');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxReference').should('exist');
    cy.url().should('match', taxReferencePageUrlPattern);
  });

  describe('TaxReference page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxReferencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxReference page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/tax-reference/new$'));
        cy.getEntityCreateUpdateHeading('TaxReference');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxReferencePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tax-references',
          body: taxReferenceSample,
        }).then(({ body }) => {
          taxReference = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tax-references+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [taxReference],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxReferencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxReference page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxReference');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxReferencePageUrlPattern);
      });

      it('edit button click should load edit TaxReference page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxReference');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxReferencePageUrlPattern);
      });

      it('last delete button click should delete instance of TaxReference', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taxReference').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', taxReferencePageUrlPattern);

        taxReference = undefined;
      });
    });
  });

  describe('new TaxReference page', () => {
    beforeEach(() => {
      cy.visit(`${taxReferencePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TaxReference');
    });

    it('should create an instance of TaxReference', () => {
      cy.get(`[data-cy="taxName"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="taxDescription"]`).type('calculate web-enabled').should('have.value', 'calculate web-enabled');

      cy.get(`[data-cy="taxPercentage"]`).type('32039').should('have.value', '32039');

      cy.get(`[data-cy="taxReferenceType"]`).select('TELCO_EXCISE_DUTY');

      cy.get(`[data-cy="fileUploadToken"]`)
        .type('context-sensitive firewall Sports')
        .should('have.value', 'context-sensitive firewall Sports');

      cy.get(`[data-cy="compilationToken"]`).type('Liaison Berkshire systems').should('have.value', 'Liaison Berkshire systems');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        taxReference = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', taxReferencePageUrlPattern);
    });
  });
});
