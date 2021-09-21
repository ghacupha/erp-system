import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IInvoice } from 'app/entities/payments/invoice/invoice.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { ISignedPayment } from 'app/entities/signed-payment/signed-payment.model';

export interface IPaymentLabel {
  id?: number;
  description?: string;
  comments?: string | null;
  containingPaymentLabel?: IPaymentLabel | null;
  placeholders?: IPlaceholder[] | null;
  paymentCalculations?: IPaymentCalculation[] | null;
  paymentCategories?: IPaymentCategory[] | null;
  paymentRequisitions?: IPaymentRequisition[] | null;
  payments?: IPayment[] | null;
  invoices?: IInvoice[] | null;
  dealers?: IDealer[] | null;
  signedPayments?: ISignedPayment[] | null;
}

export class PaymentLabel implements IPaymentLabel {
  constructor(
    public id?: number,
    public description?: string,
    public comments?: string | null,
    public containingPaymentLabel?: IPaymentLabel | null,
    public placeholders?: IPlaceholder[] | null,
    public paymentCalculations?: IPaymentCalculation[] | null,
    public paymentCategories?: IPaymentCategory[] | null,
    public paymentRequisitions?: IPaymentRequisition[] | null,
    public payments?: IPayment[] | null,
    public invoices?: IInvoice[] | null,
    public dealers?: IDealer[] | null,
    public signedPayments?: ISignedPayment[] | null
  ) {}
}

export function getPaymentLabelIdentifier(paymentLabel: IPaymentLabel): number | undefined {
  return paymentLabel.id;
}
