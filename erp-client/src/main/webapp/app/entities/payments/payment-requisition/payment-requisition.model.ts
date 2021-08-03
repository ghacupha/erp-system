import { IPayment } from 'app/entities/payments/payment/payment.model';

export interface IPaymentRequisition {
  id?: number;
  dealerName?: string | null;
  invoicedAmount?: number | null;
  disbursementCost?: number | null;
  vatableAmount?: number | null;
  payment?: IPayment | null;
}

export class PaymentRequisition implements IPaymentRequisition {
  constructor(
    public id?: number,
    public dealerName?: string | null,
    public invoicedAmount?: number | null,
    public disbursementCost?: number | null,
    public vatableAmount?: number | null,
    public payment?: IPayment | null
  ) {}
}

export function getPaymentRequisitionIdentifier(paymentRequisition: IPaymentRequisition): number | undefined {
  return paymentRequisition.id;
}
