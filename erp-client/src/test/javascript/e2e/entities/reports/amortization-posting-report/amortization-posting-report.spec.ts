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

import { AmortizationPostingReportComponentsPage } from './amortization-posting-report.page-object';

const expect = chai.expect;

describe('AmortizationPostingReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let amortizationPostingReportComponentsPage: AmortizationPostingReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AmortizationPostingReports', async () => {
    await navBarPage.goToEntity('amortization-posting-report');
    amortizationPostingReportComponentsPage = new AmortizationPostingReportComponentsPage();
    await browser.wait(ec.visibilityOf(amortizationPostingReportComponentsPage.title), 5000);
    expect(await amortizationPostingReportComponentsPage.getTitle()).to.eq('Amortization Posting Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(amortizationPostingReportComponentsPage.entities),
        ec.visibilityOf(amortizationPostingReportComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
