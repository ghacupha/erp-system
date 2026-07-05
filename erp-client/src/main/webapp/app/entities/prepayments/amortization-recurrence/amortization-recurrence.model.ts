import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPrepaymentMapping } from 'app/entities/prepayments/prepayment-mapping/prepayment-mapping.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IDepreciationMethod } from 'app/entities/assets/depreciation-method/depreciation-method.model';
import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { recurrenceFrequency } from 'app/entities/enumerations/recurrence-frequency.model';

export interface IAmortizationRecurrence {
  id?: number;
  firstAmortizationDate?: dayjs.Dayjs;
  amortizationFrequency?: recurrenceFrequency;
  numberOfRecurrences?: number;
  notesContentType?: string | null;
  notes?: string | null;
  particulars?: string | null;
  isActive?: boolean | null;
  isOverWritten?: boolean | null;
  timeOfInstallation?: dayjs.Dayjs;
  recurrenceGuid?: string;
  prepaymentAccountGuid?: string;
  placeholders?: IPlaceholder[] | null;
  parameters?: IPrepaymentMapping[] | null;
  applicationParameters?: IUniversallyUniqueMapping[] | null;
  depreciationMethod?: IDepreciationMethod;
  prepaymentAccount?: IPrepaymentAccount;
}

export class AmortizationRecurrence implements IAmortizationRecurrence {
  constructor(
    public id?: number,
    public firstAmortizationDate?: dayjs.Dayjs,
    public amortizationFrequency?: recurrenceFrequency,
    public numberOfRecurrences?: number,
    public notesContentType?: string | null,
    public notes?: string | null,
    public particulars?: string | null,
    public isActive?: boolean | null,
    public isOverWritten?: boolean | null,
    public timeOfInstallation?: dayjs.Dayjs,
    public recurrenceGuid?: string,
    public prepaymentAccountGuid?: string,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IPrepaymentMapping[] | null,
    public applicationParameters?: IUniversallyUniqueMapping[] | null,
    public depreciationMethod?: IDepreciationMethod,
    public prepaymentAccount?: IPrepaymentAccount
  ) {
    this.isActive = this.isActive ?? false;
    this.isOverWritten = this.isOverWritten ?? false;
  }
}

export function getAmortizationRecurrenceIdentifier(amortizationRecurrence: IAmortizationRecurrence): number | undefined {
  return amortizationRecurrence.id;
}
