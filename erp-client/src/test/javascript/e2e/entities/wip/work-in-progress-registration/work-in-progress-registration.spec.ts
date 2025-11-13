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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  WorkInProgressRegistrationComponentsPage,
  WorkInProgressRegistrationDeleteDialog,
  WorkInProgressRegistrationUpdatePage,
} from './work-in-progress-registration.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('WorkInProgressRegistration e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workInProgressRegistrationComponentsPage: WorkInProgressRegistrationComponentsPage;
  let workInProgressRegistrationUpdatePage: WorkInProgressRegistrationUpdatePage;
  let workInProgressRegistrationDeleteDialog: WorkInProgressRegistrationDeleteDialog;
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

  it('should load WorkInProgressRegistrations', async () => {
    await navBarPage.goToEntity('work-in-progress-registration');
    workInProgressRegistrationComponentsPage = new WorkInProgressRegistrationComponentsPage();
    await browser.wait(ec.visibilityOf(workInProgressRegistrationComponentsPage.title), 5000);
    expect(await workInProgressRegistrationComponentsPage.getTitle()).to.eq('Work In Progress Registrations');
    await browser.wait(
      ec.or(
        ec.visibilityOf(workInProgressRegistrationComponentsPage.entities),
        ec.visibilityOf(workInProgressRegistrationComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create WorkInProgressRegistration page', async () => {
    await workInProgressRegistrationComponentsPage.clickOnCreateButton();
    workInProgressRegistrationUpdatePage = new WorkInProgressRegistrationUpdatePage();
    expect(await workInProgressRegistrationUpdatePage.getPageTitle()).to.eq('Create or edit a Work In Progress Registration');
    await workInProgressRegistrationUpdatePage.cancel();
  });

  it('should create and save WorkInProgressRegistrations', async () => {
    const nbButtonsBeforeCreate = await workInProgressRegistrationComponentsPage.countDeleteButtons();

    await workInProgressRegistrationComponentsPage.clickOnCreateButton();

    await promise.all([
      workInProgressRegistrationUpdatePage.setSequenceNumberInput('sequenceNumber'),
      workInProgressRegistrationUpdatePage.setParticularsInput('particulars'),
      workInProgressRegistrationUpdatePage.setInstalmentDateInput('2000-12-31'),
      workInProgressRegistrationUpdatePage.setInstalmentAmountInput('5'),
      workInProgressRegistrationUpdatePage.setCommentsInput(absolutePath),
      workInProgressRegistrationUpdatePage.setLevelOfCompletionInput('5'),
      workInProgressRegistrationUpdatePage.getCompletedInput().click(),
      // workInProgressRegistrationUpdatePage.placeholderSelectLastOption(),
      workInProgressRegistrationUpdatePage.workInProgressGroupSelectLastOption(),
      workInProgressRegistrationUpdatePage.settlementCurrencySelectLastOption(),
      workInProgressRegistrationUpdatePage.workProjectRegisterSelectLastOption(),
      // workInProgressRegistrationUpdatePage.businessDocumentSelectLastOption(),
      // workInProgressRegistrationUpdatePage.assetAccessorySelectLastOption(),
      // workInProgressRegistrationUpdatePage.assetWarrantySelectLastOption(),
      workInProgressRegistrationUpdatePage.invoiceSelectLastOption(),
      workInProgressRegistrationUpdatePage.outletCodeSelectLastOption(),
      workInProgressRegistrationUpdatePage.settlementTransactionSelectLastOption(),
      workInProgressRegistrationUpdatePage.purchaseOrderSelectLastOption(),
      workInProgressRegistrationUpdatePage.deliveryNoteSelectLastOption(),
      workInProgressRegistrationUpdatePage.jobSheetSelectLastOption(),
      workInProgressRegistrationUpdatePage.dealerSelectLastOption(),
    ]);

    await workInProgressRegistrationUpdatePage.save();
    expect(await workInProgressRegistrationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await workInProgressRegistrationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last WorkInProgressRegistration', async () => {
    const nbButtonsBeforeDelete = await workInProgressRegistrationComponentsPage.countDeleteButtons();
    await workInProgressRegistrationComponentsPage.clickOnLastDeleteButton();

    workInProgressRegistrationDeleteDialog = new WorkInProgressRegistrationDeleteDialog();
    expect(await workInProgressRegistrationDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Work In Progress Registration?'
    );
    await workInProgressRegistrationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(workInProgressRegistrationComponentsPage.title), 5000);

    expect(await workInProgressRegistrationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
