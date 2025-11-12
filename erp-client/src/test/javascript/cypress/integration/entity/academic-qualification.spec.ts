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

describe('AcademicQualification e2e test', () => {
  const academicQualificationPageUrl = '/academic-qualification';
  const academicQualificationPageUrlPattern = new RegExp('/academic-qualification(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const academicQualificationSample = { academicQualificationsCode: 'Manat', academicQualificationType: 'AI' };

  let academicQualification: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/academic-qualifications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/academic-qualifications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/academic-qualifications/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (academicQualification) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/academic-qualifications/${academicQualification.id}`,
      }).then(() => {
        academicQualification = undefined;
      });
    }
  });

  it('AcademicQualifications menu should load AcademicQualifications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('academic-qualification');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AcademicQualification').should('exist');
    cy.url().should('match', academicQualificationPageUrlPattern);
  });

  describe('AcademicQualification page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(academicQualificationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AcademicQualification page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/academic-qualification/new$'));
        cy.getEntityCreateUpdateHeading('AcademicQualification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academicQualificationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/academic-qualifications',
          body: academicQualificationSample,
        }).then(({ body }) => {
          academicQualification = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/academic-qualifications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [academicQualification],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(academicQualificationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AcademicQualification page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('academicQualification');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academicQualificationPageUrlPattern);
      });

      it('edit button click should load edit AcademicQualification page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AcademicQualification');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academicQualificationPageUrlPattern);
      });

      it('last delete button click should delete instance of AcademicQualification', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('academicQualification').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', academicQualificationPageUrlPattern);

        academicQualification = undefined;
      });
    });
  });

  describe('new AcademicQualification page', () => {
    beforeEach(() => {
      cy.visit(`${academicQualificationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AcademicQualification');
    });

    it('should create an instance of AcademicQualification', () => {
      cy.get(`[data-cy="academicQualificationsCode"]`).type('Strategist Uruguay South').should('have.value', 'Strategist Uruguay South');

      cy.get(`[data-cy="academicQualificationType"]`).type('panel Central Factors').should('have.value', 'panel Central Factors');

      cy.get(`[data-cy="academicQualificationTypeDetail"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        academicQualification = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', academicQualificationPageUrlPattern);
    });
  });
});
