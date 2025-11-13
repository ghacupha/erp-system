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

import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IIsoCountryCode } from 'app/entities/gdi/iso-country-code/iso-country-code.model';

export interface IPerformanceOfForeignSubsidiaries {
  id?: number;
  subsidiaryName?: string;
  reportingDate?: dayjs.Dayjs;
  subsidiaryId?: string;
  grossLoansAmount?: number;
  grossNPALoanAmount?: number;
  grossAssetsAmount?: number;
  grossDepositsAmount?: number;
  profitBeforeTax?: number;
  totalCapitalAdequacyRatio?: number;
  liquidityRatio?: number;
  generalProvisions?: number;
  specificProvisions?: number;
  interestInSuspenseAmount?: number;
  totalNumberOfStaff?: number;
  numberOfBranches?: number;
  bankCode?: IInstitutionCode;
  subsidiaryCountryCode?: IIsoCountryCode;
}

export class PerformanceOfForeignSubsidiaries implements IPerformanceOfForeignSubsidiaries {
  constructor(
    public id?: number,
    public subsidiaryName?: string,
    public reportingDate?: dayjs.Dayjs,
    public subsidiaryId?: string,
    public grossLoansAmount?: number,
    public grossNPALoanAmount?: number,
    public grossAssetsAmount?: number,
    public grossDepositsAmount?: number,
    public profitBeforeTax?: number,
    public totalCapitalAdequacyRatio?: number,
    public liquidityRatio?: number,
    public generalProvisions?: number,
    public specificProvisions?: number,
    public interestInSuspenseAmount?: number,
    public totalNumberOfStaff?: number,
    public numberOfBranches?: number,
    public bankCode?: IInstitutionCode,
    public subsidiaryCountryCode?: IIsoCountryCode
  ) {}
}

export function getPerformanceOfForeignSubsidiariesIdentifier(
  performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries
): number | undefined {
  return performanceOfForeignSubsidiaries.id;
}
