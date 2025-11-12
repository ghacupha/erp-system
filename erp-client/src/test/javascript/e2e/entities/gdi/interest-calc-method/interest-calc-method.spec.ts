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

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  InterestCalcMethodComponentsPage,
  InterestCalcMethodDeleteDialog,
  InterestCalcMethodUpdatePage,
} from './interest-calc-method.page-object';

const expect = chai.expect;

describe('InterestCalcMethod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let interestCalcMethodComponentsPage: InterestCalcMethodComponentsPage;
  let interestCalcMethodUpdatePage: InterestCalcMethodUpdatePage;
  let interestCalcMethodDeleteDialog: InterestCalcMethodDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InterestCalcMethods', async () => {
    await navBarPage.goToEntity('interest-calc-method');
    interestCalcMethodComponentsPage = new InterestCalcMethodComponentsPage();
    await browser.wait(ec.visibilityOf(interestCalcMethodComponentsPage.title), 5000);
    expect(await interestCalcMethodComponentsPage.getTitle()).to.eq('Interest Calc Methods');
    await browser.wait(
      ec.or(ec.visibilityOf(interestCalcMethodComponentsPage.entities), ec.visibilityOf(interestCalcMethodComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InterestCalcMethod page', async () => {
    await interestCalcMethodComponentsPage.clickOnCreateButton();
    interestCalcMethodUpdatePage = new InterestCalcMethodUpdatePage();
    expect(await interestCalcMethodUpdatePage.getPageTitle()).to.eq('Create or edit a Interest Calc Method');
    await interestCalcMethodUpdatePage.cancel();
  });

  it('should create and save InterestCalcMethods', async () => {
    const nbButtonsBeforeCreate = await interestCalcMethodComponentsPage.countDeleteButtons();

    await interestCalcMethodComponentsPage.clickOnCreateButton();

    await promise.all([
      interestCalcMethodUpdatePage.setInterestCalculationMethodCodeInput('interestCalculationMethodCode'),
      interestCalcMethodUpdatePage.setInterestCalculationMthodTypeInput('interestCalculationMthodType'),
      interestCalcMethodUpdatePage.setInterestCalculationMethodDetailsInput('interestCalculationMethodDetails'),
    ]);

    await interestCalcMethodUpdatePage.save();
    expect(await interestCalcMethodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await interestCalcMethodComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InterestCalcMethod', async () => {
    const nbButtonsBeforeDelete = await interestCalcMethodComponentsPage.countDeleteButtons();
    await interestCalcMethodComponentsPage.clickOnLastDeleteButton();

    interestCalcMethodDeleteDialog = new InterestCalcMethodDeleteDialog();
    expect(await interestCalcMethodDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Interest Calc Method?');
    await interestCalcMethodDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(interestCalcMethodComponentsPage.title), 5000);

    expect(await interestCalcMethodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
