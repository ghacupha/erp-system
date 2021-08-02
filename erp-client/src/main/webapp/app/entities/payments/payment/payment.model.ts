import * as dayjs from 'dayjs';
import { IInvoice } from 'app/entities/payments/invoice/invoice.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';

export interface IPayment {
  id?: number;
  paymentNumber?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  paymentAmount?: number | null;
  description?: string | null;
  ownedInvoices?: IInvoice[] | null;
  paymentRequisition?: IPaymentRequisition | null;
  dealers?: IDealer[] | null;
  taxRule?: ITaxRule | null;
  paymentCategory?: IPaymentCategory;
  paymentCalculation?: IPaymentCalculation;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentNumber?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public paymentAmount?: number | null,
    public description?: string | null,
    public ownedInvoices?: IInvoice[] | null,
    public paymentRequisition?: IPaymentRequisition | null,
    public dealers?: IDealer[] | null,
    public taxRule?: ITaxRule | null,
    public paymentCategory?: IPaymentCategory,
    public paymentCalculation?: IPaymentCalculation
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}