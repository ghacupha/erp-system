import * as dayjs from 'dayjs';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';

export interface IPaymentCalculation {
  id?: number;
  paymentNumber?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  paymentExpense?: number | null;
  withholdingVAT?: number | null;
  withholdingTax?: number | null;
  paymentAmount?: number | null;
  payment?: IPayment | null;
  paymentCategory?: IPaymentCategory | null;
}

export class PaymentCalculation implements IPaymentCalculation {
  constructor(
    public id?: number,
    public paymentNumber?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public paymentExpense?: number | null,
    public withholdingVAT?: number | null,
    public withholdingTax?: number | null,
    public paymentAmount?: number | null,
    public payment?: IPayment | null,
    public paymentCategory?: IPaymentCategory | null
  ) {}
}

export function getPaymentCalculationIdentifier(paymentCalculation: IPaymentCalculation): number | undefined {
  return paymentCalculation.id;
}
