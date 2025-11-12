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

describe('IsicEconomicActivity e2e test', () => {
  const isicEconomicActivityPageUrl = '/isic-economic-activity';
  const isicEconomicActivityPageUrlPattern = new RegExp('/isic-economic-activity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const isicEconomicActivitySample = {
    businessEconomicActivityCode: 'Pants Assistant',
    section: 'Rubber Lead',
    sectionLabel: 'Music copying',
    division: 'iterate',
    divisionLabel: 'salmon',
    groupLabel: 'Forint Automotive Alabama',
    classCode: 'Home',
  };

  let isicEconomicActivity: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/isic-economic-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/isic-economic-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/isic-economic-activities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (isicEconomicActivity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/isic-economic-activities/${isicEconomicActivity.id}`,
      }).then(() => {
        isicEconomicActivity = undefined;
      });
    }
  });

  it('IsicEconomicActivities menu should load IsicEconomicActivities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('isic-economic-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IsicEconomicActivity').should('exist');
    cy.url().should('match', isicEconomicActivityPageUrlPattern);
  });

  describe('IsicEconomicActivity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(isicEconomicActivityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IsicEconomicActivity page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/isic-economic-activity/new$'));
        cy.getEntityCreateUpdateHeading('IsicEconomicActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isicEconomicActivityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/isic-economic-activities',
          body: isicEconomicActivitySample,
        }).then(({ body }) => {
          isicEconomicActivity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/isic-economic-activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [isicEconomicActivity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(isicEconomicActivityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IsicEconomicActivity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('isicEconomicActivity');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isicEconomicActivityPageUrlPattern);
      });

      it('edit button click should load edit IsicEconomicActivity page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IsicEconomicActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isicEconomicActivityPageUrlPattern);
      });

      it('last delete button click should delete instance of IsicEconomicActivity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('isicEconomicActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isicEconomicActivityPageUrlPattern);

        isicEconomicActivity = undefined;
      });
    });
  });

  describe('new IsicEconomicActivity page', () => {
    beforeEach(() => {
      cy.visit(`${isicEconomicActivityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IsicEconomicActivity');
    });

    it('should create an instance of IsicEconomicActivity', () => {
      cy.get(`[data-cy="businessEconomicActivityCode"]`).type('1080p Pants').should('have.value', '1080p Pants');

      cy.get(`[data-cy="section"]`).type('forecast Grocery').should('have.value', 'forecast Grocery');

      cy.get(`[data-cy="sectionLabel"]`).type('maroon').should('have.value', 'maroon');

      cy.get(`[data-cy="division"]`).type('Supervisor infomediaries orange').should('have.value', 'Supervisor infomediaries orange');

      cy.get(`[data-cy="divisionLabel"]`).type('Automotive red Kip').should('have.value', 'Automotive red Kip');

      cy.get(`[data-cy="groupCode"]`).type('didactic').should('have.value', 'didactic');

      cy.get(`[data-cy="groupLabel"]`).type('Card Junction').should('have.value', 'Card Junction');

      cy.get(`[data-cy="classCode"]`).type('transmit Vermont').should('have.value', 'transmit Vermont');

      cy.get(`[data-cy="businessEconomicActivityType"]`).type('Soft Concrete').should('have.value', 'Soft Concrete');

      cy.get(`[data-cy="businessEconomicActivityTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        isicEconomicActivity = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', isicEconomicActivityPageUrlPattern);
    });
  });
});
