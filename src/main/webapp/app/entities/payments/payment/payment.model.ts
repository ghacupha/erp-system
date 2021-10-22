import * as dayjs from 'dayjs';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface IPayment {
  id?: number;
  paymentNumber?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  invoicedAmount?: number | null;
  disbursementCost?: number | null;
  vatableAmount?: number | null;
  paymentAmount?: number | null;
  description?: string | null;
  settlementCurrency?: CurrencyTypes;
  conversionRate?: number;
  paymentLabels?: IPaymentLabel[] | null;
  dealer?: IDealer | null;
  paymentCategory?: IPaymentCategory | null;
  taxRule?: ITaxRule | null;
  paymentCalculation?: IPaymentCalculation | null;
  placeholders?: IPlaceholder[] | null;
  paymentGroup?: IPayment | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentNumber?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public invoicedAmount?: number | null,
    public disbursementCost?: number | null,
    public vatableAmount?: number | null,
    public paymentAmount?: number | null,
    public description?: string | null,
    public settlementCurrency?: CurrencyTypes,
    public conversionRate?: number,
    public paymentLabels?: IPaymentLabel[] | null,
    public dealer?: IDealer | null,
    public paymentCategory?: IPaymentCategory | null,
    public taxRule?: ITaxRule | null,
    public paymentCalculation?: IPaymentCalculation | null,
    public placeholders?: IPlaceholder[] | null,
    public paymentGroup?: IPayment | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
