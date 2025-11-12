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

describe('SourceRemittancePurposeType e2e test', () => {
  const sourceRemittancePurposeTypePageUrl = '/source-remittance-purpose-type';
  const sourceRemittancePurposeTypePageUrlPattern = new RegExp('/source-remittance-purpose-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const sourceRemittancePurposeTypeSample = {
    sourceOrPurposeTypeCode: 'Plastic',
    sourceOrPurposeOfRemittanceFlag: 'PURPOSE_OF_REMITTANCE',
    sourceOrPurposeOfRemittanceType: 'solution',
  };

  let sourceRemittancePurposeType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/source-remittance-purpose-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/source-remittance-purpose-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/source-remittance-purpose-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sourceRemittancePurposeType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/source-remittance-purpose-types/${sourceRemittancePurposeType.id}`,
      }).then(() => {
        sourceRemittancePurposeType = undefined;
      });
    }
  });

  it('SourceRemittancePurposeTypes menu should load SourceRemittancePurposeTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('source-remittance-purpose-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SourceRemittancePurposeType').should('exist');
    cy.url().should('match', sourceRemittancePurposeTypePageUrlPattern);
  });

  describe('SourceRemittancePurposeType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sourceRemittancePurposeTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SourceRemittancePurposeType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/source-remittance-purpose-type/new$'));
        cy.getEntityCreateUpdateHeading('SourceRemittancePurposeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourceRemittancePurposeTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/source-remittance-purpose-types',
          body: sourceRemittancePurposeTypeSample,
        }).then(({ body }) => {
          sourceRemittancePurposeType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/source-remittance-purpose-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [sourceRemittancePurposeType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(sourceRemittancePurposeTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SourceRemittancePurposeType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sourceRemittancePurposeType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourceRemittancePurposeTypePageUrlPattern);
      });

      it('edit button click should load edit SourceRemittancePurposeType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SourceRemittancePurposeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourceRemittancePurposeTypePageUrlPattern);
      });

      it('last delete button click should delete instance of SourceRemittancePurposeType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sourceRemittancePurposeType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', sourceRemittancePurposeTypePageUrlPattern);

        sourceRemittancePurposeType = undefined;
      });
    });
  });

  describe('new SourceRemittancePurposeType page', () => {
    beforeEach(() => {
      cy.visit(`${sourceRemittancePurposeTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SourceRemittancePurposeType');
    });

    it('should create an instance of SourceRemittancePurposeType', () => {
      cy.get(`[data-cy="sourceOrPurposeTypeCode"]`).type('JBOD ivory Unbranded').should('have.value', 'JBOD ivory Unbranded');

      cy.get(`[data-cy="sourceOrPurposeOfRemittanceFlag"]`).select('PURPOSE_OF_REMITTANCE');

      cy.get(`[data-cy="sourceOrPurposeOfRemittanceType"]`)
        .type('contextually-based hard Shoes')
        .should('have.value', 'contextually-based hard Shoes');

      cy.get(`[data-cy="remittancePurposeTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        sourceRemittancePurposeType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', sourceRemittancePurposeTypePageUrlPattern);
    });
  });
});
