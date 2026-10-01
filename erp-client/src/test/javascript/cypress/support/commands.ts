///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
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
// This commands.ts shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************

// ***********************************************
// Begin Specific Selector Attributes for Cypress
// ***********************************************

// Navbar
export const navbarSelector = '[data-cy="navbar"]';
export const adminMenuSelector = '[data-cy="adminMenu"]';
export const accountMenuSelector = '[data-cy="accountMenu"]';
export const registerItemSelector = '[data-cy="register"]';
export const settingsItemSelector = '[data-cy="settings"]';
export const passwordItemSelector = '[data-cy="passwordItem"]';
export const loginItemSelector = '[data-cy="login"]';
export const logoutItemSelector = '[data-cy="logout"]';
export const entityItemSelector = '[data-cy="entity"]';

// Login
export const titleLoginSelector = '[data-cy="loginTitle"]';
export const errorLoginSelector = '[data-cy="loginError"]';
export const usernameLoginSelector = '[data-cy="username"]';
export const passwordLoginSelector = '[data-cy="password"]';
export const forgetYourPasswordSelector = '[data-cy="forgetYourPasswordSelector"]';
export const submitLoginSelector = '[data-cy="submit"]';

// Register
export const titleRegisterSelector = '[data-cy="registerTitle"]';
export const usernameRegisterSelector = '[data-cy="username"]';
export const emailRegisterSelector = '[data-cy="email"]';
export const firstPasswordRegisterSelector = '[data-cy="firstPassword"]';
export const secondPasswordRegisterSelector = '[data-cy="secondPassword"]';
export const submitRegisterSelector = '[data-cy="submit"]';

// Settings
export const firstNameSettingsSelector = '[data-cy="firstname"]';
export const lastNameSettingsSelector = '[data-cy="lastname"]';
export const emailSettingsSelector = '[data-cy="email"]';
export const languageSettingsSelector = '[data-cy="langKey"]';
export const submitSettingsSelector = '[data-cy="submit"]';

// Password
export const currentPasswordSelector = '[data-cy="currentPassword"]';
export const newPasswordSelector = '[data-cy="newPassword"]';
export const confirmPasswordSelector = '[data-cy="confirmPassword"]';
export const submitPasswordSelector = '[data-cy="submit"]';

// Reset Password
export const emailResetPasswordSelector = '[data-cy="emailResetPassword"]';
export const submitInitResetPasswordSelector = '[data-cy="submit"]';

// Administration
export const userManagementPageHeadingSelector = '[data-cy="userManagementPageHeading"]';
export const swaggerFrameSelector = 'iframe[data-cy="swagger-frame"]';
export const swaggerPageSelector = '[id="swagger-ui"]';
export const metricsPageHeadingSelector = '[data-cy="metricsPageHeading"]';
export const healthPageHeadingSelector = '[data-cy="healthPageHeading"]';
export const logsPageHeadingSelector = '[data-cy="logsPageHeading"]';
export const configurationPageHeadingSelector = '[data-cy="configurationPageHeading"]';

// ***********************************************
// End Specific Selector Attributes for Cypress
// ***********************************************

export const classInvalid = 'ng-invalid';

export const classValid = 'ng-valid';

Cypress.Commands.add('authenticatedRequest', (data: any) => {
  const bearerToken = JSON.parse(sessionStorage.getItem(Cypress.env('jwtStorageName')));
  return cy.request({
    ...data,
    auth: {
      bearer: bearerToken,
    },
  });
});

Cypress.Commands.add('login', (username: string, password: string) => {
  cy.clickOnLoginItem();
  cy.get(usernameLoginSelector).type(username);
  cy.get(passwordLoginSelector).type(password);
  cy.get(submitLoginSelector).click();
});

// The app's custom form-control pickers (Settlement, Invoice, Dealer, Currency, etc.) render an
// ng-select typeahead, not a native <select> - selecting an option means opening it, typing a
// search term, waiting for the async lookup, then clicking the matching option in the dropdown
// panel. `fieldSelector` should target the ng-select host (e.g. by its [formcontrolname] or a
// wrapping [data-cy]); `searchTerm` must match at least part of the option's bound label.
Cypress.Commands.add('selectNgOption', (fieldSelector: string, searchTerm: string) => {
  cy.get(fieldSelector).click();
  cy.get(fieldSelector).find('input').type(searchTerm);
  cy.get('.ng-dropdown-panel .ng-option', { timeout: 10000 }).contains(searchTerm).click();
});

// Clicks the "Create New" footer link inside an ng-select picker's dropdown - used to trigger
// the inline-create workflow from the Settlement (on Prepayment Account) or Invoices (on
// Settlement) pickers.
Cypress.Commands.add('clickNgSelectCreateNew', (fieldSelector: string) => {
  cy.get(fieldSelector).click();
  cy.contains('.ng-dropdown-panel', 'Create New').contains('Create New').click();
});

declare global {
  namespace Cypress {
    interface Chainable {
      login(username: string, password: string): Cypress.Chainable;
      authenticatedRequest(data: any): Cypress.Chainable;
      selectNgOption(fieldSelector: string, searchTerm: string): Cypress.Chainable;
      clickNgSelectCreateNew(fieldSelector: string): Cypress.Chainable;
    }
  }
}

import 'cypress-audit/commands';
// Convert this to a module instead of script (allows import/export)
export {};
