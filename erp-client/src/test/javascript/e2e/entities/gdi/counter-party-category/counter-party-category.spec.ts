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

import {
  CounterPartyCategoryComponentsPage,
  CounterPartyCategoryDeleteDialog,
  CounterPartyCategoryUpdatePage,
} from './counter-party-category.page-object';

const expect = chai.expect;

describe('CounterPartyCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let counterPartyCategoryComponentsPage: CounterPartyCategoryComponentsPage;
  let counterPartyCategoryUpdatePage: CounterPartyCategoryUpdatePage;
  let counterPartyCategoryDeleteDialog: CounterPartyCategoryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CounterPartyCategories', async () => {
    await navBarPage.goToEntity('counter-party-category');
    counterPartyCategoryComponentsPage = new CounterPartyCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(counterPartyCategoryComponentsPage.title), 5000);
    expect(await counterPartyCategoryComponentsPage.getTitle()).to.eq('Counter Party Categories');
    await browser.wait(
      ec.or(ec.visibilityOf(counterPartyCategoryComponentsPage.entities), ec.visibilityOf(counterPartyCategoryComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CounterPartyCategory page', async () => {
    await counterPartyCategoryComponentsPage.clickOnCreateButton();
    counterPartyCategoryUpdatePage = new CounterPartyCategoryUpdatePage();
    expect(await counterPartyCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Counter Party Category');
    await counterPartyCategoryUpdatePage.cancel();
  });

  it('should create and save CounterPartyCategories', async () => {
    const nbButtonsBeforeCreate = await counterPartyCategoryComponentsPage.countDeleteButtons();

    await counterPartyCategoryComponentsPage.clickOnCreateButton();

    await promise.all([
      counterPartyCategoryUpdatePage.setCounterpartyCategoryCodeInput('counterpartyCategoryCode'),
      counterPartyCategoryUpdatePage.counterpartyCategoryCodeDetailsSelectLastOption(),
      counterPartyCategoryUpdatePage.setCounterpartyCategoryDescriptionInput('counterpartyCategoryDescription'),
    ]);

    await counterPartyCategoryUpdatePage.save();
    expect(await counterPartyCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await counterPartyCategoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CounterPartyCategory', async () => {
    const nbButtonsBeforeDelete = await counterPartyCategoryComponentsPage.countDeleteButtons();
    await counterPartyCategoryComponentsPage.clickOnLastDeleteButton();

    counterPartyCategoryDeleteDialog = new CounterPartyCategoryDeleteDialog();
    expect(await counterPartyCategoryDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Counter Party Category?');
    await counterPartyCategoryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(counterPartyCategoryComponentsPage.title), 5000);

    expect(await counterPartyCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
