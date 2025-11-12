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
  CardUsageInformationComponentsPage,
  /* CardUsageInformationDeleteDialog, */
  CardUsageInformationUpdatePage,
} from './card-usage-information.page-object';

const expect = chai.expect;

describe('CardUsageInformation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardUsageInformationComponentsPage: CardUsageInformationComponentsPage;
  let cardUsageInformationUpdatePage: CardUsageInformationUpdatePage;
  /* let cardUsageInformationDeleteDialog: CardUsageInformationDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardUsageInformations', async () => {
    await navBarPage.goToEntity('card-usage-information');
    cardUsageInformationComponentsPage = new CardUsageInformationComponentsPage();
    await browser.wait(ec.visibilityOf(cardUsageInformationComponentsPage.title), 5000);
    expect(await cardUsageInformationComponentsPage.getTitle()).to.eq('Card Usage Informations');
    await browser.wait(
      ec.or(ec.visibilityOf(cardUsageInformationComponentsPage.entities), ec.visibilityOf(cardUsageInformationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardUsageInformation page', async () => {
    await cardUsageInformationComponentsPage.clickOnCreateButton();
    cardUsageInformationUpdatePage = new CardUsageInformationUpdatePage();
    expect(await cardUsageInformationUpdatePage.getPageTitle()).to.eq('Create or edit a Card Usage Information');
    await cardUsageInformationUpdatePage.cancel();
  });

  /* it('should create and save CardUsageInformations', async () => {
        const nbButtonsBeforeCreate = await cardUsageInformationComponentsPage.countDeleteButtons();

        await cardUsageInformationComponentsPage.clickOnCreateButton();

        await promise.all([
            cardUsageInformationUpdatePage.setReportingDateInput('2000-12-31'),
            cardUsageInformationUpdatePage.setTotalNumberOfLiveCardsInput('5'),
            cardUsageInformationUpdatePage.setTotalActiveCardsInput('5'),
            cardUsageInformationUpdatePage.setTotalNumberOfTransactionsDoneInput('5'),
            cardUsageInformationUpdatePage.setTotalValueOfTransactionsDoneInLCYInput('5'),
            cardUsageInformationUpdatePage.bankCodeSelectLastOption(),
            cardUsageInformationUpdatePage.cardTypeSelectLastOption(),
            cardUsageInformationUpdatePage.cardBrandSelectLastOption(),
            cardUsageInformationUpdatePage.cardCategoryTypeSelectLastOption(),
            cardUsageInformationUpdatePage.transactionTypeSelectLastOption(),
            cardUsageInformationUpdatePage.channelTypeSelectLastOption(),
            cardUsageInformationUpdatePage.cardStateSelectLastOption(),
        ]);

        await cardUsageInformationUpdatePage.save();
        expect(await cardUsageInformationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await cardUsageInformationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last CardUsageInformation', async () => {
        const nbButtonsBeforeDelete = await cardUsageInformationComponentsPage.countDeleteButtons();
        await cardUsageInformationComponentsPage.clickOnLastDeleteButton();

        cardUsageInformationDeleteDialog = new CardUsageInformationDeleteDialog();
        expect(await cardUsageInformationDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Card Usage Information?');
        await cardUsageInformationDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(cardUsageInformationComponentsPage.title), 5000);

        expect(await cardUsageInformationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
