import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { FiscalYearStatusType } from 'app/entities/enumerations/fiscal-year-status-type.model';

export interface IFiscalYear {
  id?: number;
  fiscalYearCode?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  fiscalYearStatus?: FiscalYearStatusType | null;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  createdBy?: IApplicationUser | null;
  lastUpdatedBy?: IApplicationUser | null;
}

export class FiscalYear implements IFiscalYear {
  constructor(
    public id?: number,
    public fiscalYearCode?: string,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public fiscalYearStatus?: FiscalYearStatusType | null,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public createdBy?: IApplicationUser | null,
    public lastUpdatedBy?: IApplicationUser | null
  ) {}
}

export function getFiscalYearIdentifier(fiscalYear: IFiscalYear): number | undefined {
  return fiscalYear.id;
}
