import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';

export interface IPaymentRequisition {
  id?: number;
  invoicedAmount?: number | null;
  disbursementCost?: number | null;
  vatableAmount?: number | null;
  payment?: IPayment | null;
  dealer?: IDealer | null;
}

export class PaymentRequisition implements IPaymentRequisition {
  constructor(
    public id?: number,
    public invoicedAmount?: number | null,
    public disbursementCost?: number | null,
    public vatableAmount?: number | null,
    public payment?: IPayment | null,
    public dealer?: IDealer | null
  ) {}
}

export function getPaymentRequisitionIdentifier(paymentRequisition: IPaymentRequisition): number | undefined {
  return paymentRequisition.id;
}
