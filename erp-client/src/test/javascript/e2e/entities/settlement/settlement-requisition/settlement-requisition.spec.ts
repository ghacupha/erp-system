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

import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  SettlementRequisitionComponentsPage,
  /* SettlementRequisitionDeleteDialog, */
  SettlementRequisitionUpdatePage,
} from './settlement-requisition.page-object';

const expect = chai.expect;

describe('SettlementRequisition e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let settlementRequisitionComponentsPage: SettlementRequisitionComponentsPage;
  let settlementRequisitionUpdatePage: SettlementRequisitionUpdatePage;
  /* let settlementRequisitionDeleteDialog: SettlementRequisitionDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SettlementRequisitions', async () => {
    await navBarPage.goToEntity('settlement-requisition');
    settlementRequisitionComponentsPage = new SettlementRequisitionComponentsPage();
    await browser.wait(ec.visibilityOf(settlementRequisitionComponentsPage.title), 5000);
    expect(await settlementRequisitionComponentsPage.getTitle()).to.eq('Settlement Requisitions');
    await browser.wait(
      ec.or(ec.visibilityOf(settlementRequisitionComponentsPage.entities), ec.visibilityOf(settlementRequisitionComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SettlementRequisition page', async () => {
    await settlementRequisitionComponentsPage.clickOnCreateButton();
    settlementRequisitionUpdatePage = new SettlementRequisitionUpdatePage();
    expect(await settlementRequisitionUpdatePage.getPageTitle()).to.eq('Create or edit a Settlement Requisition');
    await settlementRequisitionUpdatePage.cancel();
  });

  /* it('should create and save SettlementRequisitions', async () => {
        const nbButtonsBeforeCreate = await settlementRequisitionComponentsPage.countDeleteButtons();

        await settlementRequisitionComponentsPage.clickOnCreateButton();

        await promise.all([
            settlementRequisitionUpdatePage.setDescriptionInput('description'),
            settlementRequisitionUpdatePage.setSerialNumberInput('64c99148-3908-465d-8c4a-e510e3ade974'),
            settlementRequisitionUpdatePage.setTimeOfRequisitionInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            settlementRequisitionUpdatePage.setRequisitionNumberInput('requisitionNumber'),
            settlementRequisitionUpdatePage.setPaymentAmountInput('5'),
            settlementRequisitionUpdatePage.paymentStatusSelectLastOption(),
            settlementRequisitionUpdatePage.settlementCurrencySelectLastOption(),
            settlementRequisitionUpdatePage.currentOwnerSelectLastOption(),
            settlementRequisitionUpdatePage.nativeOwnerSelectLastOption(),
            settlementRequisitionUpdatePage.nativeDepartmentSelectLastOption(),
            settlementRequisitionUpdatePage.billerSelectLastOption(),
            // settlementRequisitionUpdatePage.paymentInvoiceSelectLastOption(),
            // settlementRequisitionUpdatePage.deliveryNoteSelectLastOption(),
            // settlementRequisitionUpdatePage.jobSheetSelectLastOption(),
            // settlementRequisitionUpdatePage.signaturesSelectLastOption(),
            // settlementRequisitionUpdatePage.businessDocumentSelectLastOption(),
            // settlementRequisitionUpdatePage.applicationMappingSelectLastOption(),
            // settlementRequisitionUpdatePage.placeholderSelectLastOption(),
            // settlementRequisitionUpdatePage.settlementSelectLastOption(),
        ]);

        await settlementRequisitionUpdatePage.save();
        expect(await settlementRequisitionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await settlementRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last SettlementRequisition', async () => {
        const nbButtonsBeforeDelete = await settlementRequisitionComponentsPage.countDeleteButtons();
        await settlementRequisitionComponentsPage.clickOnLastDeleteButton();

        settlementRequisitionDeleteDialog = new SettlementRequisitionDeleteDialog();
        expect(await settlementRequisitionDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Settlement Requisition?');
        await settlementRequisitionDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(settlementRequisitionComponentsPage.title), 5000);

        expect(await settlementRequisitionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
