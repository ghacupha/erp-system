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

import { browser, element, by, ExpectedConditions as ec } from 'protractor';

import { NavBarPage, SignInPage, PasswordPage, SettingsPage } from '../page-objects/jhi-page-objects';

const expect = chai.expect;

describe('account', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';
  let passwordPage: PasswordPage;
  let settingsPage: SettingsPage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage(true);
  });

  it('should fail to login with bad password', async () => {
    const expect1 = 'Welcome, Java Hipster!';
    const value1 = await element(by.css('h1 > span')).getText();
    expect(value1).to.eq(expect1);
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, 'foo');

    const expect2 = 'Failed to sign in! Please check your credentials and try again.';
    const value2 = await element(by.css('.alert-danger')).getText();
    expect(value2).to.eq(expect2);
  });

  it('should login successfully with admin account', async () => {
    await browser.get('/');
    signInPage = await navBarPage.getSignInPage();

    const expect1 = 'Login';
    const value1 = await element(by.className('username-label')).getText();
    expect(value1).to.eq(expect1);
    await signInPage.autoSignInUsing(username, password);

    const expect2 = 'You are logged in as user "admin".';
    await browser.wait(ec.visibilityOf(element(by.id('home-logged-message'))));
    const value2 = await element(by.id('home-logged-message')).getText();
    expect(value2).to.eq(expect2);
  });

  it('should be able to update settings', async () => {
    settingsPage = await navBarPage.getSettingsPage();

    const expect1 = `User settings for [${username}]`;
    const value1 = await settingsPage.getTitle();
    expect(value1).to.eq(expect1);
    await settingsPage.save();

    const expect2 = 'Settings saved!';
    const alert = element(by.css('.alert-success'));
    const value2 = await alert.getText();
    expect(value2).to.eq(expect2);
  });

  it('should fail to update password when using incorrect current password', async () => {
    passwordPage = await navBarPage.getPasswordPage();

    expect(await passwordPage.getTitle()).to.eq(`Password for [${username}]`);

    await passwordPage.setCurrentPassword('wrong_current_password');
    await passwordPage.setPassword('new_password');
    await passwordPage.setConfirmPassword('new_password');
    await passwordPage.save();

    const expect2 = 'An error has occurred! The password could not be changed.';
    const alert = element(by.css('.alert-danger'));
    const value2 = await alert.getText();
    expect(value2).to.eq(expect2);
    settingsPage = await navBarPage.getSettingsPage();
  });

  it('should be able to update password', async () => {
    passwordPage = await navBarPage.getPasswordPage();

    expect(await passwordPage.getTitle()).to.eq(`Password for [${username}]`);

    await passwordPage.setCurrentPassword(password);
    await passwordPage.setPassword('newpassword');
    await passwordPage.setConfirmPassword('newpassword');
    await passwordPage.save();

    const successMsg = 'Password changed!';
    const alert = element(by.css('.alert-success'));
    const alertValue = await alert.getText();
    expect(alertValue).to.eq(successMsg);
    await navBarPage.autoSignOut();
    await navBarPage.goToSignInPage();
    await signInPage.autoSignInUsing(username, 'newpassword');

    // change back to default
    await navBarPage.goToPasswordMenu();
    await passwordPage.setCurrentPassword('newpassword');
    await passwordPage.setPassword(password);
    await passwordPage.setConfirmPassword(password);
    await passwordPage.save();

    // wait for success message
    const alertValue2 = await alert.getText();
    expect(alertValue2).to.eq(successMsg);
  });

  it('should navigate to 404 not found error page on non existing route and show user own navbar if valid authentication exists', async () => {
    // don't sign out and refresh page with non existing route
    await browser.get('/this-is-link-to-non-existing-page');

    // should navigate to 404 not found page
    const url = await browser.getCurrentUrl();
    expect(url).to.endWith('404');

    // as user is admin then should show admin menu to user
    await navBarPage.clickOnAdminMenu();
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
