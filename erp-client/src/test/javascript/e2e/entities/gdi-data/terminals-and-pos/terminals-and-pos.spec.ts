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
  TerminalsAndPOSComponentsPage,
  /* TerminalsAndPOSDeleteDialog, */
  TerminalsAndPOSUpdatePage,
} from './terminals-and-pos.page-object';

const expect = chai.expect;

describe('TerminalsAndPOS e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let terminalsAndPOSComponentsPage: TerminalsAndPOSComponentsPage;
  let terminalsAndPOSUpdatePage: TerminalsAndPOSUpdatePage;
  /* let terminalsAndPOSDeleteDialog: TerminalsAndPOSDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TerminalsAndPOS', async () => {
    await navBarPage.goToEntity('terminals-and-pos');
    terminalsAndPOSComponentsPage = new TerminalsAndPOSComponentsPage();
    await browser.wait(ec.visibilityOf(terminalsAndPOSComponentsPage.title), 5000);
    expect(await terminalsAndPOSComponentsPage.getTitle()).to.eq('Terminals And POS');
    await browser.wait(
      ec.or(ec.visibilityOf(terminalsAndPOSComponentsPage.entities), ec.visibilityOf(terminalsAndPOSComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TerminalsAndPOS page', async () => {
    await terminalsAndPOSComponentsPage.clickOnCreateButton();
    terminalsAndPOSUpdatePage = new TerminalsAndPOSUpdatePage();
    expect(await terminalsAndPOSUpdatePage.getPageTitle()).to.eq('Create or edit a Terminals And POS');
    await terminalsAndPOSUpdatePage.cancel();
  });

  /* it('should create and save TerminalsAndPOS', async () => {
        const nbButtonsBeforeCreate = await terminalsAndPOSComponentsPage.countDeleteButtons();

        await terminalsAndPOSComponentsPage.clickOnCreateButton();

        await promise.all([
            terminalsAndPOSUpdatePage.setReportingDateInput('2000-12-31'),
            terminalsAndPOSUpdatePage.setTerminalIdInput('terminalId'),
            terminalsAndPOSUpdatePage.setMerchantIdInput('merchantId'),
            terminalsAndPOSUpdatePage.setTerminalNameInput('terminalName'),
            terminalsAndPOSUpdatePage.setTerminalLocationInput('terminalLocation'),
            terminalsAndPOSUpdatePage.setIso6709LatituteInput('5'),
            terminalsAndPOSUpdatePage.setIso6709LongitudeInput('5'),
            terminalsAndPOSUpdatePage.setTerminalOpeningDateInput('2000-12-31'),
            terminalsAndPOSUpdatePage.setTerminalClosureDateInput('2000-12-31'),
            terminalsAndPOSUpdatePage.terminalTypeSelectLastOption(),
            terminalsAndPOSUpdatePage.terminalFunctionalitySelectLastOption(),
            terminalsAndPOSUpdatePage.physicalLocationSelectLastOption(),
            terminalsAndPOSUpdatePage.bankIdSelectLastOption(),
            terminalsAndPOSUpdatePage.branchIdSelectLastOption(),
        ]);

        await terminalsAndPOSUpdatePage.save();
        expect(await terminalsAndPOSUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await terminalsAndPOSComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last TerminalsAndPOS', async () => {
        const nbButtonsBeforeDelete = await terminalsAndPOSComponentsPage.countDeleteButtons();
        await terminalsAndPOSComponentsPage.clickOnLastDeleteButton();

        terminalsAndPOSDeleteDialog = new TerminalsAndPOSDeleteDialog();
        expect(await terminalsAndPOSDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Terminals And POS?');
        await terminalsAndPOSDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(terminalsAndPOSComponentsPage.title), 5000);

        expect(await terminalsAndPOSComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
