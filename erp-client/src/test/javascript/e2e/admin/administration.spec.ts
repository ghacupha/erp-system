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

import { browser, element, by, ExpectedConditions as ec } from 'protractor';

import { NavBarPage, SignInPage } from '../page-objects/jhi-page-objects';

const expect = chai.expect;

describe('administration', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage(true);
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.adminMenu), 5000);
  });

  beforeEach(async () => {
    await navBarPage.clickOnAdminMenu();
  });

  it('should load user management', async () => {
    await navBarPage.clickOnAdmin('user-management');
    const expect1 = 'Users';
    const value1 = await element(by.id('user-management-page-heading')).getText();
    expect(value1).to.eq(expect1);
  });

  it('should load metrics', async () => {
    await navBarPage.clickOnAdmin('metrics');
    const heading = element(by.id('metrics-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'Application Metrics';
    const value1 = await heading.getText();
    expect(value1).to.eq(expect1);
  });

  it('should load health', async () => {
    await navBarPage.clickOnAdmin('health');
    const heading = element(by.id('health-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'Health Checks';
    const value1 = await heading.getText();
    expect(value1).to.eq(expect1);
  });

  it('should load configuration', async () => {
    await navBarPage.clickOnAdmin('configuration');
    const heading = element(by.id('configuration-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'Configuration';
    const value1 = await heading.getText();
    expect(value1).to.eq(expect1);
  });

  it('should load logs', async () => {
    await navBarPage.clickOnAdmin('logs');
    const heading = element(by.id('logs-page-heading'));
    await browser.wait(ec.visibilityOf(heading), 10000);
    const expect1 = 'Logs';
    const value1 = await heading.getText();
    expect(value1).to.eq(expect1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
