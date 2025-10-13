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

import { CardChargesComponentsPage, CardChargesDeleteDialog, CardChargesUpdatePage } from './card-charges.page-object';

const expect = chai.expect;

describe('CardCharges e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardChargesComponentsPage: CardChargesComponentsPage;
  let cardChargesUpdatePage: CardChargesUpdatePage;
  let cardChargesDeleteDialog: CardChargesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardCharges', async () => {
    await navBarPage.goToEntity('card-charges');
    cardChargesComponentsPage = new CardChargesComponentsPage();
    await browser.wait(ec.visibilityOf(cardChargesComponentsPage.title), 5000);
    expect(await cardChargesComponentsPage.getTitle()).to.eq('Card Charges');
    await browser.wait(
      ec.or(ec.visibilityOf(cardChargesComponentsPage.entities), ec.visibilityOf(cardChargesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardCharges page', async () => {
    await cardChargesComponentsPage.clickOnCreateButton();
    cardChargesUpdatePage = new CardChargesUpdatePage();
    expect(await cardChargesUpdatePage.getPageTitle()).to.eq('Create or edit a Card Charges');
    await cardChargesUpdatePage.cancel();
  });

  it('should create and save CardCharges', async () => {
    const nbButtonsBeforeCreate = await cardChargesComponentsPage.countDeleteButtons();

    await cardChargesComponentsPage.clickOnCreateButton();

    await promise.all([
      cardChargesUpdatePage.setCardChargeTypeInput('cardChargeType'),
      cardChargesUpdatePage.setCardChargeTypeNameInput('cardChargeTypeName'),
      cardChargesUpdatePage.setCardChargeDetailsInput('cardChargeDetails'),
    ]);

    await cardChargesUpdatePage.save();
    expect(await cardChargesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardChargesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CardCharges', async () => {
    const nbButtonsBeforeDelete = await cardChargesComponentsPage.countDeleteButtons();
    await cardChargesComponentsPage.clickOnLastDeleteButton();

    cardChargesDeleteDialog = new CardChargesDeleteDialog();
    expect(await cardChargesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Charges?');
    await cardChargesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardChargesComponentsPage.title), 5000);

    expect(await cardChargesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
