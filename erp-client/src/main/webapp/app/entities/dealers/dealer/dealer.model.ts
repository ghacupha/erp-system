import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';

export interface IDealer {
  id?: number;
  dealerName?: string | null;
  taxNumber?: string | null;
  postalAddress?: string | null;
  physicalAddress?: string | null;
  accountName?: string | null;
  accountNumber?: string | null;
  bankersName?: string | null;
  bankersBranch?: string | null;
  bankersSwiftCode?: string | null;
  payments?: IPayment[] | null;
  paymentRequisitions?: IPaymentRequisition[] | null;
}

export class Dealer implements IDealer {
  constructor(
    public id?: number,
    public dealerName?: string | null,
    public taxNumber?: string | null,
    public postalAddress?: string | null,
    public physicalAddress?: string | null,
    public accountName?: string | null,
    public accountNumber?: string | null,
    public bankersName?: string | null,
    public bankersBranch?: string | null,
    public bankersSwiftCode?: string | null,
    public payments?: IPayment[] | null,
    public paymentRequisitions?: IPaymentRequisition[] | null
  ) {}
}

export function getDealerIdentifier(dealer: IDealer): number | undefined {
  return dealer.id;
}
