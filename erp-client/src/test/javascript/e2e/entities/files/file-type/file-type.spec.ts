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

import { FileTypeComponentsPage, FileTypeDeleteDialog, FileTypeUpdatePage } from './file-type.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('FileType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fileTypeComponentsPage: FileTypeComponentsPage;
  let fileTypeUpdatePage: FileTypeUpdatePage;
  let fileTypeDeleteDialog: FileTypeDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FileTypes', async () => {
    await navBarPage.goToEntity('file-type');
    fileTypeComponentsPage = new FileTypeComponentsPage();
    await browser.wait(ec.visibilityOf(fileTypeComponentsPage.title), 5000);
    expect(await fileTypeComponentsPage.getTitle()).to.eq('File Types');
    await browser.wait(ec.or(ec.visibilityOf(fileTypeComponentsPage.entities), ec.visibilityOf(fileTypeComponentsPage.noResult)), 1000);
  });

  it('should load create FileType page', async () => {
    await fileTypeComponentsPage.clickOnCreateButton();
    fileTypeUpdatePage = new FileTypeUpdatePage();
    expect(await fileTypeUpdatePage.getPageTitle()).to.eq('Create or edit a File Type');
    await fileTypeUpdatePage.cancel();
  });

  it('should create and save FileTypes', async () => {
    const nbButtonsBeforeCreate = await fileTypeComponentsPage.countDeleteButtons();

    await fileTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      fileTypeUpdatePage.setFileTypeNameInput('fileTypeName'),
      fileTypeUpdatePage.fileMediumTypeSelectLastOption(),
      fileTypeUpdatePage.setDescriptionInput('description'),
      fileTypeUpdatePage.setFileTemplateInput(absolutePath),
      fileTypeUpdatePage.fileTypeSelectLastOption(),
      // fileTypeUpdatePage.placeholderSelectLastOption(),
    ]);

    await fileTypeUpdatePage.save();
    expect(await fileTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await fileTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FileType', async () => {
    const nbButtonsBeforeDelete = await fileTypeComponentsPage.countDeleteButtons();
    await fileTypeComponentsPage.clickOnLastDeleteButton();

    fileTypeDeleteDialog = new FileTypeDeleteDialog();
    expect(await fileTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this File Type?');
    await fileTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(fileTypeComponentsPage.title), 5000);

    expect(await fileTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
