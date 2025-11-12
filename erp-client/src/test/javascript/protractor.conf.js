/*
 * Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
 * Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
exports.config = {
  allScriptsTimeout: 20000,

  specs: [
    './e2e/account/**/*.spec.ts',
    './e2e/admin/**/*.spec.ts',
    './e2e/entities/**/*.spec.ts',
    /* jhipster-needle-add-protractor-tests - JHipster will add protractors tests here */
  ],

  capabilities: {
    browserName: 'chrome',
    chromeOptions: {
      args: process.env.JHI_E2E_HEADLESS
        ? ['--headless', '--disable-gpu', '--window-size=800,600']
        : ['--disable-gpu', '--window-size=800,600'],
    },
  },

  directConnect: true,

  baseUrl: 'http://localhost:8980/',

  framework: 'mocha',

  SELENIUM_PROMISE_MANAGER: false,

  mochaOpts: {
    reporter: 'spec',
    slow: 3000,
    ui: 'bdd',
    timeout: 720000,
  },

  beforeLaunch: function () {
    require('ts-node').register({
      project: 'tsconfig.e2e.json',
    });
  },

  onPrepare: function () {
    browser.driver.manage().window().setSize(1280, 1024);
    // Disable animations
    // @ts-ignore
    browser.executeScript('document.body.className += " notransition";');
    const chai = require('chai');
    const chaiAsPromised = require('chai-as-promised');
    chai.use(chaiAsPromised);
    const chaiString = require('chai-string');
    chai.use(chaiString);
    // @ts-ignore
    global.chai = chai;
  },

  useAllAngular2AppRoots: true,
};
