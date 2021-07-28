///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
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

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
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
    ]);

    expect(await paymentCategoryUpdatePage.getCategoryNameInput()).to.eq(
      'categoryName',
      'Expected CategoryName value to be equals to categoryName'
    );
    expect(await paymentCategoryUpdatePage.getCategoryDescriptionInput()).to.eq(
      'categoryDescription',
      'Expected CategoryDescription value to be equals to categoryDescription'
    );

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

    expect(await paymentCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
