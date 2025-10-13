/*
 * Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
const { pathsToModuleNameMapper } = require('ts-jest/utils');
// const esModules = ['ngx-bootstrap', '@ng-select/ng-select', 'zone.js', 'jsdom'].join('|');
const esModules = ['@ng-select/ng-select', 'zone.js', 'jsdom'].join('|');

const {
  compilerOptions: { paths = {}, baseUrl = './' },
} = require('./tsconfig.json');
const environment = require('./webpack/environment');

module.exports = {
  globals: {
    ...environment,
  },
  roots: ['<rootDir>', `<rootDir>/${baseUrl}`],
  modulePaths: [`<rootDir>/${baseUrl}`],
  setupFiles: ['jest-date-mock'],
  cacheDirectory: '<rootDir>/target/jest-cache',
  coverageDirectory: '<rootDir>/target/test-results/',
  moduleNameMapper: pathsToModuleNameMapper(paths, { prefix: `<rootDir>/${baseUrl}/` }),
  reporters: ['default', ['jest-junit', { outputDirectory: '<rootDir>/target/test-results/', outputName: 'TESTS-results-jest.xml' }]],
  testResultsProcessor: 'jest-sonar-reporter',
  testMatch: ['<rootDir>/src/main/webapp/app/**/@(*.)@(spec.ts)'],
  testURL: 'http://localhost/',
  // transform: {
  //   '^.+\\.(ts|tsx)?$': 'ts-jest',
  //   "^.+\\.(js|jsx)$": "babel-jest",
  //   "^.+\\.scss$": "jest-transform-scss",
  // },
  transform: {
      '^.+\\.(tsx)?$': 'ts-jest',
      "^.+\\.(js|jsx)$": "babel-jest",
      "^.+\\.scss$": "jest-transform-scss",
  },
  transformIgnorePatterns: [`/node_modules/(?!${esModules})`],
};
