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

describe('AssetWarranty e2e test', () => {
  const assetWarrantyPageUrl = '/asset-warranty';
  const assetWarrantyPageUrlPattern = new RegExp('/asset-warranty(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetWarrantySample = {};

  let assetWarranty: any;
  let dealer: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {
        dealerName: 'States Berkshire Slovakia',
        taxNumber: 'analyzing deposit',
        identificationDocumentNumber: 'Producer',
        organizationName: 'withdrawal Rustic Specialist',
        department: 'Sports',
        position: 'Loan',
        postalAddress: 'Sausages override Concrete',
        physicalAddress: 'array Tools',
        accountName: 'Credit Card Account',
        accountNumber: 'paradigm',
        bankersName: 'Card Officer',
        bankersBranch: 'Prairie',
        bankersSwiftCode: 'panel Towels Bike',
        fileUploadToken: 'Buckinghamshire Handcrafted Wooden',
        compilationToken: 'Bedfordshire',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'Awesome JBOD salmon',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-warranties+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-warranties').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-warranties/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (assetWarranty) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-warranties/${assetWarranty.id}`,
      }).then(() => {
        assetWarranty = undefined;
      });
    }
  });

  afterEach(() => {
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });

  it('AssetWarranties menu should load AssetWarranties page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-warranty');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetWarranty').should('exist');
    cy.url().should('match', assetWarrantyPageUrlPattern);
  });

  describe('AssetWarranty page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetWarrantyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetWarranty page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-warranty/new$'));
        cy.getEntityCreateUpdateHeading('AssetWarranty');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWarrantyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-warranties',

          body: {
            ...assetWarrantySample,
            dealer: dealer,
          },
        }).then(({ body }) => {
          assetWarranty = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-warranties+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetWarranty],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetWarrantyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetWarranty page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetWarranty');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWarrantyPageUrlPattern);
      });

      it('edit button click should load edit AssetWarranty page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetWarranty');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWarrantyPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetWarranty', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetWarranty').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWarrantyPageUrlPattern);

        assetWarranty = undefined;
      });
    });
  });

  describe('new AssetWarranty page', () => {
    beforeEach(() => {
      cy.visit(`${assetWarrantyPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetWarranty');
    });

    it('should create an instance of AssetWarranty', () => {
      cy.get(`[data-cy="assetTag"]`).type('benchmark').should('have.value', 'benchmark');

      cy.get(`[data-cy="description"]`).type('Generic Niger').should('have.value', 'Generic Niger');

      cy.get(`[data-cy="modelNumber"]`).type('Georgia Plastic Personal').should('have.value', 'Georgia Plastic Personal');

      cy.get(`[data-cy="serialNumber"]`).type('Radial Intelligent').should('have.value', 'Radial Intelligent');

      cy.get(`[data-cy="expiryDate"]`).type('2023-05-04').should('have.value', '2023-05-04');

      cy.get(`[data-cy="dealer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetWarranty = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetWarrantyPageUrlPattern);
    });
  });
});
