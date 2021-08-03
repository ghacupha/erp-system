import { IPayment } from 'app/shared/model/payments/payment.model';

export interface IDealer {
  id?: number;
  dealerName?: string;
  taxNumber?: string;
  postalAddress?: string;
  physicalAddress?: string;
  accountName?: string;
  accountNumber?: string;
  bankersName?: string;
  bankersBranch?: string;
  bankersSwiftCode?: string;
  payments?: IPayment[];
}

export class Dealer implements IDealer {
  constructor(
    public id?: number,
    public dealerName?: string,
    public taxNumber?: string,
    public postalAddress?: string,
    public physicalAddress?: string,
    public accountName?: string,
    public accountNumber?: string,
    public bankersName?: string,
    public bankersBranch?: string,
    public bankersSwiftCode?: string,
    public payments?: IPayment[]
  ) {}
}
