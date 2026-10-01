/*
 * Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
 * Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
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
// Jest scope decision: this config now correctly executes pure-function reducer/BDD specs
// (transform regex was previously broken - '.(tsx)?' never matched '.ts' - and ts-jest wasn't
// pointed at tsconfig.spec.json, so type-checking picked up Cypress's global chai types instead
// of Jest's). Component-level TestBed specs still don't compile: an unrelated transitive import
// (core/tracker/tracker.service.ts's `import * as SockJS from 'sockjs-client'`) requires
// esModuleInterop to be OFF, while dayjs's default import elsewhere requires it ON - the two
// requirements conflict under ts-jest's single compilation pass. Per the project plan's time-box
// rule, this was not chased further. Component-level navigation/routing behavior is verified via
// Cypress specs instead (src/test/javascript/cypress/integration/entity/*-navigation.spec.ts,
// inline-create-workflow.spec.ts), not Jest/TestBed.
const { pathsToModuleNameMapper } = require('ts-jest/utils');
// const esModules = ['ngx-bootstrap', '@ng-select/ng-select', 'zone.js', 'jsdom'].join('|');
const esModules = ['@ng-select/ng-select', 'zone.js', 'jsdom'].join('|');

const {
  compilerOptions: { paths = {}, baseUrl = './' },
} = require('./tsconfig.json');
const environment = require('./webpack/environment');

module.exports = {
  preset: 'jest-preset-angular',
  globals: {
    ...environment,
    'ts-jest': {
      tsconfig: 'tsconfig.spec.json',
    },
  },
  roots: ['<rootDir>', `<rootDir>/${baseUrl}`],
  modulePaths: [`<rootDir>/${baseUrl}`],
  setupFiles: ['jest-date-mock'],
  setupFilesAfterEnv: ['<rootDir>/jest.setup.ts'],
  testEnvironment: 'jsdom',
  cacheDirectory: '<rootDir>/target/jest-cache',
  coverageDirectory: '<rootDir>/target/test-results/',
  moduleNameMapper: pathsToModuleNameMapper(paths, { prefix: `<rootDir>/${baseUrl}/` }),
  reporters: ['default', ['jest-junit', { outputDirectory: '<rootDir>/target/test-results/', outputName: 'TESTS-results-jest.xml' }]],
  testResultsProcessor: 'jest-sonar-reporter',
  testMatch: ['<rootDir>/src/main/webapp/app/**/@(*.)@(spec.ts)'],
  testURL: 'http://localhost/',
  transform: {
    '^.+\\.(js|jsx)$': 'babel-jest',
    '^.+\\.scss$': 'jest-transform-scss',
  },
  transformIgnorePatterns: [`/node_modules/(?!${esModules})`],
};
