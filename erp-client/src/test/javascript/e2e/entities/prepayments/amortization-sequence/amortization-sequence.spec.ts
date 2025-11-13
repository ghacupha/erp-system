///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
  AmortizationSequenceComponentsPage,
  /* AmortizationSequenceDeleteDialog, */
  AmortizationSequenceUpdatePage,
} from './amortization-sequence.page-object';

const expect = chai.expect;

describe('AmortizationSequence e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let amortizationSequenceComponentsPage: AmortizationSequenceComponentsPage;
  let amortizationSequenceUpdatePage: AmortizationSequenceUpdatePage;
  /* let amortizationSequenceDeleteDialog: AmortizationSequenceDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AmortizationSequences', async () => {
    await navBarPage.goToEntity('amortization-sequence');
    amortizationSequenceComponentsPage = new AmortizationSequenceComponentsPage();
    await browser.wait(ec.visibilityOf(amortizationSequenceComponentsPage.title), 5000);
    expect(await amortizationSequenceComponentsPage.getTitle()).to.eq('Amortization Sequences');
    await browser.wait(
      ec.or(ec.visibilityOf(amortizationSequenceComponentsPage.entities), ec.visibilityOf(amortizationSequenceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AmortizationSequence page', async () => {
    await amortizationSequenceComponentsPage.clickOnCreateButton();
    amortizationSequenceUpdatePage = new AmortizationSequenceUpdatePage();
    expect(await amortizationSequenceUpdatePage.getPageTitle()).to.eq('Create or edit a Amortization Sequence');
    await amortizationSequenceUpdatePage.cancel();
  });

  /* it('should create and save AmortizationSequences', async () => {
        const nbButtonsBeforeCreate = await amortizationSequenceComponentsPage.countDeleteButtons();

        await amortizationSequenceComponentsPage.clickOnCreateButton();

        await promise.all([
            amortizationSequenceUpdatePage.setPrepaymentAccountGuidInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            amortizationSequenceUpdatePage.setRecurrenceGuidInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            amortizationSequenceUpdatePage.setSequenceNumberInput('5'),
            amortizationSequenceUpdatePage.setParticularsInput('particulars'),
            amortizationSequenceUpdatePage.setCurrentAmortizationDateInput('2000-12-31'),
            amortizationSequenceUpdatePage.setPreviousAmortizationDateInput('2000-12-31'),
            amortizationSequenceUpdatePage.setNextAmortizationDateInput('2000-12-31'),
            amortizationSequenceUpdatePage.getIsCommencementSequenceInput().click(),
            amortizationSequenceUpdatePage.getIsTerminalSequenceInput().click(),
            amortizationSequenceUpdatePage.setAmortizationAmountInput('5'),
            amortizationSequenceUpdatePage.setSequenceGuidInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            amortizationSequenceUpdatePage.prepaymentAccountSelectLastOption(),
            amortizationSequenceUpdatePage.amortizationRecurrenceSelectLastOption(),
            // amortizationSequenceUpdatePage.placeholderSelectLastOption(),
            // amortizationSequenceUpdatePage.prepaymentMappingSelectLastOption(),
            // amortizationSequenceUpdatePage.applicationParametersSelectLastOption(),
        ]);

        await amortizationSequenceUpdatePage.save();
        expect(await amortizationSequenceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await amortizationSequenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AmortizationSequence', async () => {
        const nbButtonsBeforeDelete = await amortizationSequenceComponentsPage.countDeleteButtons();
        await amortizationSequenceComponentsPage.clickOnLastDeleteButton();

        amortizationSequenceDeleteDialog = new AmortizationSequenceDeleteDialog();
        expect(await amortizationSequenceDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Amortization Sequence?');
        await amortizationSequenceDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(amortizationSequenceComponentsPage.title), 5000);

        expect(await amortizationSequenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
