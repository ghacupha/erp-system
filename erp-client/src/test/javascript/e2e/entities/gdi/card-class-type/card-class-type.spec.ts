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

import { CardClassTypeComponentsPage, CardClassTypeDeleteDialog, CardClassTypeUpdatePage } from './card-class-type.page-object';

const expect = chai.expect;

describe('CardClassType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardClassTypeComponentsPage: CardClassTypeComponentsPage;
  let cardClassTypeUpdatePage: CardClassTypeUpdatePage;
  let cardClassTypeDeleteDialog: CardClassTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardClassTypes', async () => {
    await navBarPage.goToEntity('card-class-type');
    cardClassTypeComponentsPage = new CardClassTypeComponentsPage();
    await browser.wait(ec.visibilityOf(cardClassTypeComponentsPage.title), 5000);
    expect(await cardClassTypeComponentsPage.getTitle()).to.eq('Card Class Types');
    await browser.wait(
      ec.or(ec.visibilityOf(cardClassTypeComponentsPage.entities), ec.visibilityOf(cardClassTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardClassType page', async () => {
    await cardClassTypeComponentsPage.clickOnCreateButton();
    cardClassTypeUpdatePage = new CardClassTypeUpdatePage();
    expect(await cardClassTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Card Class Type');
    await cardClassTypeUpdatePage.cancel();
  });

  it('should create and save CardClassTypes', async () => {
    const nbButtonsBeforeCreate = await cardClassTypeComponentsPage.countDeleteButtons();

    await cardClassTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      cardClassTypeUpdatePage.setCardClassTypeCodeInput('cardClassTypeCode'),
      cardClassTypeUpdatePage.setCardClassTypeInput('cardClassType'),
      cardClassTypeUpdatePage.setCardClassDetailsInput('cardClassDetails'),
    ]);

    await cardClassTypeUpdatePage.save();
    expect(await cardClassTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardClassTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CardClassType', async () => {
    const nbButtonsBeforeDelete = await cardClassTypeComponentsPage.countDeleteButtons();
    await cardClassTypeComponentsPage.clickOnLastDeleteButton();

    cardClassTypeDeleteDialog = new CardClassTypeDeleteDialog();
    expect(await cardClassTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Class Type?');
    await cardClassTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardClassTypeComponentsPage.title), 5000);

    expect(await cardClassTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
