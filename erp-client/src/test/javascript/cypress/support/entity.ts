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

/* eslint-disable @typescript-eslint/no-namespace */
/* eslint-disable @typescript-eslint/no-use-before-define */
// eslint-disable-next-line spaced-comment
/// <reference types="cypress" />

// ***********************************************
// Begin Specific Selector Attributes for Cypress
// ***********************************************

// Entity
export const entityTableSelector = '[data-cy="entityTable"]';
export const entityCreateButtonSelector = '[data-cy="entityCreateButton"]';
export const entityCreateSaveButtonSelector = '[data-cy="entityCreateSaveButton"]';
export const entityCreateCancelButtonSelector = '[data-cy="entityCreateCancelButton"]';
export const entityDetailsButtonSelector = '[data-cy="entityDetailsButton"]'; // can return multiple elements
export const entityDetailsBackButtonSelector = '[data-cy="entityDetailsBackButton"]';
export const entityEditButtonSelector = '[data-cy="entityEditButton"]';
export const entityDeleteButtonSelector = '[data-cy="entityDeleteButton"]';
export const entityConfirmDeleteButtonSelector = '[data-cy="entityConfirmDeleteButton"]';

// ***********************************************
// End Specific Selector Attributes for Cypress
// ***********************************************

Cypress.Commands.add('getEntityHeading', (entityName: string) => {
  return cy.get(`[data-cy="${entityName}Heading"]`);
});

Cypress.Commands.add('getEntityCreateUpdateHeading', (entityName: string) => {
  return cy.get(`[data-cy="${entityName}CreateUpdateHeading"]`);
});

Cypress.Commands.add('getEntityDetailsHeading', (entityInstanceName: string) => {
  return cy.get(`[data-cy="${entityInstanceName}DetailsHeading"]`);
});

Cypress.Commands.add('getEntityDeleteDialogHeading', (entityInstanceName: string) => {
  return cy.get(`[data-cy="${entityInstanceName}DeleteDialogHeading"]`);
});

Cypress.Commands.add('setFieldImageAsBytesOfEntity', (fieldName: string, fileName: string, mimeType: string) => {
  // fileName is the image which you have already put in cypress fixture folder
  // should be like : 'integration-test.png', 'image/png'
  cy.fixture(fileName)
    .as('image')
    .get(`[data-cy="${fieldName}"]`)
    .then(function (el) {
      const blob = Cypress.Blob.base64StringToBlob(this.image, mimeType);
      const file = new File([blob], fileName, { type: mimeType });
      const list = new DataTransfer();
      list.items.add(file);
      const myFileList = list.files;
      (el[0] as HTMLInputElement).files = myFileList;
      el[0].dispatchEvent(new Event('change', { bubbles: true }));
    });
});

Cypress.Commands.add('setFieldSelectToLastOfEntity', (fieldName: string) => {
  return cy.get(`[data-cy="${fieldName}"]`).then(select => {
    const selectSize = (select[0] as HTMLSelectElement)?.options?.length || Number(select.attr('size')) || 0;
    if (selectSize > 0) {
      return cy.get(`[data-cy="${fieldName}"] option`).then((options: any) => {
        const elements = [...options].map(o => o.label);
        const lastElement = elements.length - 1;
        cy.get(`[data-cy="${fieldName}"]`).select(lastElement).type('{downarrow}');
      });
    } else {
      return cy.get(`[data-cy="${fieldName}"]`).type('{downarrow}');
    }
  });
});

declare global {
  namespace Cypress {
    interface Chainable {
      getEntityHeading(entityName: string): Cypress.Chainable;
      getEntityCreateUpdateHeading(entityName: string): Cypress.Chainable;
      getEntityDetailsHeading(entityInstanceName: string): Cypress.Chainable;
      getEntityDeleteDialogHeading(entityInstanceName: string): Cypress.Chainable;
      setFieldImageAsBytesOfEntity(fieldName: string, fileName: string, mimeType: string): Cypress.Chainable;
      setFieldSelectToLastOfEntity(fieldName: string): Cypress.Chainable;
    }
  }
}

// Convert this to a module instead of script (allows import/export)
export {};
