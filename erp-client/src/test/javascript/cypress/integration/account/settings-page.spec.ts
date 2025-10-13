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

import { firstNameSettingsSelector, lastNameSettingsSelector, submitSettingsSelector, emailSettingsSelector } from '../../support/commands';

describe('/account/settings', () => {
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookies();
    cy.visit('');
    cy.login('user', 'user');
    cy.clickOnSettingsItem();
  });

  beforeEach(() => {
    cy.intercept('POST', '/api/account').as('settingsSave');
  });

  it("should be able to change 'user' firstname settings", () => {
    cy.get(firstNameSettingsSelector).clear().type('jhipster');
    // need to modify email because default email does not match regex in vue
    cy.get(emailSettingsSelector).clear().type('user@localhost.fr');
    cy.get(submitSettingsSelector).click({ force: true });
    cy.wait('@settingsSave').then(({ response }) => expect(response.statusCode).to.equal(200));
  });

  it("should be able to change 'user' lastname settings", () => {
    cy.get(lastNameSettingsSelector).clear().type('retspihj');
    // need to modify email because default email does not match regex in vue
    cy.get(emailSettingsSelector).clear().type('user@localhost.fr');
    cy.get(submitSettingsSelector).click({ force: true });
    cy.wait('@settingsSave').then(({ response }) => expect(response.statusCode).to.equal(200));
  });

  it("should be able to change 'user' email settings", () => {
    cy.get(emailSettingsSelector).clear().type('user@localhost.fr');
    cy.get(submitSettingsSelector).click({ force: true });
    cy.wait('@settingsSave').then(({ response }) => expect(response.statusCode).to.equal(200));
  });

  it("should not be able to change 'user' settings if email already exists", () => {
    // add an email for admin account
    cy.clickOnLogoutItem();
    cy.visit('');
    cy.login(username, password);
    cy.clickOnSettingsItem();
    cy.get(emailSettingsSelector).clear().type('admin@localhost.fr');
    cy.get(submitSettingsSelector).click({ force: true });
    cy.clickOnLogoutItem();

    // try to reuse email used in admin account
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.intercept('POST', '/api/account').as('settingsNotSave');
    cy.login('user', 'user');
    cy.clickOnSettingsItem();
    cy.get(emailSettingsSelector).clear().type('admin@localhost.fr');
    cy.get(submitSettingsSelector).click({ force: true });
    // Fix in future version of cypress
    // => https://glebbahmutov.com/blog/cypress-intercept-problems/#no-overwriting-interceptors
    // => https://github.com/cypress-io/cypress/issues/9302
    // cy.wait('@settingsNotSave').then(({ response }) => expect(response.statusCode).to.equal(400));
  });
});
