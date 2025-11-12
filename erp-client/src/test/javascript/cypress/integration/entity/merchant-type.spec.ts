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

describe('MerchantType e2e test', () => {
  const merchantTypePageUrl = '/merchant-type';
  const merchantTypePageUrlPattern = new RegExp('/merchant-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const merchantTypeSample = { merchantTypeCode: 'Loaf deposit', merchantType: 'California neural Beauty' };

  let merchantType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/merchant-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/merchant-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/merchant-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (merchantType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/merchant-types/${merchantType.id}`,
      }).then(() => {
        merchantType = undefined;
      });
    }
  });

  it('MerchantTypes menu should load MerchantTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('merchant-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MerchantType').should('exist');
    cy.url().should('match', merchantTypePageUrlPattern);
  });

  describe('MerchantType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(merchantTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MerchantType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/merchant-type/new$'));
        cy.getEntityCreateUpdateHeading('MerchantType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', merchantTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/merchant-types',
          body: merchantTypeSample,
        }).then(({ body }) => {
          merchantType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/merchant-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [merchantType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(merchantTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MerchantType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('merchantType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', merchantTypePageUrlPattern);
      });

      it('edit button click should load edit MerchantType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MerchantType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', merchantTypePageUrlPattern);
      });

      it('last delete button click should delete instance of MerchantType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('merchantType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', merchantTypePageUrlPattern);

        merchantType = undefined;
      });
    });
  });

  describe('new MerchantType page', () => {
    beforeEach(() => {
      cy.visit(`${merchantTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('MerchantType');
    });

    it('should create an instance of MerchantType', () => {
      cy.get(`[data-cy="merchantTypeCode"]`).type('Cambridgeshire Fields Colorado').should('have.value', 'Cambridgeshire Fields Colorado');

      cy.get(`[data-cy="merchantType"]`).type('Chips blue').should('have.value', 'Chips blue');

      cy.get(`[data-cy="merchantTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        merchantType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', merchantTypePageUrlPattern);
    });
  });
});
