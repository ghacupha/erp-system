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
  TARecognitionROURuleComponentsPage,
  /* TARecognitionROURuleDeleteDialog, */
  TARecognitionROURuleUpdatePage,
} from './ta-recognition-rou-rule.page-object';

const expect = chai.expect;

describe('TARecognitionROURule e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tARecognitionROURuleComponentsPage: TARecognitionROURuleComponentsPage;
  let tARecognitionROURuleUpdatePage: TARecognitionROURuleUpdatePage;
  /* let tARecognitionROURuleDeleteDialog: TARecognitionROURuleDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TARecognitionROURules', async () => {
    await navBarPage.goToEntity('ta-recognition-rou-rule');
    tARecognitionROURuleComponentsPage = new TARecognitionROURuleComponentsPage();
    await browser.wait(ec.visibilityOf(tARecognitionROURuleComponentsPage.title), 5000);
    expect(await tARecognitionROURuleComponentsPage.getTitle()).to.eq('TA Recognition ROU Rules');
    await browser.wait(
      ec.or(ec.visibilityOf(tARecognitionROURuleComponentsPage.entities), ec.visibilityOf(tARecognitionROURuleComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TARecognitionROURule page', async () => {
    await tARecognitionROURuleComponentsPage.clickOnCreateButton();
    tARecognitionROURuleUpdatePage = new TARecognitionROURuleUpdatePage();
    expect(await tARecognitionROURuleUpdatePage.getPageTitle()).to.eq('Create or edit a TA Recognition ROU Rule');
    await tARecognitionROURuleUpdatePage.cancel();
  });

  /* it('should create and save TARecognitionROURules', async () => {
        const nbButtonsBeforeCreate = await tARecognitionROURuleComponentsPage.countDeleteButtons();

        await tARecognitionROURuleComponentsPage.clickOnCreateButton();

        await promise.all([
            tARecognitionROURuleUpdatePage.setNameInput('name'),
            tARecognitionROURuleUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            tARecognitionROURuleUpdatePage.leaseContractSelectLastOption(),
            tARecognitionROURuleUpdatePage.debitSelectLastOption(),
            tARecognitionROURuleUpdatePage.creditSelectLastOption(),
            // tARecognitionROURuleUpdatePage.placeholderSelectLastOption(),
        ]);

        await tARecognitionROURuleUpdatePage.save();
        expect(await tARecognitionROURuleUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tARecognitionROURuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TARecognitionROURule', async () => {
        const nbButtonsBeforeDelete = await tARecognitionROURuleComponentsPage.countDeleteButtons();
        await tARecognitionROURuleComponentsPage.clickOnLastDeleteButton();

        tARecognitionROURuleDeleteDialog = new TARecognitionROURuleDeleteDialog();
        expect(await tARecognitionROURuleDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this TA Recognition ROU Rule?');
        await tARecognitionROURuleDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(tARecognitionROURuleComponentsPage.title), 5000);

        expect(await tARecognitionROURuleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
