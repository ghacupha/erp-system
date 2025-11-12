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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CardIssuerChargesComponentsPage,
  /* CardIssuerChargesDeleteDialog, */
  CardIssuerChargesUpdatePage,
} from './card-issuer-charges.page-object';

const expect = chai.expect;

describe('CardIssuerCharges e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardIssuerChargesComponentsPage: CardIssuerChargesComponentsPage;
  let cardIssuerChargesUpdatePage: CardIssuerChargesUpdatePage;
  /* let cardIssuerChargesDeleteDialog: CardIssuerChargesDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardIssuerCharges', async () => {
    await navBarPage.goToEntity('card-issuer-charges');
    cardIssuerChargesComponentsPage = new CardIssuerChargesComponentsPage();
    await browser.wait(ec.visibilityOf(cardIssuerChargesComponentsPage.title), 5000);
    expect(await cardIssuerChargesComponentsPage.getTitle()).to.eq('Card Issuer Charges');
    await browser.wait(
      ec.or(ec.visibilityOf(cardIssuerChargesComponentsPage.entities), ec.visibilityOf(cardIssuerChargesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardIssuerCharges page', async () => {
    await cardIssuerChargesComponentsPage.clickOnCreateButton();
    cardIssuerChargesUpdatePage = new CardIssuerChargesUpdatePage();
    expect(await cardIssuerChargesUpdatePage.getPageTitle()).to.eq('Create or edit a Card Issuer Charges');
    await cardIssuerChargesUpdatePage.cancel();
  });

  /* it('should create and save CardIssuerCharges', async () => {
        const nbButtonsBeforeCreate = await cardIssuerChargesComponentsPage.countDeleteButtons();

        await cardIssuerChargesComponentsPage.clickOnCreateButton();

        await promise.all([
            cardIssuerChargesUpdatePage.setReportingDateInput('2000-12-31'),
            cardIssuerChargesUpdatePage.setCardFeeChargeInLCYInput('5'),
            cardIssuerChargesUpdatePage.bankCodeSelectLastOption(),
            cardIssuerChargesUpdatePage.cardCategorySelectLastOption(),
            cardIssuerChargesUpdatePage.cardTypeSelectLastOption(),
            cardIssuerChargesUpdatePage.cardBrandSelectLastOption(),
            cardIssuerChargesUpdatePage.cardClassSelectLastOption(),
            cardIssuerChargesUpdatePage.cardChargeTypeSelectLastOption(),
        ]);

        await cardIssuerChargesUpdatePage.save();
        expect(await cardIssuerChargesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await cardIssuerChargesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last CardIssuerCharges', async () => {
        const nbButtonsBeforeDelete = await cardIssuerChargesComponentsPage.countDeleteButtons();
        await cardIssuerChargesComponentsPage.clickOnLastDeleteButton();

        cardIssuerChargesDeleteDialog = new CardIssuerChargesDeleteDialog();
        expect(await cardIssuerChargesDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Card Issuer Charges?');
        await cardIssuerChargesDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(cardIssuerChargesComponentsPage.title), 5000);

        expect(await cardIssuerChargesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
