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
  CustomerComplaintStatusTypeComponentsPage,
  CustomerComplaintStatusTypeDeleteDialog,
  CustomerComplaintStatusTypeUpdatePage,
} from './customer-complaint-status-type.page-object';

const expect = chai.expect;

describe('CustomerComplaintStatusType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let customerComplaintStatusTypeComponentsPage: CustomerComplaintStatusTypeComponentsPage;
  let customerComplaintStatusTypeUpdatePage: CustomerComplaintStatusTypeUpdatePage;
  let customerComplaintStatusTypeDeleteDialog: CustomerComplaintStatusTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CustomerComplaintStatusTypes', async () => {
    await navBarPage.goToEntity('customer-complaint-status-type');
    customerComplaintStatusTypeComponentsPage = new CustomerComplaintStatusTypeComponentsPage();
    await browser.wait(ec.visibilityOf(customerComplaintStatusTypeComponentsPage.title), 5000);
    expect(await customerComplaintStatusTypeComponentsPage.getTitle()).to.eq('Customer Complaint Status Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(customerComplaintStatusTypeComponentsPage.entities),
        ec.visibilityOf(customerComplaintStatusTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CustomerComplaintStatusType page', async () => {
    await customerComplaintStatusTypeComponentsPage.clickOnCreateButton();
    customerComplaintStatusTypeUpdatePage = new CustomerComplaintStatusTypeUpdatePage();
    expect(await customerComplaintStatusTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Customer Complaint Status Type');
    await customerComplaintStatusTypeUpdatePage.cancel();
  });

  it('should create and save CustomerComplaintStatusTypes', async () => {
    const nbButtonsBeforeCreate = await customerComplaintStatusTypeComponentsPage.countDeleteButtons();

    await customerComplaintStatusTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      customerComplaintStatusTypeUpdatePage.setCustomerComplaintStatusTypeCodeInput('customerComplaintStatusTypeCode'),
      customerComplaintStatusTypeUpdatePage.setCustomerComplaintStatusTypeInput('customerComplaintStatusType'),
      customerComplaintStatusTypeUpdatePage.setCustomerComplaintStatusTypeDetailsInput('customerComplaintStatusTypeDetails'),
    ]);

    await customerComplaintStatusTypeUpdatePage.save();
    expect(await customerComplaintStatusTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await customerComplaintStatusTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CustomerComplaintStatusType', async () => {
    const nbButtonsBeforeDelete = await customerComplaintStatusTypeComponentsPage.countDeleteButtons();
    await customerComplaintStatusTypeComponentsPage.clickOnLastDeleteButton();

    customerComplaintStatusTypeDeleteDialog = new CustomerComplaintStatusTypeDeleteDialog();
    expect(await customerComplaintStatusTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Customer Complaint Status Type?'
    );
    await customerComplaintStatusTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(customerComplaintStatusTypeComponentsPage.title), 5000);

    expect(await customerComplaintStatusTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
