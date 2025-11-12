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

import {
  CardFraudInformationComponentsPage,
  CardFraudInformationDeleteDialog,
  CardFraudInformationUpdatePage,
} from './card-fraud-information.page-object';

const expect = chai.expect;

describe('CardFraudInformation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardFraudInformationComponentsPage: CardFraudInformationComponentsPage;
  let cardFraudInformationUpdatePage: CardFraudInformationUpdatePage;
  let cardFraudInformationDeleteDialog: CardFraudInformationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardFraudInformations', async () => {
    await navBarPage.goToEntity('card-fraud-information');
    cardFraudInformationComponentsPage = new CardFraudInformationComponentsPage();
    await browser.wait(ec.visibilityOf(cardFraudInformationComponentsPage.title), 5000);
    expect(await cardFraudInformationComponentsPage.getTitle()).to.eq('Card Fraud Informations');
    await browser.wait(
      ec.or(ec.visibilityOf(cardFraudInformationComponentsPage.entities), ec.visibilityOf(cardFraudInformationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardFraudInformation page', async () => {
    await cardFraudInformationComponentsPage.clickOnCreateButton();
    cardFraudInformationUpdatePage = new CardFraudInformationUpdatePage();
    expect(await cardFraudInformationUpdatePage.getPageTitle()).to.eq('Create or edit a Card Fraud Information');
    await cardFraudInformationUpdatePage.cancel();
  });

  it('should create and save CardFraudInformations', async () => {
    const nbButtonsBeforeCreate = await cardFraudInformationComponentsPage.countDeleteButtons();

    await cardFraudInformationComponentsPage.clickOnCreateButton();

    await promise.all([
      cardFraudInformationUpdatePage.setReportingDateInput('2000-12-31'),
      cardFraudInformationUpdatePage.setTotalNumberOfFraudIncidentsInput('5'),
      cardFraudInformationUpdatePage.setValueOfFraudIncedentsInLCYInput('5'),
    ]);

    await cardFraudInformationUpdatePage.save();
    expect(await cardFraudInformationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardFraudInformationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CardFraudInformation', async () => {
    const nbButtonsBeforeDelete = await cardFraudInformationComponentsPage.countDeleteButtons();
    await cardFraudInformationComponentsPage.clickOnLastDeleteButton();

    cardFraudInformationDeleteDialog = new CardFraudInformationDeleteDialog();
    expect(await cardFraudInformationDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Fraud Information?');
    await cardFraudInformationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardFraudInformationComponentsPage.title), 5000);

    expect(await cardFraudInformationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
