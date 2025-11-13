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

describe('ProfessionalQualification e2e test', () => {
  const professionalQualificationPageUrl = '/professional-qualification';
  const professionalQualificationPageUrlPattern = new RegExp('/professional-qualification(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const professionalQualificationSample = {
    professionalQualificationsCode: 'Up-sized Orchestrator Metal',
    professionalQualificationsType: 'Soft',
  };

  let professionalQualification: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/professional-qualifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/professional-qualifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/professional-qualifications/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (professionalQualification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/professional-qualifications/${professionalQualification.id}`,
      }).then(() => {
        professionalQualification = undefined;
      });
    }
  });

  it('ProfessionalQualifications menu should load ProfessionalQualifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('professional-qualification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProfessionalQualification').should('exist');
    cy.url().should('match', professionalQualificationPageUrlPattern);
  });

  describe('ProfessionalQualification page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(professionalQualificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProfessionalQualification page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/professional-qualification/new$'));
        cy.getEntityCreateUpdateHeading('ProfessionalQualification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', professionalQualificationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/professional-qualifications',
          body: professionalQualificationSample,
        }).then(({ body }) => {
          professionalQualification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/professional-qualifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [professionalQualification],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(professionalQualificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProfessionalQualification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('professionalQualification');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', professionalQualificationPageUrlPattern);
      });

      it('edit button click should load edit ProfessionalQualification page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProfessionalQualification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', professionalQualificationPageUrlPattern);
      });

      it('last delete button click should delete instance of ProfessionalQualification', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('professionalQualification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', professionalQualificationPageUrlPattern);

        professionalQualification = undefined;
      });
    });
  });

  describe('new ProfessionalQualification page', () => {
    beforeEach(() => {
      cy.visit(`${professionalQualificationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ProfessionalQualification');
    });

    it('should create an instance of ProfessionalQualification', () => {
      cy.get(`[data-cy="professionalQualificationsCode"]`).type('turquoise Paradigm').should('have.value', 'turquoise Paradigm');

      cy.get(`[data-cy="professionalQualificationsType"]`).type('AGP Plastic turn-key').should('have.value', 'AGP Plastic turn-key');

      cy.get(`[data-cy="professionalQualificationsDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        professionalQualification = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', professionalQualificationPageUrlPattern);
    });
  });
});
