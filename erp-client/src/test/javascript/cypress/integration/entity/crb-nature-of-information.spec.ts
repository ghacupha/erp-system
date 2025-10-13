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

describe('CrbNatureOfInformation e2e test', () => {
  const crbNatureOfInformationPageUrl = '/crb-nature-of-information';
  const crbNatureOfInformationPageUrlPattern = new RegExp('/crb-nature-of-information(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbNatureOfInformationSample = {
    natureOfInformationTypeCode: 'visualize Buckinghamshire IB',
    natureOfInformationType: 'New withdrawal toolset',
  };

  let crbNatureOfInformation: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-nature-of-informations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-nature-of-informations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-nature-of-informations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbNatureOfInformation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-nature-of-informations/${crbNatureOfInformation.id}`,
      }).then(() => {
        crbNatureOfInformation = undefined;
      });
    }
  });

  it('CrbNatureOfInformations menu should load CrbNatureOfInformations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-nature-of-information');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbNatureOfInformation').should('exist');
    cy.url().should('match', crbNatureOfInformationPageUrlPattern);
  });

  describe('CrbNatureOfInformation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbNatureOfInformationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbNatureOfInformation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-nature-of-information/new$'));
        cy.getEntityCreateUpdateHeading('CrbNatureOfInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbNatureOfInformationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-nature-of-informations',
          body: crbNatureOfInformationSample,
        }).then(({ body }) => {
          crbNatureOfInformation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-nature-of-informations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbNatureOfInformation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbNatureOfInformationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbNatureOfInformation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbNatureOfInformation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbNatureOfInformationPageUrlPattern);
      });

      it('edit button click should load edit CrbNatureOfInformation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbNatureOfInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbNatureOfInformationPageUrlPattern);
      });

      it('last delete button click should delete instance of CrbNatureOfInformation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbNatureOfInformation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbNatureOfInformationPageUrlPattern);

        crbNatureOfInformation = undefined;
      });
    });
  });

  describe('new CrbNatureOfInformation page', () => {
    beforeEach(() => {
      cy.visit(`${crbNatureOfInformationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbNatureOfInformation');
    });

    it('should create an instance of CrbNatureOfInformation', () => {
      cy.get(`[data-cy="natureOfInformationTypeCode"]`).type('technologies').should('have.value', 'technologies');

      cy.get(`[data-cy="natureOfInformationType"]`).type('Cotton Georgia Extension').should('have.value', 'Cotton Georgia Extension');

      cy.get(`[data-cy="natureOfInformationTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbNatureOfInformation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbNatureOfInformationPageUrlPattern);
    });
  });
});
