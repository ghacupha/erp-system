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

import { CrbCustomerTypeComponentsPage, CrbCustomerTypeDeleteDialog, CrbCustomerTypeUpdatePage } from './crb-customer-type.page-object';

const expect = chai.expect;

describe('CrbCustomerType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbCustomerTypeComponentsPage: CrbCustomerTypeComponentsPage;
  let crbCustomerTypeUpdatePage: CrbCustomerTypeUpdatePage;
  let crbCustomerTypeDeleteDialog: CrbCustomerTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbCustomerTypes', async () => {
    await navBarPage.goToEntity('crb-customer-type');
    crbCustomerTypeComponentsPage = new CrbCustomerTypeComponentsPage();
    await browser.wait(ec.visibilityOf(crbCustomerTypeComponentsPage.title), 5000);
    expect(await crbCustomerTypeComponentsPage.getTitle()).to.eq('Crb Customer Types');
    await browser.wait(
      ec.or(ec.visibilityOf(crbCustomerTypeComponentsPage.entities), ec.visibilityOf(crbCustomerTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbCustomerType page', async () => {
    await crbCustomerTypeComponentsPage.clickOnCreateButton();
    crbCustomerTypeUpdatePage = new CrbCustomerTypeUpdatePage();
    expect(await crbCustomerTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Customer Type');
    await crbCustomerTypeUpdatePage.cancel();
  });

  it('should create and save CrbCustomerTypes', async () => {
    const nbButtonsBeforeCreate = await crbCustomerTypeComponentsPage.countDeleteButtons();

    await crbCustomerTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbCustomerTypeUpdatePage.setCustomerTypeCodeInput('customerTypeCode'),
      crbCustomerTypeUpdatePage.setCustomerTypeInput('customerType'),
      crbCustomerTypeUpdatePage.setDescriptionInput('description'),
    ]);

    await crbCustomerTypeUpdatePage.save();
    expect(await crbCustomerTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbCustomerTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbCustomerType', async () => {
    const nbButtonsBeforeDelete = await crbCustomerTypeComponentsPage.countDeleteButtons();
    await crbCustomerTypeComponentsPage.clickOnLastDeleteButton();

    crbCustomerTypeDeleteDialog = new CrbCustomerTypeDeleteDialog();
    expect(await crbCustomerTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Crb Customer Type?');
    await crbCustomerTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbCustomerTypeComponentsPage.title), 5000);

    expect(await crbCustomerTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
