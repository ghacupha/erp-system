import * as dayjs from 'dayjs';
import { IInvoice } from 'app/entities/payments/invoice/invoice.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface IPayment {
  id?: number;
  paymentNumber?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  paymentAmount?: number | null;
  description?: string | null;
  currency?: CurrencyTypes;
  conversionRate?: number;
  ownedInvoices?: IInvoice[] | null;
  dealers?: IDealer[] | null;
  paymentCategory?: IPaymentCategory | null;
  taxRule?: ITaxRule | null;
  paymentCalculation?: IPaymentCalculation | null;
  paymentRequisition?: IPaymentRequisition | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentNumber?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public paymentAmount?: number | null,
    public description?: string | null,
    public currency?: CurrencyTypes,
    public conversionRate?: number,
    public ownedInvoices?: IInvoice[] | null,
    public dealers?: IDealer[] | null,
    public paymentCategory?: IPaymentCategory | null,
    public taxRule?: ITaxRule | null,
    public paymentCalculation?: IPaymentCalculation | null,
    public paymentRequisition?: IPaymentRequisition | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
