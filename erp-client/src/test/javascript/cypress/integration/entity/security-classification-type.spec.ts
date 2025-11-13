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

describe('SecurityClassificationType e2e test', () => {
  const securityClassificationTypePageUrl = '/security-classification-type';
  const securityClassificationTypePageUrlPattern = new RegExp('/security-classification-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const securityClassificationTypeSample = {
    securityClassificationTypeCode: 'Nebraska haptic Islands',
    securityClassificationType: 'Handcrafted Beauty object-oriented',
  };

  let securityClassificationType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/security-classification-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/security-classification-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/security-classification-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (securityClassificationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/security-classification-types/${securityClassificationType.id}`,
      }).then(() => {
        securityClassificationType = undefined;
      });
    }
  });

  it('SecurityClassificationTypes menu should load SecurityClassificationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('security-classification-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SecurityClassificationType').should('exist');
    cy.url().should('match', securityClassificationTypePageUrlPattern);
  });

  describe('SecurityClassificationType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(securityClassificationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SecurityClassificationType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/security-classification-type/new$'));
        cy.getEntityCreateUpdateHeading('SecurityClassificationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClassificationTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/security-classification-types',
          body: securityClassificationTypeSample,
        }).then(({ body }) => {
          securityClassificationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/security-classification-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [securityClassificationType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(securityClassificationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SecurityClassificationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('securityClassificationType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClassificationTypePageUrlPattern);
      });

      it('edit button click should load edit SecurityClassificationType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SecurityClassificationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClassificationTypePageUrlPattern);
      });

      it('last delete button click should delete instance of SecurityClassificationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('securityClassificationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClassificationTypePageUrlPattern);

        securityClassificationType = undefined;
      });
    });
  });

  describe('new SecurityClassificationType page', () => {
    beforeEach(() => {
      cy.visit(`${securityClassificationTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SecurityClassificationType');
    });

    it('should create an instance of SecurityClassificationType', () => {
      cy.get(`[data-cy="securityClassificationTypeCode"]`).type('Alaska').should('have.value', 'Alaska');

      cy.get(`[data-cy="securityClassificationType"]`).type('program Integrated').should('have.value', 'program Integrated');

      cy.get(`[data-cy="securityClassificationDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        securityClassificationType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', securityClassificationTypePageUrlPattern);
    });
  });
});
