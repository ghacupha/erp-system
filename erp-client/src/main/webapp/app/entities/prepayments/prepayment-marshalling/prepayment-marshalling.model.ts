import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { IAmortizationPeriod } from 'app/entities/prepayments/amortization-period/amortization-period.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';

export interface IPrepaymentMarshalling {
  id?: number;
  inactive?: boolean;
  amortizationPeriods?: number | null;
  processed?: boolean | null;
  prepaymentAccount?: IPrepaymentAccount;
  firstAmortizationPeriod?: IAmortizationPeriod;
  placeholders?: IPlaceholder[] | null;
  firstFiscalMonth?: IFiscalMonth;
  lastFiscalMonth?: IFiscalMonth;
}

export class PrepaymentMarshalling implements IPrepaymentMarshalling {
  constructor(
    public id?: number,
    public inactive?: boolean,
    public amortizationPeriods?: number | null,
    public processed?: boolean | null,
    public prepaymentAccount?: IPrepaymentAccount,
    public firstAmortizationPeriod?: IAmortizationPeriod,
    public placeholders?: IPlaceholder[] | null,
    public firstFiscalMonth?: IFiscalMonth,
    public lastFiscalMonth?: IFiscalMonth
  ) {
    this.inactive = this.inactive ?? false;
    this.processed = this.processed ?? false;
  }
}

export function getPrepaymentMarshallingIdentifier(prepaymentMarshalling: IPrepaymentMarshalling): number | undefined {
  return prepaymentMarshalling.id;
}
