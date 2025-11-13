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

describe('DeliveryNote e2e test', () => {
  const deliveryNotePageUrl = '/delivery-note';
  const deliveryNotePageUrlPattern = new RegExp('/delivery-note(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const deliveryNoteSample = { deliveryNoteNumber: 'Chair Open-source', documentDate: '2022-03-01' };

  let deliveryNote: any;
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
        dealerName: 'Moldovan',
        taxNumber: 'Games help-desk Metal',
        identificationDocumentNumber: 'gold Handcrafted Lebanon',
        organizationName: 'Analyst',
        department: 'B2B parse backing',
        position: '1080p',
        postalAddress: 'Zambia redefine',
        physicalAddress: 'experiences Frozen sensor',
        accountName: 'Money Market Account',
        accountNumber: 'Music Concrete',
        bankersName: 'solution brand dynamic',
        bankersBranch: 'Gateway Handmade Account',
        bankersSwiftCode: 'Slovenia Avon',
        fileUploadToken: 'and Gabon',
        compilationToken: '4th Lakes Litas',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'Card',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/delivery-notes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/delivery-notes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/delivery-notes/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/business-stamps', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/purchase-orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (deliveryNote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/delivery-notes/${deliveryNote.id}`,
      }).then(() => {
        deliveryNote = undefined;
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

  it('DeliveryNotes menu should load DeliveryNotes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('delivery-note');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DeliveryNote').should('exist');
    cy.url().should('match', deliveryNotePageUrlPattern);
  });

  describe('DeliveryNote page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(deliveryNotePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DeliveryNote page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/delivery-note/new$'));
        cy.getEntityCreateUpdateHeading('DeliveryNote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryNotePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/delivery-notes',

          body: {
            ...deliveryNoteSample,
            receivedBy: dealer,
            supplier: dealer,
          },
        }).then(({ body }) => {
          deliveryNote = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/delivery-notes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [deliveryNote],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(deliveryNotePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DeliveryNote page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deliveryNote');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryNotePageUrlPattern);
      });

      it('edit button click should load edit DeliveryNote page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DeliveryNote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryNotePageUrlPattern);
      });

      it('last delete button click should delete instance of DeliveryNote', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('deliveryNote').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryNotePageUrlPattern);

        deliveryNote = undefined;
      });
    });
  });

  describe('new DeliveryNote page', () => {
    beforeEach(() => {
      cy.visit(`${deliveryNotePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DeliveryNote');
    });

    it('should create an instance of DeliveryNote', () => {
      cy.get(`[data-cy="deliveryNoteNumber"]`).type('hard').should('have.value', 'hard');

      cy.get(`[data-cy="documentDate"]`).type('2022-03-02').should('have.value', '2022-03-02');

      cy.get(`[data-cy="description"]`).type('mission-critical salmon').should('have.value', 'mission-critical salmon');

      cy.get(`[data-cy="serialNumber"]`).type('Personal up Markets').should('have.value', 'Personal up Markets');

      cy.get(`[data-cy="quantity"]`).type('3912').should('have.value', '3912');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="receivedBy"]`).select(1);
      cy.get(`[data-cy="supplier"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        deliveryNote = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', deliveryNotePageUrlPattern);
    });
  });
});
