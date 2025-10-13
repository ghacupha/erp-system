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

/* eslint-disable @typescript-eslint/no-namespace */
/* eslint-disable @typescript-eslint/no-use-before-define */

import {
  navbarSelector,
  adminMenuSelector,
  accountMenuSelector,
  registerItemSelector,
  loginItemSelector,
  logoutItemSelector,
  settingsItemSelector,
  passwordItemSelector,
  entityItemSelector,
} from './commands';

Cypress.Commands.add('clickOnLoginItem', () => {
  return cy.get(navbarSelector).get(accountMenuSelector).click({ force: true }).get(loginItemSelector).click({ force: true });
});

Cypress.Commands.add('clickOnLogoutItem', () => {
  return cy.get(navbarSelector).get(accountMenuSelector).click({ force: true }).get(logoutItemSelector).click({ force: true });
});

Cypress.Commands.add('clickOnRegisterItem', () => {
  return cy.get(navbarSelector).get(accountMenuSelector).click({ force: true }).get(registerItemSelector).click({ force: true });
});

Cypress.Commands.add('clickOnSettingsItem', () => {
  return cy.get(navbarSelector).get(accountMenuSelector).click({ force: true }).get(settingsItemSelector).click({ force: true });
});

Cypress.Commands.add('clickOnPasswordItem', () => {
  return cy.get(navbarSelector).get(accountMenuSelector).click({ force: true }).get(passwordItemSelector).click({ force: true });
});

Cypress.Commands.add('clickOnAdminMenuItem', (item: string) => {
  return cy
    .get(navbarSelector)
    .get(adminMenuSelector)
    .click({ force: true })
    .get(`.dropdown-item[href="/admin/${item}"]`)
    .click({ force: true });
});

Cypress.Commands.add('clickOnEntityMenuItem', (entityName: string) => {
  return cy
    .get(navbarSelector)
    .get(entityItemSelector)
    .click({ force: true })
    .get(`.dropdown-item[href="/${entityName}"]`)
    .click({ force: true });
});

declare global {
  namespace Cypress {
    interface Chainable {
      clickOnLoginItem(): Cypress.Chainable;
      clickOnLogoutItem(): Cypress.Chainable;
      clickOnRegisterItem(): Cypress.Chainable;
      clickOnSettingsItem(): Cypress.Chainable;
      clickOnPasswordItem(): Cypress.Chainable;
      clickOnAdminMenuItem(item: string): Cypress.Chainable;
      clickOnEntityMenuItem(entityName: string): Cypress.Chainable;
    }
  }
}

// Convert this to a module instead of script (allows import/export)
export {};
