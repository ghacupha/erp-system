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
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { ICollateralType } from 'app/entities/gdi/collateral-type/collateral-type.model';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { CollateralInsuredFlagTypes } from 'app/entities/enumerations/collateral-insured-flag-types.model';

export interface ICollateralInformation {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  collateralId?: string;
  loanContractId?: string;
  customerId?: string;
  registrationPropertyNumber?: string | null;
  collateralOMVInCCY?: number;
  collateralFSVInLCY?: number;
  collateralDiscountedValue?: number | null;
  amountCharged?: number;
  collateralDiscountRate?: number | null;
  loanToValueRatio?: number | null;
  nameOfPropertyValuer?: string | null;
  collateralLastValuationDate?: dayjs.Dayjs | null;
  insuredFlag?: CollateralInsuredFlagTypes;
  nameOfInsurer?: string | null;
  amountInsured?: number | null;
  insuranceExpiryDate?: dayjs.Dayjs | null;
  guaranteeInsurers?: string | null;
  bankCode?: IInstitutionCode;
  branchCode?: IBankBranchCode;
  collateralType?: ICollateralType;
  countyCode?: ICountySubCountyCode | null;
}

export class CollateralInformation implements ICollateralInformation {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public collateralId?: string,
    public loanContractId?: string,
    public customerId?: string,
    public registrationPropertyNumber?: string | null,
    public collateralOMVInCCY?: number,
    public collateralFSVInLCY?: number,
    public collateralDiscountedValue?: number | null,
    public amountCharged?: number,
    public collateralDiscountRate?: number | null,
    public loanToValueRatio?: number | null,
    public nameOfPropertyValuer?: string | null,
    public collateralLastValuationDate?: dayjs.Dayjs | null,
    public insuredFlag?: CollateralInsuredFlagTypes,
    public nameOfInsurer?: string | null,
    public amountInsured?: number | null,
    public insuranceExpiryDate?: dayjs.Dayjs | null,
    public guaranteeInsurers?: string | null,
    public bankCode?: IInstitutionCode,
    public branchCode?: IBankBranchCode,
    public collateralType?: ICollateralType,
    public countyCode?: ICountySubCountyCode | null
  ) {}
}

export function getCollateralInformationIdentifier(collateralInformation: ICollateralInformation): number | undefined {
  return collateralInformation.id;
}
