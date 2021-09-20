import * as dayjs from 'dayjs';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CategoryTypes } from 'app/entities/enumerations/category-types.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface ISignedPayment {
  id?: number;
  paymentCategory?: CategoryTypes;
  transactionNumber?: string;
  transactionDate?: dayjs.Dayjs;
  transactionCurrency?: CurrencyTypes;
  transactionAmount?: number;
  beneficiary?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class SignedPayment implements ISignedPayment {
  constructor(
    public id?: number,
    public paymentCategory?: CategoryTypes,
    public transactionNumber?: string,
    public transactionDate?: dayjs.Dayjs,
    public transactionCurrency?: CurrencyTypes,
    public transactionAmount?: number,
    public beneficiary?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getSignedPaymentIdentifier(signedPayment: ISignedPayment): number | undefined {
  return signedPayment.id;
}
