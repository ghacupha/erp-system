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
  NatureOfCustomerComplaintsComponentsPage,
  NatureOfCustomerComplaintsDeleteDialog,
  NatureOfCustomerComplaintsUpdatePage,
} from './nature-of-customer-complaints.page-object';

const expect = chai.expect;

describe('NatureOfCustomerComplaints e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let natureOfCustomerComplaintsComponentsPage: NatureOfCustomerComplaintsComponentsPage;
  let natureOfCustomerComplaintsUpdatePage: NatureOfCustomerComplaintsUpdatePage;
  let natureOfCustomerComplaintsDeleteDialog: NatureOfCustomerComplaintsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NatureOfCustomerComplaints', async () => {
    await navBarPage.goToEntity('nature-of-customer-complaints');
    natureOfCustomerComplaintsComponentsPage = new NatureOfCustomerComplaintsComponentsPage();
    await browser.wait(ec.visibilityOf(natureOfCustomerComplaintsComponentsPage.title), 5000);
    expect(await natureOfCustomerComplaintsComponentsPage.getTitle()).to.eq('Nature Of Customer Complaints');
    await browser.wait(
      ec.or(
        ec.visibilityOf(natureOfCustomerComplaintsComponentsPage.entities),
        ec.visibilityOf(natureOfCustomerComplaintsComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create NatureOfCustomerComplaints page', async () => {
    await natureOfCustomerComplaintsComponentsPage.clickOnCreateButton();
    natureOfCustomerComplaintsUpdatePage = new NatureOfCustomerComplaintsUpdatePage();
    expect(await natureOfCustomerComplaintsUpdatePage.getPageTitle()).to.eq('Create or edit a Nature Of Customer Complaints');
    await natureOfCustomerComplaintsUpdatePage.cancel();
  });

  it('should create and save NatureOfCustomerComplaints', async () => {
    const nbButtonsBeforeCreate = await natureOfCustomerComplaintsComponentsPage.countDeleteButtons();

    await natureOfCustomerComplaintsComponentsPage.clickOnCreateButton();

    await promise.all([
      natureOfCustomerComplaintsUpdatePage.setNatureOfComplaintTypeCodeInput('natureOfComplaintTypeCode'),
      natureOfCustomerComplaintsUpdatePage.setNatureOfComplaintTypeInput('natureOfComplaintType'),
      natureOfCustomerComplaintsUpdatePage.setNatureOfComplaintTypeDetailsInput('natureOfComplaintTypeDetails'),
    ]);

    await natureOfCustomerComplaintsUpdatePage.save();
    expect(await natureOfCustomerComplaintsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await natureOfCustomerComplaintsComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last NatureOfCustomerComplaints', async () => {
    const nbButtonsBeforeDelete = await natureOfCustomerComplaintsComponentsPage.countDeleteButtons();
    await natureOfCustomerComplaintsComponentsPage.clickOnLastDeleteButton();

    natureOfCustomerComplaintsDeleteDialog = new NatureOfCustomerComplaintsDeleteDialog();
    expect(await natureOfCustomerComplaintsDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Nature Of Customer Complaints?'
    );
    await natureOfCustomerComplaintsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(natureOfCustomerComplaintsComponentsPage.title), 5000);

    expect(await natureOfCustomerComplaintsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
