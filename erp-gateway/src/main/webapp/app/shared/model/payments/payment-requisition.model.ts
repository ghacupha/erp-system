import { IPayment } from 'app/shared/model/payments/payment.model';

export interface IPaymentRequisition {
  id?: number;
  dealerName?: string;
  invoicedAmount?: number;
  disbursementCost?: number;
  vatableAmount?: number;
  requisitions?: IPayment[];
}

export class PaymentRequisition implements IPaymentRequisition {
  constructor(
    public id?: number,
    public dealerName?: string,
    public invoicedAmount?: number,
    public disbursementCost?: number,
    public vatableAmount?: number,
    public requisitions?: IPayment[]
  ) {}
}
