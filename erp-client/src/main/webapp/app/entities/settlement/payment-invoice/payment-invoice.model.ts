import * as dayjs from 'dayjs';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IDeliveryNote } from 'app/entities/settlement/delivery-note/delivery-note.model';
import { IJobSheet } from 'app/entities/settlement/job-sheet/job-sheet.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';

export interface IPaymentInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceAmount?: number | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  remarks?: string | null;
  purchaseOrders?: IPurchaseOrder[] | null;
  placeholders?: IPlaceholder[] | null;
  paymentLabels?: IPaymentLabel[] | null;
  settlementCurrency?: ISettlementCurrency;
  biller?: IDealer;
  deliveryNotes?: IDeliveryNote[] | null;
  jobSheets?: IJobSheet[] | null;
  businessDocuments?: IBusinessDocument[] | null;
}

export class PaymentInvoice implements IPaymentInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: string,
    public invoiceDate?: dayjs.Dayjs | null,
    public invoiceAmount?: number | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public remarks?: string | null,
    public purchaseOrders?: IPurchaseOrder[] | null,
    public placeholders?: IPlaceholder[] | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public settlementCurrency?: ISettlementCurrency,
    public biller?: IDealer,
    public deliveryNotes?: IDeliveryNote[] | null,
    public jobSheets?: IJobSheet[] | null,
    public businessDocuments?: IBusinessDocument[] | null
  ) {}
}

export function getPaymentInvoiceIdentifier(paymentInvoice: IPaymentInvoice): number | undefined {
  return paymentInvoice.id;
}
