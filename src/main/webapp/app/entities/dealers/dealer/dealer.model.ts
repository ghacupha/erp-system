import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { ISignedPayment } from 'app/entities/signed-payment/signed-payment.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';

export interface IDealer {
  id?: number;
  dealerName?: string;
  taxNumber?: string | null;
  postalAddress?: string | null;
  physicalAddress?: string | null;
  accountName?: string | null;
  accountNumber?: string | null;
  bankersName?: string | null;
  bankersBranch?: string | null;
  bankersSwiftCode?: string | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
  dealerGroup?: IDealer | null;
  paymentRequisitions?: IPaymentRequisition[] | null;
  signedPayments?: ISignedPayment[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class Dealer implements IDealer {
  constructor(
    public id?: number,
    public dealerName?: string,
    public taxNumber?: string | null,
    public postalAddress?: string | null,
    public physicalAddress?: string | null,
    public accountName?: string | null,
    public accountNumber?: string | null,
    public bankersName?: string | null,
    public bankersBranch?: string | null,
    public bankersSwiftCode?: string | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public dealerGroup?: IDealer | null,
    public paymentRequisitions?: IPaymentRequisition[] | null,
    public signedPayments?: ISignedPayment[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getDealerIdentifier(dealer: IDealer): number | undefined {
  return dealer.id;
}
