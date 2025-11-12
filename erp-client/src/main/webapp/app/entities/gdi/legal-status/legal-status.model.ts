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

export interface ILegalStatus {
  id?: number;
  legalStatusCode?: string;
  legalStatusType?: string;
  legalStatusDescription?: string | null;
}

export class LegalStatus implements ILegalStatus {
  constructor(
    public id?: number,
    public legalStatusCode?: string,
    public legalStatusType?: string,
    public legalStatusDescription?: string | null
  ) {}
}

export function getLegalStatusIdentifier(legalStatus: ILegalStatus): number | undefined {
  return legalStatus.id;
}
