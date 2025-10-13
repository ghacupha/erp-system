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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbRecordFileTypeComponentsPage,
  CrbRecordFileTypeDeleteDialog,
  CrbRecordFileTypeUpdatePage,
} from './crb-record-file-type.page-object';

const expect = chai.expect;

describe('CrbRecordFileType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbRecordFileTypeComponentsPage: CrbRecordFileTypeComponentsPage;
  let crbRecordFileTypeUpdatePage: CrbRecordFileTypeUpdatePage;
  let crbRecordFileTypeDeleteDialog: CrbRecordFileTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbRecordFileTypes', async () => {
    await navBarPage.goToEntity('crb-record-file-type');
    crbRecordFileTypeComponentsPage = new CrbRecordFileTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbRecordFileTypeComponentsPage.title), 5000);
    expect(await crbRecordFileTypeComponentsPage.getTitle()).to.eq('Crb Record File Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbRecordFileTypeComponentsPage.entities), ec.visibilityOf(crbRecordFileTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbRecordFileType page', async () => {
    await crbRecordFileTypeComponentsPage.clickOnCreateButton();
    crbRecordFileTypeUpdatePage = new CrbRecordFileTypeUpdatePage();
    expect(await crbRecordFileTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Record File Type');
    await crbRecordFileTypeUpdatePage.cancel();
  });

  it('should create and save CrbRecordFileTypes', async () => {
    const nbButtonsBeforeCreate = await crbRecordFileTypeComponentsPage.countDeleteButtons();

    await crbRecordFileTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbRecordFileTypeUpdatePage.setRecordFileTypeCodeInput('recordFileTypeCode'),
      crbRecordFileTypeUpdatePage.setRecordFileTypeInput('recordFileType'),
      crbRecordFileTypeUpdatePage.setRecordFileTypeDetailsInput('recordFileTypeDetails'),
    ]);

    await crbRecordFileTypeUpdatePage.save();
    expect(await crbRecordFileTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbRecordFileTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbRecordFileType', async () => {
    const nbButtonsBeforeDelete = await crbRecordFileTypeComponentsPage.countDeleteButtons();
    await crbRecordFileTypeComponentsPage.clickOnLastDeleteButton();

    crbRecordFileTypeDeleteDialog = new CrbRecordFileTypeDeleteDialog();
    expect(await crbRecordFileTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Record File Type?');
    await crbRecordFileTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbRecordFileTypeComponentsPage.title), 5000);

    expect(await crbRecordFileTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
