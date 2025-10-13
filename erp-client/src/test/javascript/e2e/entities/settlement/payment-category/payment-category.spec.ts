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

import { PaymentCategoryComponentsPage, PaymentCategoryDeleteDialog, PaymentCategoryUpdatePage } from './payment-category.page-object';

const expect = chai.expect;

describe('PaymentCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let paymentCategoryComponentsPage: PaymentCategoryComponentsPage;
  let paymentCategoryUpdatePage: PaymentCategoryUpdatePage;
  let paymentCategoryDeleteDialog: PaymentCategoryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PaymentCategories', async () => {
    await navBarPage.goToEntity('payment-category');
    paymentCategoryComponentsPage = new PaymentCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(paymentCategoryComponentsPage.title), 5000);
    expect(await paymentCategoryComponentsPage.getTitle()).to.eq('Payment Categories');
    await browser.wait(
      ec.or(ec.visibilityOf(paymentCategoryComponentsPage.entities), ec.visibilityOf(paymentCategoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PaymentCategory page', async () => {
    await paymentCategoryComponentsPage.clickOnCreateButton();
    paymentCategoryUpdatePage = new PaymentCategoryUpdatePage();
    expect(await paymentCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Payment Category');
    await paymentCategoryUpdatePage.cancel();
  });

  it('should create and save PaymentCategories', async () => {
    const nbButtonsBeforeCreate = await paymentCategoryComponentsPage.countDeleteButtons();

    await paymentCategoryComponentsPage.clickOnCreateButton();

    await promise.all([
      paymentCategoryUpdatePage.setCategoryNameInput('categoryName'),
      paymentCategoryUpdatePage.setCategoryDescriptionInput('categoryDescription'),
      paymentCategoryUpdatePage.categoryTypeSelectLastOption(),
      paymentCategoryUpdatePage.setFileUploadTokenInput('fileUploadToken'),
      paymentCategoryUpdatePage.setCompilationTokenInput('compilationToken'),
      // paymentCategoryUpdatePage.paymentLabelSelectLastOption(),
      // paymentCategoryUpdatePage.placeholderSelectLastOption(),
    ]);

    await paymentCategoryUpdatePage.save();
    expect(await paymentCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await paymentCategoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last PaymentCategory', async () => {
    const nbButtonsBeforeDelete = await paymentCategoryComponentsPage.countDeleteButtons();
    await paymentCategoryComponentsPage.clickOnLastDeleteButton();

    paymentCategoryDeleteDialog = new PaymentCategoryDeleteDialog();
    expect(await paymentCategoryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Payment Category?');
    await paymentCategoryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(paymentCategoryComponentsPage.title), 5000);

    expect(await paymentCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
