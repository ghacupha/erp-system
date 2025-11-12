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

import { CardCategoryTypeComponentsPage, CardCategoryTypeDeleteDialog, CardCategoryTypeUpdatePage } from './card-category-type.page-object';

const expect = chai.expect;

describe('CardCategoryType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardCategoryTypeComponentsPage: CardCategoryTypeComponentsPage;
  let cardCategoryTypeUpdatePage: CardCategoryTypeUpdatePage;
  let cardCategoryTypeDeleteDialog: CardCategoryTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardCategoryTypes', async () => {
    await navBarPage.goToEntity('card-category-type');
    cardCategoryTypeComponentsPage = new CardCategoryTypeComponentsPage();
    await browser.wait(ec.visibilityOf(cardCategoryTypeComponentsPage.title), 5000);
    expect(await cardCategoryTypeComponentsPage.getTitle()).to.eq('Card Category Types');
    await browser.wait(
      ec.or(ec.visibilityOf(cardCategoryTypeComponentsPage.entities), ec.visibilityOf(cardCategoryTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardCategoryType page', async () => {
    await cardCategoryTypeComponentsPage.clickOnCreateButton();
    cardCategoryTypeUpdatePage = new CardCategoryTypeUpdatePage();
    expect(await cardCategoryTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Card Category Type');
    await cardCategoryTypeUpdatePage.cancel();
  });

  it('should create and save CardCategoryTypes', async () => {
    const nbButtonsBeforeCreate = await cardCategoryTypeComponentsPage.countDeleteButtons();

    await cardCategoryTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      cardCategoryTypeUpdatePage.cardCategoryFlagSelectLastOption(),
      cardCategoryTypeUpdatePage.setCardCategoryDescriptionInput('cardCategoryDescription'),
      cardCategoryTypeUpdatePage.setCardCategoryDetailsInput('cardCategoryDetails'),
    ]);

    await cardCategoryTypeUpdatePage.save();
    expect(await cardCategoryTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardCategoryTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CardCategoryType', async () => {
    const nbButtonsBeforeDelete = await cardCategoryTypeComponentsPage.countDeleteButtons();
    await cardCategoryTypeComponentsPage.clickOnLastDeleteButton();

    cardCategoryTypeDeleteDialog = new CardCategoryTypeDeleteDialog();
    expect(await cardCategoryTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Category Type?');
    await cardCategoryTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardCategoryTypeComponentsPage.title), 5000);

    expect(await cardCategoryTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
