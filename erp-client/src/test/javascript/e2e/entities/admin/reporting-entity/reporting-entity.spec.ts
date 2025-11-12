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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ReportingEntityComponentsPage, ReportingEntityDeleteDialog, ReportingEntityUpdatePage } from './reporting-entity.page-object';

const expect = chai.expect;

describe('ReportingEntity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reportingEntityComponentsPage: ReportingEntityComponentsPage;
  let reportingEntityUpdatePage: ReportingEntityUpdatePage;
  let reportingEntityDeleteDialog: ReportingEntityDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ReportingEntities', async () => {
    await navBarPage.goToEntity('reporting-entity');
    reportingEntityComponentsPage = new ReportingEntityComponentsPage();
    await browser.wait(ec.visibilityOf(reportingEntityComponentsPage.title), 5000);
    expect(await reportingEntityComponentsPage.getTitle()).to.eq('Reporting Entities');
    await browser.wait(
      ec.or(ec.visibilityOf(reportingEntityComponentsPage.entities), ec.visibilityOf(reportingEntityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ReportingEntity page', async () => {
    await reportingEntityComponentsPage.clickOnCreateButton();
    reportingEntityUpdatePage = new ReportingEntityUpdatePage();
    expect(await reportingEntityUpdatePage.getPageTitle()).to.eq('Create or edit a Reporting Entity');
    await reportingEntityUpdatePage.cancel();
  });

  it('should create and save ReportingEntities', async () => {
    const nbButtonsBeforeCreate = await reportingEntityComponentsPage.countDeleteButtons();

    await reportingEntityComponentsPage.clickOnCreateButton();

    await promise.all([
      reportingEntityUpdatePage.setEntityNameInput('entityName'),
      reportingEntityUpdatePage.reportingCurrencySelectLastOption(),
      reportingEntityUpdatePage.retainedEarningsAccountSelectLastOption(),
    ]);

    await reportingEntityUpdatePage.save();
    expect(await reportingEntityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reportingEntityComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ReportingEntity', async () => {
    const nbButtonsBeforeDelete = await reportingEntityComponentsPage.countDeleteButtons();
    await reportingEntityComponentsPage.clickOnLastDeleteButton();

    reportingEntityDeleteDialog = new ReportingEntityDeleteDialog();
    expect(await reportingEntityDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Reporting Entity?');
    await reportingEntityDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(reportingEntityComponentsPage.title), 5000);

    expect(await reportingEntityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
