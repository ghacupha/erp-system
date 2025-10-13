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
  PrepaymentMarshallingComponentsPage,
  /* PrepaymentMarshallingDeleteDialog, */
  PrepaymentMarshallingUpdatePage,
} from './prepayment-marshalling.page-object';

const expect = chai.expect;

describe('PrepaymentMarshalling e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentMarshallingComponentsPage: PrepaymentMarshallingComponentsPage;
  let prepaymentMarshallingUpdatePage: PrepaymentMarshallingUpdatePage;
  /* let prepaymentMarshallingDeleteDialog: PrepaymentMarshallingDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentMarshallings', async () => {
    await navBarPage.goToEntity('prepayment-marshalling');
    prepaymentMarshallingComponentsPage = new PrepaymentMarshallingComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentMarshallingComponentsPage.title), 5000);
    expect(await prepaymentMarshallingComponentsPage.getTitle()).to.eq('Prepayment Marshallings');
    await browser.wait(
      ec.or(ec.visibilityOf(prepaymentMarshallingComponentsPage.entities), ec.visibilityOf(prepaymentMarshallingComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PrepaymentMarshalling page', async () => {
    await prepaymentMarshallingComponentsPage.clickOnCreateButton();
    prepaymentMarshallingUpdatePage = new PrepaymentMarshallingUpdatePage();
    expect(await prepaymentMarshallingUpdatePage.getPageTitle()).to.eq('Create or edit a Prepayment Marshalling');
    await prepaymentMarshallingUpdatePage.cancel();
  });

  /* it('should create and save PrepaymentMarshallings', async () => {
        const nbButtonsBeforeCreate = await prepaymentMarshallingComponentsPage.countDeleteButtons();

        await prepaymentMarshallingComponentsPage.clickOnCreateButton();

        await promise.all([
            prepaymentMarshallingUpdatePage.getInactiveInput().click(),
            prepaymentMarshallingUpdatePage.setAmortizationPeriodsInput('5'),
            prepaymentMarshallingUpdatePage.getProcessedInput().click(),
            prepaymentMarshallingUpdatePage.prepaymentAccountSelectLastOption(),
            prepaymentMarshallingUpdatePage.firstAmortizationPeriodSelectLastOption(),
            // prepaymentMarshallingUpdatePage.placeholderSelectLastOption(),
            prepaymentMarshallingUpdatePage.firstFiscalMonthSelectLastOption(),
            prepaymentMarshallingUpdatePage.lastFiscalMonthSelectLastOption(),
        ]);

        await prepaymentMarshallingUpdatePage.save();
        expect(await prepaymentMarshallingUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await prepaymentMarshallingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last PrepaymentMarshalling', async () => {
        const nbButtonsBeforeDelete = await prepaymentMarshallingComponentsPage.countDeleteButtons();
        await prepaymentMarshallingComponentsPage.clickOnLastDeleteButton();

        prepaymentMarshallingDeleteDialog = new PrepaymentMarshallingDeleteDialog();
        expect(await prepaymentMarshallingDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Prepayment Marshalling?');
        await prepaymentMarshallingDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(prepaymentMarshallingComponentsPage.title), 5000);

        expect(await prepaymentMarshallingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
