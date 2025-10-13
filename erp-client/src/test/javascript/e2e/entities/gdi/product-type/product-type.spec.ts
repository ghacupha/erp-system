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

import { ProductTypeComponentsPage, ProductTypeDeleteDialog, ProductTypeUpdatePage } from './product-type.page-object';

const expect = chai.expect;

describe('ProductType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productTypeComponentsPage: ProductTypeComponentsPage;
  let productTypeUpdatePage: ProductTypeUpdatePage;
  let productTypeDeleteDialog: ProductTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProductTypes', async () => {
    await navBarPage.goToEntity('product-type');
    productTypeComponentsPage = new ProductTypeComponentsPage();
    await browser.wait(ec.visibilityOf(productTypeComponentsPage.title), 5000);
    expect(await productTypeComponentsPage.getTitle()).to.eq('Product Types');
    await browser.wait(
      ec.or(ec.visibilityOf(productTypeComponentsPage.entities), ec.visibilityOf(productTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ProductType page', async () => {
    await productTypeComponentsPage.clickOnCreateButton();
    productTypeUpdatePage = new ProductTypeUpdatePage();
    expect(await productTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Product Type');
    await productTypeUpdatePage.cancel();
  });

  it('should create and save ProductTypes', async () => {
    const nbButtonsBeforeCreate = await productTypeComponentsPage.countDeleteButtons();

    await productTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      productTypeUpdatePage.setProductCodeInput('productCode'),
      productTypeUpdatePage.setProductTypeInput('productType'),
      productTypeUpdatePage.setProductTypeDescriptionInput('productTypeDescription'),
    ]);

    await productTypeUpdatePage.save();
    expect(await productTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await productTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ProductType', async () => {
    const nbButtonsBeforeDelete = await productTypeComponentsPage.countDeleteButtons();
    await productTypeComponentsPage.clickOnLastDeleteButton();

    productTypeDeleteDialog = new ProductTypeDeleteDialog();
    expect(await productTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Product Type?');
    await productTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(productTypeComponentsPage.title), 5000);

    expect(await productTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
