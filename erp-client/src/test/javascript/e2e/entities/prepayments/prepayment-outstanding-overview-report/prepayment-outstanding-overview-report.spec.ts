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

import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PrepaymentOutstandingOverviewReportComponentsPage } from './prepayment-outstanding-overview-report.page-object';

const expect = chai.expect;

describe('PrepaymentOutstandingOverviewReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentOutstandingOverviewReportComponentsPage: PrepaymentOutstandingOverviewReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentOutstandingOverviewReports', async () => {
    await navBarPage.goToEntity('prepayment-outstanding-overview-report');
    prepaymentOutstandingOverviewReportComponentsPage = new PrepaymentOutstandingOverviewReportComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentOutstandingOverviewReportComponentsPage.title), 5000);
    expect(await prepaymentOutstandingOverviewReportComponentsPage.getTitle()).to.eq('Prepayment Outstanding Overview Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(prepaymentOutstandingOverviewReportComponentsPage.entities),
        ec.visibilityOf(prepaymentOutstandingOverviewReportComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
