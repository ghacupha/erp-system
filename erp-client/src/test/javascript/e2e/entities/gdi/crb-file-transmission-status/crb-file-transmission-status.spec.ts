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
  CrbFileTransmissionStatusComponentsPage,
  CrbFileTransmissionStatusDeleteDialog,
  CrbFileTransmissionStatusUpdatePage,
} from './crb-file-transmission-status.page-object';

const expect = chai.expect;

describe('CrbFileTransmissionStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbFileTransmissionStatusComponentsPage: CrbFileTransmissionStatusComponentsPage;
  let crbFileTransmissionStatusUpdatePage: CrbFileTransmissionStatusUpdatePage;
  let crbFileTransmissionStatusDeleteDialog: CrbFileTransmissionStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbFileTransmissionStatuses', async () => {
    await navBarPage.goToEntity('crb-file-transmission-status');
    crbFileTransmissionStatusComponentsPage = new CrbFileTransmissionStatusComponentsPage();
    await browser.wait(ec.visibilityOf(crbFileTransmissionStatusComponentsPage.title), 5000);
    expect(await crbFileTransmissionStatusComponentsPage.getTitle()).to.eq('Crb File Transmission Statuses');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbFileTransmissionStatusComponentsPage.entities),
        ec.visibilityOf(crbFileTransmissionStatusComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbFileTransmissionStatus page', async () => {
    await crbFileTransmissionStatusComponentsPage.clickOnCreateButton();
    crbFileTransmissionStatusUpdatePage = new CrbFileTransmissionStatusUpdatePage();
    expect(await crbFileTransmissionStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Crb File Transmission Status');
    await crbFileTransmissionStatusUpdatePage.cancel();
  });

  it('should create and save CrbFileTransmissionStatuses', async () => {
    const nbButtonsBeforeCreate = await crbFileTransmissionStatusComponentsPage.countDeleteButtons();

    await crbFileTransmissionStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      crbFileTransmissionStatusUpdatePage.setSubmittedFileStatusTypeCodeInput('submittedFileStatusTypeCode'),
      crbFileTransmissionStatusUpdatePage.submittedFileStatusTypeSelectLastOption(),
      crbFileTransmissionStatusUpdatePage.setSubmittedFileStatusTypeDescriptionInput('submittedFileStatusTypeDescription'),
    ]);

    await crbFileTransmissionStatusUpdatePage.save();
    expect(await crbFileTransmissionStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbFileTransmissionStatusComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbFileTransmissionStatus', async () => {
    const nbButtonsBeforeDelete = await crbFileTransmissionStatusComponentsPage.countDeleteButtons();
    await crbFileTransmissionStatusComponentsPage.clickOnLastDeleteButton();

    crbFileTransmissionStatusDeleteDialog = new CrbFileTransmissionStatusDeleteDialog();
    expect(await crbFileTransmissionStatusDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb File Transmission Status?'
    );
    await crbFileTransmissionStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbFileTransmissionStatusComponentsPage.title), 5000);

    expect(await crbFileTransmissionStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
