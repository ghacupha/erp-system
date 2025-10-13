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

import { CardStateComponentsPage, CardStateDeleteDialog, CardStateUpdatePage } from './card-state.page-object';

const expect = chai.expect;

describe('CardState e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardStateComponentsPage: CardStateComponentsPage;
  let cardStateUpdatePage: CardStateUpdatePage;
  let cardStateDeleteDialog: CardStateDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardStates', async () => {
    await navBarPage.goToEntity('card-state');
    cardStateComponentsPage = new CardStateComponentsPage();
    await browser.wait(ec.visibilityOf(cardStateComponentsPage.title), 5000);
    expect(await cardStateComponentsPage.getTitle()).to.eq('Card States');
    await browser.wait(ec.or(ec.visibilityOf(cardStateComponentsPage.entities), ec.visibilityOf(cardStateComponentsPage.noResult)), 1000);
  });

  it('should load create CardState page', async () => {
    await cardStateComponentsPage.clickOnCreateButton();
    cardStateUpdatePage = new CardStateUpdatePage();
    expect(await cardStateUpdatePage.getPageTitle()).to.eq('Create or edit a Card State');
    await cardStateUpdatePage.cancel();
  });

  it('should create and save CardStates', async () => {
    const nbButtonsBeforeCreate = await cardStateComponentsPage.countDeleteButtons();

    await cardStateComponentsPage.clickOnCreateButton();

    await promise.all([
      cardStateUpdatePage.cardStateFlagSelectLastOption(),
      cardStateUpdatePage.setCardStateFlagDetailsInput('cardStateFlagDetails'),
      cardStateUpdatePage.setCardStateFlagDescriptionInput('cardStateFlagDescription'),
    ]);

    await cardStateUpdatePage.save();
    expect(await cardStateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardStateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CardState', async () => {
    const nbButtonsBeforeDelete = await cardStateComponentsPage.countDeleteButtons();
    await cardStateComponentsPage.clickOnLastDeleteButton();

    cardStateDeleteDialog = new CardStateDeleteDialog();
    expect(await cardStateDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card State?');
    await cardStateDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardStateComponentsPage.title), 5000);

    expect(await cardStateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
