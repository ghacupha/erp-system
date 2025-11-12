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

import * as dayjs from 'dayjs';
import { ITerminalTypes } from 'app/entities/gdi/terminal-types/terminal-types.model';
import { ITerminalFunctions } from 'app/entities/gdi/terminal-functions/terminal-functions.model';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';

export interface ITerminalsAndPOS {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  terminalId?: string;
  merchantId?: string;
  terminalName?: string;
  terminalLocation?: string;
  iso6709Latitute?: number;
  iso6709Longitude?: number;
  terminalOpeningDate?: dayjs.Dayjs;
  terminalClosureDate?: dayjs.Dayjs | null;
  terminalType?: ITerminalTypes;
  terminalFunctionality?: ITerminalFunctions;
  physicalLocation?: ICountySubCountyCode;
  bankId?: IInstitutionCode;
  branchId?: IBankBranchCode;
}

export class TerminalsAndPOS implements ITerminalsAndPOS {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public terminalId?: string,
    public merchantId?: string,
    public terminalName?: string,
    public terminalLocation?: string,
    public iso6709Latitute?: number,
    public iso6709Longitude?: number,
    public terminalOpeningDate?: dayjs.Dayjs,
    public terminalClosureDate?: dayjs.Dayjs | null,
    public terminalType?: ITerminalTypes,
    public terminalFunctionality?: ITerminalFunctions,
    public physicalLocation?: ICountySubCountyCode,
    public bankId?: IInstitutionCode,
    public branchId?: IBankBranchCode
  ) {}
}

export function getTerminalsAndPOSIdentifier(terminalsAndPOS: ITerminalsAndPOS): number | undefined {
  return terminalsAndPOS.id;
}
