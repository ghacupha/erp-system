import * as dayjs from 'dayjs';
import { ICountySubCountyCode } from 'app/entities/gdi-data/county-sub-county-code/county-sub-county-code.model';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IOutletType } from 'app/entities/system/outlet-type/outlet-type.model';
import { IOutletStatus } from 'app/entities/system/outlet-status/outlet-status.model';

export interface IParticularsOfOutlet {
  id?: number;
  businessReportingDate?: dayjs.Dayjs;
  outletName?: string;
  town?: string;
  iso6709Latitute?: number;
  iso6709Longitude?: number;
  cbkApprovalDate?: dayjs.Dayjs;
  outletOpeningDate?: dayjs.Dayjs;
  outletClosureDate?: dayjs.Dayjs | null;
  licenseFeePayable?: number;
  subCountyCode?: ICountySubCountyCode;
  bankCode?: IInstitutionCode;
  outletId?: IBankBranchCode;
  typeOfOutlet?: IOutletType;
  outletStatus?: IOutletStatus;
}

export class ParticularsOfOutlet implements IParticularsOfOutlet {
  constructor(
    public id?: number,
    public businessReportingDate?: dayjs.Dayjs,
    public outletName?: string,
    public town?: string,
    public iso6709Latitute?: number,
    public iso6709Longitude?: number,
    public cbkApprovalDate?: dayjs.Dayjs,
    public outletOpeningDate?: dayjs.Dayjs,
    public outletClosureDate?: dayjs.Dayjs | null,
    public licenseFeePayable?: number,
    public subCountyCode?: ICountySubCountyCode,
    public bankCode?: IInstitutionCode,
    public outletId?: IBankBranchCode,
    public typeOfOutlet?: IOutletType,
    public outletStatus?: IOutletStatus
  ) {}
}

export function getParticularsOfOutletIdentifier(particularsOfOutlet: IParticularsOfOutlet): number | undefined {
  return particularsOfOutlet.id;
}
