import * as dayjs from 'dayjs';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: string | null;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceAmount?: number | null;
  payment?: IPayment | null;
  dealer?: IDealer | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: string | null,
    public invoiceDate?: dayjs.Dayjs | null,
    public invoiceAmount?: number | null,
    public payment?: IPayment | null,
    public dealer?: IDealer | null
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
