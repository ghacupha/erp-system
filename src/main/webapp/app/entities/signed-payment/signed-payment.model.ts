import * as dayjs from 'dayjs';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface ISignedPayment {
  id?: number;
  transactionNumber?: string;
  transactionDate?: dayjs.Dayjs;
  transactionCurrency?: CurrencyTypes;
  transactionAmount?: number;
  paymentLabels?: IPaymentLabel[] | null;
  dealer?: IDealer | null;
  paymentCategory?: IPaymentCategory | null;
  placeholders?: IPlaceholder[] | null;
  signedPaymentGroup?: ISignedPayment | null;
}

export class SignedPayment implements ISignedPayment {
  constructor(
    public id?: number,
    public transactionNumber?: string,
    public transactionDate?: dayjs.Dayjs,
    public transactionCurrency?: CurrencyTypes,
    public transactionAmount?: number,
    public paymentLabels?: IPaymentLabel[] | null,
    public dealer?: IDealer | null,
    public paymentCategory?: IPaymentCategory | null,
    public placeholders?: IPlaceholder[] | null,
    public signedPaymentGroup?: ISignedPayment | null
  ) {}
}

export function getSignedPaymentIdentifier(signedPayment: ISignedPayment): number | undefined {
  return signedPayment.id;
}
