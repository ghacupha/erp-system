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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PerformanceOfForeignSubsidiariesComponentsPage,
  /* PerformanceOfForeignSubsidiariesDeleteDialog, */
  PerformanceOfForeignSubsidiariesUpdatePage,
} from './performance-of-foreign-subsidiaries.page-object';

const expect = chai.expect;

describe('PerformanceOfForeignSubsidiaries e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let performanceOfForeignSubsidiariesComponentsPage: PerformanceOfForeignSubsidiariesComponentsPage;
  let performanceOfForeignSubsidiariesUpdatePage: PerformanceOfForeignSubsidiariesUpdatePage;
  /* let performanceOfForeignSubsidiariesDeleteDialog: PerformanceOfForeignSubsidiariesDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PerformanceOfForeignSubsidiaries', async () => {
    await navBarPage.goToEntity('performance-of-foreign-subsidiaries');
    performanceOfForeignSubsidiariesComponentsPage = new PerformanceOfForeignSubsidiariesComponentsPage();
    await browser.wait(ec.visibilityOf(performanceOfForeignSubsidiariesComponentsPage.title), 5000);
    expect(await performanceOfForeignSubsidiariesComponentsPage.getTitle()).to.eq('Performance Of Foreign Subsidiaries');
    await browser.wait(
      ec.or(
        ec.visibilityOf(performanceOfForeignSubsidiariesComponentsPage.entities),
        ec.visibilityOf(performanceOfForeignSubsidiariesComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create PerformanceOfForeignSubsidiaries page', async () => {
    await performanceOfForeignSubsidiariesComponentsPage.clickOnCreateButton();
    performanceOfForeignSubsidiariesUpdatePage = new PerformanceOfForeignSubsidiariesUpdatePage();
    expect(await performanceOfForeignSubsidiariesUpdatePage.getPageTitle()).to.eq('Create or edit a Performance Of Foreign Subsidiaries');
    await performanceOfForeignSubsidiariesUpdatePage.cancel();
  });

  /* it('should create and save PerformanceOfForeignSubsidiaries', async () => {
        const nbButtonsBeforeCreate = await performanceOfForeignSubsidiariesComponentsPage.countDeleteButtons();

        await performanceOfForeignSubsidiariesComponentsPage.clickOnCreateButton();

        await promise.all([
            performanceOfForeignSubsidiariesUpdatePage.setSubsidiaryNameInput('subsidiaryName'),
            performanceOfForeignSubsidiariesUpdatePage.setReportingDateInput('2000-12-31'),
            performanceOfForeignSubsidiariesUpdatePage.setSubsidiaryIdInput('subsidiaryId'),
            performanceOfForeignSubsidiariesUpdatePage.setGrossLoansAmountInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setGrossNPALoanAmountInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setGrossAssetsAmountInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setGrossDepositsAmountInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setProfitBeforeTaxInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setTotalCapitalAdequacyRatioInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setLiquidityRatioInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setGeneralProvisionsInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setSpecificProvisionsInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setInterestInSuspenseAmountInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setTotalNumberOfStaffInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.setNumberOfBranchesInput('5'),
            performanceOfForeignSubsidiariesUpdatePage.bankCodeSelectLastOption(),
            performanceOfForeignSubsidiariesUpdatePage.subsidiaryCountryCodeSelectLastOption(),
        ]);

        await performanceOfForeignSubsidiariesUpdatePage.save();
        expect(await performanceOfForeignSubsidiariesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await performanceOfForeignSubsidiariesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last PerformanceOfForeignSubsidiaries', async () => {
        const nbButtonsBeforeDelete = await performanceOfForeignSubsidiariesComponentsPage.countDeleteButtons();
        await performanceOfForeignSubsidiariesComponentsPage.clickOnLastDeleteButton();

        performanceOfForeignSubsidiariesDeleteDialog = new PerformanceOfForeignSubsidiariesDeleteDialog();
        expect(await performanceOfForeignSubsidiariesDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Performance Of Foreign Subsidiaries?');
        await performanceOfForeignSubsidiariesDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(performanceOfForeignSubsidiariesComponentsPage.title), 5000);

        expect(await performanceOfForeignSubsidiariesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
