import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { IKenyanCurrencyDenomination } from 'app/entities/gdi/kenyan-currency-denomination/kenyan-currency-denomination.model';

export interface IWeeklyCashHolding {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  fitUnits?: number;
  unfitUnits?: number;
  bankCode?: IInstitutionCode;
  branchId?: IBankBranchCode;
  subCountyCode?: ICountySubCountyCode;
  denomination?: IKenyanCurrencyDenomination;
}

export class WeeklyCashHolding implements IWeeklyCashHolding {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public fitUnits?: number,
    public unfitUnits?: number,
    public bankCode?: IInstitutionCode,
    public branchId?: IBankBranchCode,
    public subCountyCode?: ICountySubCountyCode,
    public denomination?: IKenyanCurrencyDenomination
  ) {}
}

export function getWeeklyCashHoldingIdentifier(weeklyCashHolding: IWeeklyCashHolding): number | undefined {
  return weeklyCashHolding.id;
}
