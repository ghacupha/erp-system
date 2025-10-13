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

describe('AssetRegistration e2e test', () => {
  const assetRegistrationPageUrl = '/asset-registration';
  const assetRegistrationPageUrlPattern = new RegExp('/asset-registration(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetRegistrationSample = {
    assetNumber: 'compress Kingdom virtual',
    assetTag: 'deposit Aruba',
    assetCost: 61548,
    capitalizationDate: '2022-04-12',
    historicalCost: 86795,
    registrationDate: '2022-04-12',
  };

  let assetRegistration: any;
  //let assetCategory: any;
  //let dealer: any;
  //let settlement: any;

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
      url: '/api/asset-categories',
      body: {"assetCategoryName":"Lead","description":"deposit Decentralized Gloves","notes":"Account","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","depreciationRateYearly":33289},
    }).then(({ body }) => {
      assetCategory = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"pink Parkway definition","taxNumber":"Forges alarm Illinois","identificationDocumentNumber":"CSS Architect program","organizationName":"Visionary Dollar Buckinghamshire","department":"Tuna","position":"Village","postalAddress":"Cambridgeshire","physicalAddress":"Paradigm Bike","accountName":"Investment Account","accountNumber":"Account programming Shoes","bankersName":"Avon","bankersBranch":"invoice","bankersSwiftCode":"Metal Shoes","fileUploadToken":"Fresh backing","compilationToken":"Credit Handmade Outdoors","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"input quantify"},
    }).then(({ body }) => {
      dealer = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/settlements',
      body: {"paymentNumber":"North parsing Bahraini","paymentDate":"2022-02-03","paymentAmount":50422,"description":"blockchains Planner United","notes":"Specialist ivory","calculationFile":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","calculationFileContentType":"unknown","fileUploadToken":"Handmade","compilationToken":"cyan payment","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="},
    }).then(({ body }) => {
      settlement = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-registrations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-registrations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-registrations/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
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
      body: [],
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

    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-warranties', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-accessories', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (assetRegistration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-registrations/${assetRegistration.id}`,
      }).then(() => {
        assetRegistration = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
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
    if (settlement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlements/${settlement.id}`,
      }).then(() => {
        settlement = undefined;
      });
    }
  });
   */

  it('AssetRegistrations menu should load AssetRegistrations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-registration');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetRegistration').should('exist');
    cy.url().should('match', assetRegistrationPageUrlPattern);
  });

  describe('AssetRegistration page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetRegistrationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetRegistration page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-registration/new$'));
        cy.getEntityCreateUpdateHeading('AssetRegistration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegistrationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-registrations',
  
          body: {
            ...assetRegistrationSample,
            assetCategory: assetCategory,
            dealer: dealer,
            acquiringTransaction: settlement,
          },
        }).then(({ body }) => {
          assetRegistration = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-registrations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetRegistration],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetRegistrationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetRegistrationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetRegistration page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetRegistration');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegistrationPageUrlPattern);
      });

      it('edit button click should load edit AssetRegistration page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetRegistration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegistrationPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetRegistration', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetRegistration').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRegistrationPageUrlPattern);

        assetRegistration = undefined;
      });
    });
  });

  describe('new AssetRegistration page', () => {
    beforeEach(() => {
      cy.visit(`${assetRegistrationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetRegistration');
    });

    it.skip('should create an instance of AssetRegistration', () => {
      cy.get(`[data-cy="assetNumber"]`).type('Maine').should('have.value', 'Maine');

      cy.get(`[data-cy="assetTag"]`).type('PNG streamline').should('have.value', 'PNG streamline');

      cy.get(`[data-cy="assetDetails"]`)
        .type('Cambridgeshire Executive Identity')
        .should('have.value', 'Cambridgeshire Executive Identity');

      cy.get(`[data-cy="assetCost"]`).type('97088').should('have.value', '97088');

      cy.setFieldImageAsBytesOfEntity('comments', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="modelNumber"]`).type('B2B').should('have.value', 'B2B');

      cy.get(`[data-cy="serialNumber"]`).type('Latvian').should('have.value', 'Latvian');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="capitalizationDate"]`).type('2022-04-12').should('have.value', '2022-04-12');

      cy.get(`[data-cy="historicalCost"]`).type('98367').should('have.value', '98367');

      cy.get(`[data-cy="registrationDate"]`).type('2022-04-12').should('have.value', '2022-04-12');

      cy.get(`[data-cy="assetCategory"]`).select(1);
      cy.get(`[data-cy="dealer"]`).select(1);
      cy.get(`[data-cy="acquiringTransaction"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetRegistration = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetRegistrationPageUrlPattern);
    });
  });
});
