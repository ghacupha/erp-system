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

describe('BusinessStamp e2e test', () => {
  const businessStampPageUrl = '/business-stamp';
  const businessStampPageUrlPattern = new RegExp('/business-stamp(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const businessStampSample = {};

  let businessStamp: any;
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
        dealerName: 'Wooden Legacy Alabama',
        taxNumber: 'demand-driven',
        identificationDocumentNumber: 'Market',
        organizationName: '1080p',
        department: 'Tactics Plaza Granite',
        position: 'port HTTP Account',
        postalAddress: 'Union synthesize Division',
        physicalAddress: 'France hacking Plastic',
        accountName: 'Investment Account',
        accountNumber: 'Sleek',
        bankersName: 'Small Forward',
        bankersBranch: 'Ergonomic methodologies backing',
        bankersSwiftCode: 'quantify Function-based',
        fileUploadToken: 'quantifying',
        compilationToken: 'Oregon quantifying',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'card Handcrafted implement',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/business-stamps+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/business-stamps').as('postEntityRequest');
    cy.intercept('DELETE', '/api/business-stamps/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (businessStamp) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-stamps/${businessStamp.id}`,
      }).then(() => {
        businessStamp = undefined;
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

  it('BusinessStamps menu should load BusinessStamps page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('business-stamp');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BusinessStamp').should('exist');
    cy.url().should('match', businessStampPageUrlPattern);
  });

  describe('BusinessStamp page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(businessStampPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BusinessStamp page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/business-stamp/new$'));
        cy.getEntityCreateUpdateHeading('BusinessStamp');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessStampPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/business-stamps',

          body: {
            ...businessStampSample,
            stampHolder: dealer,
          },
        }).then(({ body }) => {
          businessStamp = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/business-stamps+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [businessStamp],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(businessStampPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BusinessStamp page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('businessStamp');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessStampPageUrlPattern);
      });

      it('edit button click should load edit BusinessStamp page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BusinessStamp');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessStampPageUrlPattern);
      });

      it('last delete button click should delete instance of BusinessStamp', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('businessStamp').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessStampPageUrlPattern);

        businessStamp = undefined;
      });
    });
  });

  describe('new BusinessStamp page', () => {
    beforeEach(() => {
      cy.visit(`${businessStampPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('BusinessStamp');
    });

    it('should create an instance of BusinessStamp', () => {
      cy.get(`[data-cy="stampDate"]`).type('2022-03-02').should('have.value', '2022-03-02');

      cy.get(`[data-cy="purpose"]`).type('1080p Taiwan').should('have.value', '1080p Taiwan');

      cy.get(`[data-cy="details"]`).type('red').should('have.value', 'red');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="stampHolder"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        businessStamp = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', businessStampPageUrlPattern);
    });
  });
});
