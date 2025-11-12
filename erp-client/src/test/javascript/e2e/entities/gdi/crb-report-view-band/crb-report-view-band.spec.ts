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

import {
  CrbReportViewBandComponentsPage,
  CrbReportViewBandDeleteDialog,
  CrbReportViewBandUpdatePage,
} from './crb-report-view-band.page-object';

const expect = chai.expect;

describe('CrbReportViewBand e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbReportViewBandComponentsPage: CrbReportViewBandComponentsPage;
  let crbReportViewBandUpdatePage: CrbReportViewBandUpdatePage;
  let crbReportViewBandDeleteDialog: CrbReportViewBandDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbReportViewBands', async () => {
    await navBarPage.goToEntity('crb-report-view-band');
    crbReportViewBandComponentsPage = new CrbReportViewBandComponentsPage();
    await browser.wait(ec.visibilityOf(crbReportViewBandComponentsPage.title), 5000);
    expect(await crbReportViewBandComponentsPage.getTitle()).to.eq('Crb Report View Bands');
    await browser.wait(
      ec.or(ec.visibilityOf(crbReportViewBandComponentsPage.entities), ec.visibilityOf(crbReportViewBandComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbReportViewBand page', async () => {
    await crbReportViewBandComponentsPage.clickOnCreateButton();
    crbReportViewBandUpdatePage = new CrbReportViewBandUpdatePage();
    expect(await crbReportViewBandUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Report View Band');
    await crbReportViewBandUpdatePage.cancel();
  });

  it('should create and save CrbReportViewBands', async () => {
    const nbButtonsBeforeCreate = await crbReportViewBandComponentsPage.countDeleteButtons();

    await crbReportViewBandComponentsPage.clickOnCreateButton();

    await promise.all([
      crbReportViewBandUpdatePage.setReportViewCodeInput('reportViewCode'),
      crbReportViewBandUpdatePage.setReportViewCategoryInput('reportViewCategory'),
      crbReportViewBandUpdatePage.setReportViewCategoryDescriptionInput('reportViewCategoryDescription'),
    ]);

    await crbReportViewBandUpdatePage.save();
    expect(await crbReportViewBandUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbReportViewBandComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbReportViewBand', async () => {
    const nbButtonsBeforeDelete = await crbReportViewBandComponentsPage.countDeleteButtons();
    await crbReportViewBandComponentsPage.clickOnLastDeleteButton();

    crbReportViewBandDeleteDialog = new CrbReportViewBandDeleteDialog();
    expect(await crbReportViewBandDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Report View Band?');
    await crbReportViewBandDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbReportViewBandComponentsPage.title), 5000);

    expect(await crbReportViewBandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
