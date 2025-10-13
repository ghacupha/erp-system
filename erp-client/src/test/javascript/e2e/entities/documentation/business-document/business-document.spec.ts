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

import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  BusinessDocumentComponentsPage,
  /* BusinessDocumentDeleteDialog, */
  BusinessDocumentUpdatePage,
} from './business-document.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('BusinessDocument e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let businessDocumentComponentsPage: BusinessDocumentComponentsPage;
  let businessDocumentUpdatePage: BusinessDocumentUpdatePage;
  /* let businessDocumentDeleteDialog: BusinessDocumentDeleteDialog; */
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

  it('should load BusinessDocuments', async () => {
    await navBarPage.goToEntity('business-document');
    businessDocumentComponentsPage = new BusinessDocumentComponentsPage();
    await browser.wait(ec.visibilityOf(businessDocumentComponentsPage.title), 5000);
    expect(await businessDocumentComponentsPage.getTitle()).to.eq('Business Documents');
    await browser.wait(
      ec.or(ec.visibilityOf(businessDocumentComponentsPage.entities), ec.visibilityOf(businessDocumentComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BusinessDocument page', async () => {
    await businessDocumentComponentsPage.clickOnCreateButton();
    businessDocumentUpdatePage = new BusinessDocumentUpdatePage();
    expect(await businessDocumentUpdatePage.getPageTitle()).to.eq('Create or edit a Business Document');
    await businessDocumentUpdatePage.cancel();
  });

  /* it('should create and save BusinessDocuments', async () => {
        const nbButtonsBeforeCreate = await businessDocumentComponentsPage.countDeleteButtons();

        await businessDocumentComponentsPage.clickOnCreateButton();

        await promise.all([
            businessDocumentUpdatePage.setDocumentTitleInput('documentTitle'),
            businessDocumentUpdatePage.setDescriptionInput('description'),
            businessDocumentUpdatePage.setDocumentSerialInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            businessDocumentUpdatePage.setLastModifiedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            businessDocumentUpdatePage.setAttachmentFilePathInput('attachmentFilePath'),
            businessDocumentUpdatePage.setDocumentFileInput(absolutePath),
            businessDocumentUpdatePage.getFileTamperedInput().click(),
            businessDocumentUpdatePage.setDocumentFileChecksumInput('documentFileChecksum'),
            businessDocumentUpdatePage.createdBySelectLastOption(),
            businessDocumentUpdatePage.lastModifiedBySelectLastOption(),
            businessDocumentUpdatePage.originatingDepartmentSelectLastOption(),
            // businessDocumentUpdatePage.applicationMappingsSelectLastOption(),
            // businessDocumentUpdatePage.placeholderSelectLastOption(),
            businessDocumentUpdatePage.fileChecksumAlgorithmSelectLastOption(),
            businessDocumentUpdatePage.securityClearanceSelectLastOption(),
        ]);

        await businessDocumentUpdatePage.save();
        expect(await businessDocumentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await businessDocumentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last BusinessDocument', async () => {
        const nbButtonsBeforeDelete = await businessDocumentComponentsPage.countDeleteButtons();
        await businessDocumentComponentsPage.clickOnLastDeleteButton();

        businessDocumentDeleteDialog = new BusinessDocumentDeleteDialog();
        expect(await businessDocumentDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Business Document?');
        await businessDocumentDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(businessDocumentComponentsPage.title), 5000);

        expect(await businessDocumentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
