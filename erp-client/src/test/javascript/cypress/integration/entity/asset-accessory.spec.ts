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

describe('AssetAccessory e2e test', () => {
  const assetAccessoryPageUrl = '/asset-accessory';
  const assetAccessoryPageUrlPattern = new RegExp('/asset-accessory(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetAccessorySample = {};

  let assetAccessory: any;
  //let serviceOutlet: any;
  //let settlement: any;
  //let assetCategory: any;
  //let dealer: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/service-outlets',
      body: {"outletCode":"productivity","outletName":"Reverse-engineered","town":"users Mali Account","parliamentaryConstituency":"Checking Salad","gpsCoordinates":"Unbranded Towels Electronics","outletOpeningDate":"2022-03-01","regulatorApprovalDate":"2022-03-01","outletClosureDate":"2022-02-28","dateLastModified":"2022-02-28","licenseFeePayable":75439},
    }).then(({ body }) => {
      serviceOutlet = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/settlements',
      body: {"paymentNumber":"encoding Home program","paymentDate":"2022-02-02","paymentAmount":4770,"description":"generating relationships","notes":"bandwidth","calculationFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","calculationFileContentType":"unknown","fileUploadToken":"Synergistic","compilationToken":"withdrawal Account","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="},
    }).then(({ body }) => {
      settlement = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-categories',
      body: {"assetCategoryName":"copying throughput Bolivia","description":"Euro","notes":"Switchable Pines","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","depreciationRateYearly":26606},
    }).then(({ body }) => {
      assetCategory = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"Extended","taxNumber":"generating Computers engineer","identificationDocumentNumber":"indexing Shoes Human","organizationName":"Mandatory Fantastic Soft","department":"invoice back Supervisor","position":"Keyboard","postalAddress":"Generic e-tailers Buckinghamshire","physicalAddress":"compress","accountName":"Personal Loan Account","accountNumber":"Algerian Glen Dynamic","bankersName":"Director Baby","bankersBranch":"Director synthesize","bankersSwiftCode":"Maryland end-to-end Fish","fileUploadToken":"bandwidth Interactions Generic","compilationToken":"Interactions","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"flexibility Berkshire"},
    }).then(({ body }) => {
      dealer = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-accessories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-accessories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-accessories/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/asset-warranties', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/service-outlets', {
      statusCode: 200,
      body: [serviceOutlet],
    });

    cy.intercept('GET', '/api/settlements', {
      statusCode: 200,
      body: [settlement],
    });

    cy.intercept('GET', '/api/asset-categories', {
      statusCode: 200,
      body: [assetCategory],
    });

    cy.intercept('GET', '/api/purchase-orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/delivery-notes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/job-sheets', {
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

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (assetAccessory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-accessories/${assetAccessory.id}`,
      }).then(() => {
        assetAccessory = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (serviceOutlet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-outlets/${serviceOutlet.id}`,
      }).then(() => {
        serviceOutlet = undefined;
      });
    }
    if (settlement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlements/${settlement.id}`,
      }).then(() => {
        settlement = undefined;
      });
    }
    if (assetCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-categories/${assetCategory.id}`,
      }).then(() => {
        assetCategory = undefined;
      });
    }
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });
   */

  it('AssetAccessories menu should load AssetAccessories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-accessory');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetAccessory').should('exist');
    cy.url().should('match', assetAccessoryPageUrlPattern);
  });

  describe('AssetAccessory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetAccessoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetAccessory page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-accessory/new$'));
        cy.getEntityCreateUpdateHeading('AssetAccessory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAccessoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-accessories',
  
          body: {
            ...assetAccessorySample,
            serviceOutlet: serviceOutlet,
            settlement: settlement,
            assetCategory: assetCategory,
            dealer: dealer,
          },
        }).then(({ body }) => {
          assetAccessory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-accessories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetAccessory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetAccessoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetAccessoryPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetAccessory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetAccessory');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAccessoryPageUrlPattern);
      });

      it('edit button click should load edit AssetAccessory page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetAccessory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAccessoryPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetAccessory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetAccessory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAccessoryPageUrlPattern);

        assetAccessory = undefined;
      });
    });
  });

  describe('new AssetAccessory page', () => {
    beforeEach(() => {
      cy.visit(`${assetAccessoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetAccessory');
    });

    it.skip('should create an instance of AssetAccessory', () => {
      cy.get(`[data-cy="assetTag"]`).type('explicit deposit attitude').should('have.value', 'explicit deposit attitude');

      cy.get(`[data-cy="assetDetails"]`).type('Buckinghamshire').should('have.value', 'Buckinghamshire');

      cy.setFieldImageAsBytesOfEntity('comments', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="modelNumber"]`).type('SMTP').should('have.value', 'SMTP');

      cy.get(`[data-cy="serialNumber"]`).type('Cambridgeshire').should('have.value', 'Cambridgeshire');

      cy.get(`[data-cy="serviceOutlet"]`).select(1);
      cy.get(`[data-cy="settlement"]`).select([0]);
      cy.get(`[data-cy="assetCategory"]`).select(1);
      cy.get(`[data-cy="dealer"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetAccessory = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetAccessoryPageUrlPattern);
    });
  });
});
