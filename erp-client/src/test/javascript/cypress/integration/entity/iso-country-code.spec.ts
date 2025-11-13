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

describe('IsoCountryCode e2e test', () => {
  const isoCountryCodePageUrl = '/iso-country-code';
  const isoCountryCodePageUrlPattern = new RegExp('/iso-country-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const isoCountryCodeSample = {};

  let isoCountryCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/iso-country-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/iso-country-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/iso-country-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (isoCountryCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/iso-country-codes/${isoCountryCode.id}`,
      }).then(() => {
        isoCountryCode = undefined;
      });
    }
  });

  it('IsoCountryCodes menu should load IsoCountryCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('iso-country-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IsoCountryCode').should('exist');
    cy.url().should('match', isoCountryCodePageUrlPattern);
  });

  describe('IsoCountryCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(isoCountryCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IsoCountryCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/iso-country-code/new$'));
        cy.getEntityCreateUpdateHeading('IsoCountryCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCountryCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/iso-country-codes',
          body: isoCountryCodeSample,
        }).then(({ body }) => {
          isoCountryCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/iso-country-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [isoCountryCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(isoCountryCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IsoCountryCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('isoCountryCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCountryCodePageUrlPattern);
      });

      it('edit button click should load edit IsoCountryCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IsoCountryCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCountryCodePageUrlPattern);
      });

      it('last delete button click should delete instance of IsoCountryCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('isoCountryCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCountryCodePageUrlPattern);

        isoCountryCode = undefined;
      });
    });
  });

  describe('new IsoCountryCode page', () => {
    beforeEach(() => {
      cy.visit(`${isoCountryCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IsoCountryCode');
    });

    it('should create an instance of IsoCountryCode', () => {
      cy.get(`[data-cy="countryCode"]`).type('NL').should('have.value', 'NL');

      cy.get(`[data-cy="countryDescription"]`).type('Specialist connect Dynamic').should('have.value', 'Specialist connect Dynamic');

      cy.get(`[data-cy="continentCode"]`).type('Lead').should('have.value', 'Lead');

      cy.get(`[data-cy="continentName"]`).type('Health THX Frozen').should('have.value', 'Health THX Frozen');

      cy.get(`[data-cy="subRegion"]`).type('Music Bedfordshire Market').should('have.value', 'Music Bedfordshire Market');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        isoCountryCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', isoCountryCodePageUrlPattern);
    });
  });
});
