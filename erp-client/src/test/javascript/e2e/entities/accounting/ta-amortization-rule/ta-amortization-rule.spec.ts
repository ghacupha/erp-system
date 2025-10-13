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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TAAmortizationRuleComponentsPage,
  /* TAAmortizationRuleDeleteDialog, */
  TAAmortizationRuleUpdatePage,
} from './ta-amortization-rule.page-object';

const expect = chai.expect;

describe('TAAmortizationRule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tAAmortizationRuleComponentsPage: TAAmortizationRuleComponentsPage;
  let tAAmortizationRuleUpdatePage: TAAmortizationRuleUpdatePage;
  /* let tAAmortizationRuleDeleteDialog: TAAmortizationRuleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TAAmortizationRules', async () => {
    await navBarPage.goToEntity('ta-amortization-rule');
    tAAmortizationRuleComponentsPage = new TAAmortizationRuleComponentsPage();
    await browser.wait(ec.visibilityOf(tAAmortizationRuleComponentsPage.title), 5000);
    expect(await tAAmortizationRuleComponentsPage.getTitle()).to.eq('TA Amortization Rules');
    await browser.wait(
      ec.or(ec.visibilityOf(tAAmortizationRuleComponentsPage.entities), ec.visibilityOf(tAAmortizationRuleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TAAmortizationRule page', async () => {
    await tAAmortizationRuleComponentsPage.clickOnCreateButton();
    tAAmortizationRuleUpdatePage = new TAAmortizationRuleUpdatePage();
    expect(await tAAmortizationRuleUpdatePage.getPageTitle()).to.eq('Create or edit a TA Amortization Rule');
    await tAAmortizationRuleUpdatePage.cancel();
  });

  /* it('should create and save TAAmortizationRules', async () => {
        const nbButtonsBeforeCreate = await tAAmortizationRuleComponentsPage.countDeleteButtons();

        await tAAmortizationRuleComponentsPage.clickOnCreateButton();

        await promise.all([
            tAAmortizationRuleUpdatePage.setNameInput('name'),
            tAAmortizationRuleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            tAAmortizationRuleUpdatePage.leaseContractSelectLastOption(),
            tAAmortizationRuleUpdatePage.debitSelectLastOption(),
            tAAmortizationRuleUpdatePage.creditSelectLastOption(),
            // tAAmortizationRuleUpdatePage.placeholderSelectLastOption(),
        ]);

        await tAAmortizationRuleUpdatePage.save();
        expect(await tAAmortizationRuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tAAmortizationRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TAAmortizationRule', async () => {
        const nbButtonsBeforeDelete = await tAAmortizationRuleComponentsPage.countDeleteButtons();
        await tAAmortizationRuleComponentsPage.clickOnLastDeleteButton();

        tAAmortizationRuleDeleteDialog = new TAAmortizationRuleDeleteDialog();
        expect(await tAAmortizationRuleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this TA Amortization Rule?');
        await tAAmortizationRuleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(tAAmortizationRuleComponentsPage.title), 5000);

        expect(await tAAmortizationRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
