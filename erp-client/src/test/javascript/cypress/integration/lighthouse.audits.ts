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

describe('Lighthouse Audits', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('homepage', () => {
    const customThresholds = {
      performance: 80,
      accessibility: 90,
      seo: 90,
      'best-practices': 90,
      // If you have enabled PWA you should set this threshold to 100
      pwa: 0,
    };

    const desktopConfig = {
      extends: 'lighthouse:default',
      formFactor: 'desktop',
      // Change the CPU slowdown multiplier to emulate different kind of devices
      // See https://github.com/GoogleChrome/lighthouse/blob/master/docs/throttling.md#cpu-throttling for details
      throttling: {
        cpuSlowdownMultiplier: 1,
      },
      screenEmulation: { disabled: true },
    };
    cy.lighthouse(customThresholds, desktopConfig);
  });
});
