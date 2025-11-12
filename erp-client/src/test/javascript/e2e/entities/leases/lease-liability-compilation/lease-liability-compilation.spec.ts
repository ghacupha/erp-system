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

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LeaseLiabilityCompilationComponentsPage,
  LeaseLiabilityCompilationDeleteDialog,
  LeaseLiabilityCompilationUpdatePage,
} from './lease-liability-compilation.page-object';

const expect = chai.expect;

describe('LeaseLiabilityCompilation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityCompilationComponentsPage: LeaseLiabilityCompilationComponentsPage;
  let leaseLiabilityCompilationUpdatePage: LeaseLiabilityCompilationUpdatePage;
  let leaseLiabilityCompilationDeleteDialog: LeaseLiabilityCompilationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityCompilations', async () => {
    await navBarPage.goToEntity('lease-liability-compilation');
    leaseLiabilityCompilationComponentsPage = new LeaseLiabilityCompilationComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityCompilationComponentsPage.title), 5000);
    expect(await leaseLiabilityCompilationComponentsPage.getTitle()).to.eq('Lease Liability Compilations');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityCompilationComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityCompilationComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create LeaseLiabilityCompilation page', async () => {
    await leaseLiabilityCompilationComponentsPage.clickOnCreateButton();
    leaseLiabilityCompilationUpdatePage = new LeaseLiabilityCompilationUpdatePage();
    expect(await leaseLiabilityCompilationUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Liability Compilation');
    await leaseLiabilityCompilationUpdatePage.cancel();
  });

  it('should create and save LeaseLiabilityCompilations', async () => {
    const nbButtonsBeforeCreate = await leaseLiabilityCompilationComponentsPage.countDeleteButtons();

    await leaseLiabilityCompilationComponentsPage.clickOnCreateButton();

    await promise.all([
      leaseLiabilityCompilationUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      leaseLiabilityCompilationUpdatePage.setTimeOfRequestInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      leaseLiabilityCompilationUpdatePage.requestedBySelectLastOption(),
    ]);

    await leaseLiabilityCompilationUpdatePage.save();
    expect(await leaseLiabilityCompilationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leaseLiabilityCompilationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LeaseLiabilityCompilation', async () => {
    const nbButtonsBeforeDelete = await leaseLiabilityCompilationComponentsPage.countDeleteButtons();
    await leaseLiabilityCompilationComponentsPage.clickOnLastDeleteButton();

    leaseLiabilityCompilationDeleteDialog = new LeaseLiabilityCompilationDeleteDialog();
    expect(await leaseLiabilityCompilationDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Lease Liability Compilation?'
    );
    await leaseLiabilityCompilationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(leaseLiabilityCompilationComponentsPage.title), 5000);

    expect(await leaseLiabilityCompilationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
