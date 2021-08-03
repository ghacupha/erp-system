import { Moment } from 'moment';

export interface IPaymentCalculation {
  id?: number;
  paymentNumber?: string;
  paymentDate?: Moment;
  paymentCategory?: string;
  paymentExpense?: number;
  withholdingVAT?: number;
  withholdingTax?: number;
  paymentAmount?: number;
  paymentId?: number;
}

export class PaymentCalculation implements IPaymentCalculation {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: Moment,
    public paymentCategory?: string,
    public paymentExpense?: number,
    public withholdingVAT?: number,
    public withholdingTax?: number,
    public paymentAmount?: number,
    public paymentId?: number
  ) {}
}
