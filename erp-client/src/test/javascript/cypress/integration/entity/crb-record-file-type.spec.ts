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

describe('CrbRecordFileType e2e test', () => {
  const crbRecordFileTypePageUrl = '/crb-record-file-type';
  const crbRecordFileTypePageUrlPattern = new RegExp('/crb-record-file-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbRecordFileTypeSample = { recordFileTypeCode: 'Delaware indexing Small', recordFileType: 'SQL' };

  let crbRecordFileType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-record-file-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-record-file-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-record-file-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbRecordFileType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-record-file-types/${crbRecordFileType.id}`,
      }).then(() => {
        crbRecordFileType = undefined;
      });
    }
  });

  it('CrbRecordFileTypes menu should load CrbRecordFileTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-record-file-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbRecordFileType').should('exist');
    cy.url().should('match', crbRecordFileTypePageUrlPattern);
  });

  describe('CrbRecordFileType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbRecordFileTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbRecordFileType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-record-file-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbRecordFileType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbRecordFileTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-record-file-types',
          body: crbRecordFileTypeSample,
        }).then(({ body }) => {
          crbRecordFileType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-record-file-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbRecordFileType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbRecordFileTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbRecordFileType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbRecordFileType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbRecordFileTypePageUrlPattern);
      });

      it('edit button click should load edit CrbRecordFileType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbRecordFileType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbRecordFileTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbRecordFileType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbRecordFileType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbRecordFileTypePageUrlPattern);

        crbRecordFileType = undefined;
      });
    });
  });

  describe('new CrbRecordFileType page', () => {
    beforeEach(() => {
      cy.visit(`${crbRecordFileTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbRecordFileType');
    });

    it('should create an instance of CrbRecordFileType', () => {
      cy.get(`[data-cy="recordFileTypeCode"]`).type('Grove Jewelery').should('have.value', 'Grove Jewelery');

      cy.get(`[data-cy="recordFileType"]`).type('Extended violet').should('have.value', 'Extended violet');

      cy.get(`[data-cy="recordFileTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbRecordFileType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbRecordFileTypePageUrlPattern);
    });
  });
});
