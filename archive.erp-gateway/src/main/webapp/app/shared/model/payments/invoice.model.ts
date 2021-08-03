import { Moment } from 'moment';

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: Moment;
  invoiceAmount?: number;
  paymentId?: number;
  dealerId?: number;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: string,
    public invoiceDate?: Moment,
    public invoiceAmount?: number,
    public paymentId?: number,
    public dealerId?: number
  ) {}
}
