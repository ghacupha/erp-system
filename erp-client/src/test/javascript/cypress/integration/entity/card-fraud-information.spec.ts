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

describe('CardFraudInformation e2e test', () => {
  const cardFraudInformationPageUrl = '/card-fraud-information';
  const cardFraudInformationPageUrlPattern = new RegExp('/card-fraud-information(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardFraudInformationSample = { reportingDate: '2023-10-03', totalNumberOfFraudIncidents: 49233, valueOfFraudIncedentsInLCY: 90971 };

  let cardFraudInformation: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-fraud-informations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-fraud-informations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-fraud-informations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cardFraudInformation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-fraud-informations/${cardFraudInformation.id}`,
      }).then(() => {
        cardFraudInformation = undefined;
      });
    }
  });

  it('CardFraudInformations menu should load CardFraudInformations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-fraud-information');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardFraudInformation').should('exist');
    cy.url().should('match', cardFraudInformationPageUrlPattern);
  });

  describe('CardFraudInformation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardFraudInformationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardFraudInformation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-fraud-information/new$'));
        cy.getEntityCreateUpdateHeading('CardFraudInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudInformationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-fraud-informations',
          body: cardFraudInformationSample,
        }).then(({ body }) => {
          cardFraudInformation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-fraud-informations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardFraudInformation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardFraudInformationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardFraudInformation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardFraudInformation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudInformationPageUrlPattern);
      });

      it('edit button click should load edit CardFraudInformation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardFraudInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudInformationPageUrlPattern);
      });

      it('last delete button click should delete instance of CardFraudInformation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardFraudInformation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardFraudInformationPageUrlPattern);

        cardFraudInformation = undefined;
      });
    });
  });

  describe('new CardFraudInformation page', () => {
    beforeEach(() => {
      cy.visit(`${cardFraudInformationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardFraudInformation');
    });

    it('should create an instance of CardFraudInformation', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="totalNumberOfFraudIncidents"]`).type('14210').should('have.value', '14210');

      cy.get(`[data-cy="valueOfFraudIncedentsInLCY"]`).type('84288').should('have.value', '84288');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardFraudInformation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardFraudInformationPageUrlPattern);
    });
  });
});
