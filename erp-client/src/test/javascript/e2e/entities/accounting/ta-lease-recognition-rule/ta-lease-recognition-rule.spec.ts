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
  TALeaseRecognitionRuleComponentsPage,
  /* TALeaseRecognitionRuleDeleteDialog, */
  TALeaseRecognitionRuleUpdatePage,
} from './ta-lease-recognition-rule.page-object';

const expect = chai.expect;

describe('TALeaseRecognitionRule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tALeaseRecognitionRuleComponentsPage: TALeaseRecognitionRuleComponentsPage;
  let tALeaseRecognitionRuleUpdatePage: TALeaseRecognitionRuleUpdatePage;
  /* let tALeaseRecognitionRuleDeleteDialog: TALeaseRecognitionRuleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TALeaseRecognitionRules', async () => {
    await navBarPage.goToEntity('ta-lease-recognition-rule');
    tALeaseRecognitionRuleComponentsPage = new TALeaseRecognitionRuleComponentsPage();
    await browser.wait(ec.visibilityOf(tALeaseRecognitionRuleComponentsPage.title), 5000);
    expect(await tALeaseRecognitionRuleComponentsPage.getTitle()).to.eq('TA Lease Recognition Rules');
    await browser.wait(
      ec.or(ec.visibilityOf(tALeaseRecognitionRuleComponentsPage.entities), ec.visibilityOf(tALeaseRecognitionRuleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TALeaseRecognitionRule page', async () => {
    await tALeaseRecognitionRuleComponentsPage.clickOnCreateButton();
    tALeaseRecognitionRuleUpdatePage = new TALeaseRecognitionRuleUpdatePage();
    expect(await tALeaseRecognitionRuleUpdatePage.getPageTitle()).to.eq('Create or edit a TA Lease Recognition Rule');
    await tALeaseRecognitionRuleUpdatePage.cancel();
  });

  /* it('should create and save TALeaseRecognitionRules', async () => {
        const nbButtonsBeforeCreate = await tALeaseRecognitionRuleComponentsPage.countDeleteButtons();

        await tALeaseRecognitionRuleComponentsPage.clickOnCreateButton();

        await promise.all([
            tALeaseRecognitionRuleUpdatePage.setNameInput('name'),
            tALeaseRecognitionRuleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            tALeaseRecognitionRuleUpdatePage.leaseContractSelectLastOption(),
            tALeaseRecognitionRuleUpdatePage.debitSelectLastOption(),
            tALeaseRecognitionRuleUpdatePage.creditSelectLastOption(),
            // tALeaseRecognitionRuleUpdatePage.placeholderSelectLastOption(),
        ]);

        await tALeaseRecognitionRuleUpdatePage.save();
        expect(await tALeaseRecognitionRuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tALeaseRecognitionRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TALeaseRecognitionRule', async () => {
        const nbButtonsBeforeDelete = await tALeaseRecognitionRuleComponentsPage.countDeleteButtons();
        await tALeaseRecognitionRuleComponentsPage.clickOnLastDeleteButton();

        tALeaseRecognitionRuleDeleteDialog = new TALeaseRecognitionRuleDeleteDialog();
        expect(await tALeaseRecognitionRuleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this TA Lease Recognition Rule?');
        await tALeaseRecognitionRuleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(tALeaseRecognitionRuleComponentsPage.title), 5000);

        expect(await tALeaseRecognitionRuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
