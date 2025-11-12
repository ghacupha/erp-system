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
  TALeaseInterestAccrualRuleComponentsPage,
  /* TALeaseInterestAccrualRuleDeleteDialog, */
  TALeaseInterestAccrualRuleUpdatePage,
} from './ta-lease-interest-accrual-rule.page-object';

const expect = chai.expect;

describe('TALeaseInterestAccrualRule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tALeaseInterestAccrualRuleComponentsPage: TALeaseInterestAccrualRuleComponentsPage;
  let tALeaseInterestAccrualRuleUpdatePage: TALeaseInterestAccrualRuleUpdatePage;
  /* let tALeaseInterestAccrualRuleDeleteDialog: TALeaseInterestAccrualRuleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TALeaseInterestAccrualRules', async () => {
    await navBarPage.goToEntity('ta-lease-interest-accrual-rule');
    tALeaseInterestAccrualRuleComponentsPage = new TALeaseInterestAccrualRuleComponentsPage();
    await browser.wait(ec.visibilityOf(tALeaseInterestAccrualRuleComponentsPage.title), 5000);
    expect(await tALeaseInterestAccrualRuleComponentsPage.getTitle()).to.eq('TA Lease Interest Accrual Rules');
    await browser.wait(
      ec.or(
        ec.visibilityOf(tALeaseInterestAccrualRuleComponentsPage.entities),
        ec.visibilityOf(tALeaseInterestAccrualRuleComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create TALeaseInterestAccrualRule page', async () => {
    await tALeaseInterestAccrualRuleComponentsPage.clickOnCreateButton();
    tALeaseInterestAccrualRuleUpdatePage = new TALeaseInterestAccrualRuleUpdatePage();
    expect(await tALeaseInterestAccrualRuleUpdatePage.getPageTitle()).to.eq('Create or edit a TA Lease Interest Accrual Rule');
    await tALeaseInterestAccrualRuleUpdatePage.cancel();
  });

  /* it('should create and save TALeaseInterestAccrualRules', async () => {
        const nbButtonsBeforeCreate = await tALeaseInterestAccrualRuleComponentsPage.countDeleteButtons();

        await tALeaseInterestAccrualRuleComponentsPage.clickOnCreateButton();

        await promise.all([
            tALeaseInterestAccrualRuleUpdatePage.setNameInput('name'),
            tALeaseInterestAccrualRuleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            tALeaseInterestAccrualRuleUpdatePage.leaseContractSelectLastOption(),
            tALeaseInterestAccrualRuleUpdatePage.debitSelectLastOption(),
            tALeaseInterestAccrualRuleUpdatePage.creditSelectLastOption(),
            // tALeaseInterestAccrualRuleUpdatePage.placeholderSelectLastOption(),
        ]);

        await tALeaseInterestAccrualRuleUpdatePage.save();
        expect(await tALeaseInterestAccrualRuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tALeaseInterestAccrualRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TALeaseInterestAccrualRule', async () => {
        const nbButtonsBeforeDelete = await tALeaseInterestAccrualRuleComponentsPage.countDeleteButtons();
        await tALeaseInterestAccrualRuleComponentsPage.clickOnLastDeleteButton();

        tALeaseInterestAccrualRuleDeleteDialog = new TALeaseInterestAccrualRuleDeleteDialog();
        expect(await tALeaseInterestAccrualRuleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this TA Lease Interest Accrual Rule?');
        await tALeaseInterestAccrualRuleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(tALeaseInterestAccrualRuleComponentsPage.title), 5000);

        expect(await tALeaseInterestAccrualRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
