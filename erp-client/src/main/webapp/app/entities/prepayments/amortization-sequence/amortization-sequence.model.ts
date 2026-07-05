import * as dayjs from 'dayjs';
import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { IAmortizationRecurrence } from 'app/entities/prepayments/amortization-recurrence/amortization-recurrence.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPrepaymentMapping } from 'app/entities/prepayments/prepayment-mapping/prepayment-mapping.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';

export interface IAmortizationSequence {
  id?: number;
  prepaymentAccountGuid?: string;
  recurrenceGuid?: string;
  sequenceNumber?: number;
  particulars?: string | null;
  currentAmortizationDate?: dayjs.Dayjs;
  previousAmortizationDate?: dayjs.Dayjs | null;
  nextAmortizationDate?: dayjs.Dayjs | null;
  isCommencementSequence?: boolean;
  isTerminalSequence?: boolean;
  amortizationAmount?: number;
  sequenceGuid?: string;
  prepaymentAccount?: IPrepaymentAccount;
  amortizationRecurrence?: IAmortizationRecurrence;
  placeholders?: IPlaceholder[] | null;
  prepaymentMappings?: IPrepaymentMapping[] | null;
  applicationParameters?: IUniversallyUniqueMapping[] | null;
}

export class AmortizationSequence implements IAmortizationSequence {
  constructor(
    public id?: number,
    public prepaymentAccountGuid?: string,
    public recurrenceGuid?: string,
    public sequenceNumber?: number,
    public particulars?: string | null,
    public currentAmortizationDate?: dayjs.Dayjs,
    public previousAmortizationDate?: dayjs.Dayjs | null,
    public nextAmortizationDate?: dayjs.Dayjs | null,
    public isCommencementSequence?: boolean,
    public isTerminalSequence?: boolean,
    public amortizationAmount?: number,
    public sequenceGuid?: string,
    public prepaymentAccount?: IPrepaymentAccount,
    public amortizationRecurrence?: IAmortizationRecurrence,
    public placeholders?: IPlaceholder[] | null,
    public prepaymentMappings?: IPrepaymentMapping[] | null,
    public applicationParameters?: IUniversallyUniqueMapping[] | null
  ) {
    this.isCommencementSequence = this.isCommencementSequence ?? false;
    this.isTerminalSequence = this.isTerminalSequence ?? false;
  }
}

export function getAmortizationSequenceIdentifier(amortizationSequence: IAmortizationSequence): number | undefined {
  return amortizationSequence.id;
}
