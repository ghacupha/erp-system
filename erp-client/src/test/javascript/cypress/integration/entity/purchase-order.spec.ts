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

describe('PurchaseOrder e2e test', () => {
  const purchaseOrderPageUrl = '/purchase-order';
  const purchaseOrderPageUrlPattern = new RegExp('/purchase-order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const purchaseOrderSample = { purchaseOrderNumber: 'Shirt' };

  let purchaseOrder: any;
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
        dealerName: 'unleash',
        taxNumber: 'Facilitator Representative',
        identificationDocumentNumber: 'circuit Regional Cambridgeshire',
        organizationName: 'multi-byte networks',
        department: 'Security',
        position: 'mobile compress B2B',
        postalAddress: 'calculate software challenge',
        physicalAddress: 'Soap Accountability maroon',
        accountName: 'Investment Account',
        accountNumber: 'Ranch Ergonomic',
        bankersName: 'XML programming',
        bankersBranch: 'Analyst Buckinghamshire Chips',
        bankersSwiftCode: 'orchestration 1080p efficient',
        fileUploadToken: 'National navigating flexibility',
        compilationToken: 'cross-platform',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'SDD haptic',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/purchase-orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchase-orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchase-orders/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
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
    if (purchaseOrder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchase-orders/${purchaseOrder.id}`,
      }).then(() => {
        purchaseOrder = undefined;
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

  it('PurchaseOrders menu should load PurchaseOrders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchase-order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchaseOrder').should('exist');
    cy.url().should('match', purchaseOrderPageUrlPattern);
  });

  describe('PurchaseOrder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchaseOrderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchaseOrder page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/purchase-order/new$'));
        cy.getEntityCreateUpdateHeading('PurchaseOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', purchaseOrderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchase-orders',

          body: {
            ...purchaseOrderSample,
            vendor: dealer,
          },
        }).then(({ body }) => {
          purchaseOrder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchase-orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [purchaseOrder],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchaseOrderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PurchaseOrder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchaseOrder');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', purchaseOrderPageUrlPattern);
      });

      it('edit button click should load edit PurchaseOrder page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchaseOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', purchaseOrderPageUrlPattern);
      });

      it('last delete button click should delete instance of PurchaseOrder', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('purchaseOrder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', purchaseOrderPageUrlPattern);

        purchaseOrder = undefined;
      });
    });
  });

  describe('new PurchaseOrder page', () => {
    beforeEach(() => {
      cy.visit(`${purchaseOrderPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PurchaseOrder');
    });

    it('should create an instance of PurchaseOrder', () => {
      cy.get(`[data-cy="purchaseOrderNumber"]`).type('Mountain Tasty').should('have.value', 'Mountain Tasty');

      cy.get(`[data-cy="purchaseOrderDate"]`).type('2022-02-03').should('have.value', '2022-02-03');

      cy.get(`[data-cy="purchaseOrderAmount"]`).type('50872').should('have.value', '50872');

      cy.get(`[data-cy="description"]`).type('Soft redundant Assurance').should('have.value', 'Soft redundant Assurance');

      cy.get(`[data-cy="notes"]`).type('Home relationships vortals').should('have.value', 'Home relationships vortals');

      cy.get(`[data-cy="fileUploadToken"]`).type('Sharable optimal silver').should('have.value', 'Sharable optimal silver');

      cy.get(`[data-cy="compilationToken"]`).type('proactive Finland').should('have.value', 'proactive Finland');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="vendor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        purchaseOrder = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', purchaseOrderPageUrlPattern);
    });
  });
});
