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
  CreditCardOwnershipComponentsPage,
  CreditCardOwnershipDeleteDialog,
  CreditCardOwnershipUpdatePage,
} from './credit-card-ownership.page-object';

const expect = chai.expect;

describe('CreditCardOwnership e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let creditCardOwnershipComponentsPage: CreditCardOwnershipComponentsPage;
  let creditCardOwnershipUpdatePage: CreditCardOwnershipUpdatePage;
  let creditCardOwnershipDeleteDialog: CreditCardOwnershipDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CreditCardOwnerships', async () => {
    await navBarPage.goToEntity('credit-card-ownership');
    creditCardOwnershipComponentsPage = new CreditCardOwnershipComponentsPage();
    await browser.wait(ec.visibilityOf(creditCardOwnershipComponentsPage.title), 5000);
    expect(await creditCardOwnershipComponentsPage.getTitle()).to.eq('Credit Card Ownerships');
    await browser.wait(
      ec.or(ec.visibilityOf(creditCardOwnershipComponentsPage.entities), ec.visibilityOf(creditCardOwnershipComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CreditCardOwnership page', async () => {
    await creditCardOwnershipComponentsPage.clickOnCreateButton();
    creditCardOwnershipUpdatePage = new CreditCardOwnershipUpdatePage();
    expect(await creditCardOwnershipUpdatePage.getPageTitle()).to.eq('Create or edit a Credit Card Ownership');
    await creditCardOwnershipUpdatePage.cancel();
  });

  it('should create and save CreditCardOwnerships', async () => {
    const nbButtonsBeforeCreate = await creditCardOwnershipComponentsPage.countDeleteButtons();

    await creditCardOwnershipComponentsPage.clickOnCreateButton();

    await promise.all([
      creditCardOwnershipUpdatePage.setCreditCardOwnershipCategoryCodeInput('creditCardOwnershipCategoryCode'),
      creditCardOwnershipUpdatePage.creditCardOwnershipCategoryTypeSelectLastOption(),
      creditCardOwnershipUpdatePage.setDescriptionInput('description'),
    ]);

    await creditCardOwnershipUpdatePage.save();
    expect(await creditCardOwnershipUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await creditCardOwnershipComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CreditCardOwnership', async () => {
    const nbButtonsBeforeDelete = await creditCardOwnershipComponentsPage.countDeleteButtons();
    await creditCardOwnershipComponentsPage.clickOnLastDeleteButton();

    creditCardOwnershipDeleteDialog = new CreditCardOwnershipDeleteDialog();
    expect(await creditCardOwnershipDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Credit Card Ownership?');
    await creditCardOwnershipDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(creditCardOwnershipComponentsPage.title), 5000);

    expect(await creditCardOwnershipComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
