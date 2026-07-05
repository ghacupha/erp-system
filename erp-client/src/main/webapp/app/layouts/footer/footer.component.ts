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

import { Component } from '@angular/core';
import { VERSION as AngularVersion } from '@angular/core';
import { versionInfo } from '../../../version-info';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
})
export class FooterComponent {
  version = AngularVersion.full;

  // Prefer the SYSTEM_BUILD env var injected by webpack DefinePlugin (from erp-client/.env
  // or a CI/system env var). Falls back to the hash in git-version.json when not set.
  systemBuild = (SYSTEM_BUILD || versionInfo.hash).substring(0, 7);

  clientVersion = '1.7.8';

  serverVersion = '1.8.1';
}
