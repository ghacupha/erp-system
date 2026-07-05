import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IInstitutionCode {
  id?: number;
  institutionCode?: string;
  institutionName?: string;
  shortName?: string | null;
  category?: string | null;
  institutionCategory?: string | null;
  institutionOwnership?: string | null;
  dateLicensed?: dayjs.Dayjs | null;
  institutionStatus?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class InstitutionCode implements IInstitutionCode {
  constructor(
    public id?: number,
    public institutionCode?: string,
    public institutionName?: string,
    public shortName?: string | null,
    public category?: string | null,
    public institutionCategory?: string | null,
    public institutionOwnership?: string | null,
    public dateLicensed?: dayjs.Dayjs | null,
    public institutionStatus?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getInstitutionCodeIdentifier(institutionCode: IInstitutionCode): number | undefined {
  return institutionCode.id;
}
