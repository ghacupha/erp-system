import * as dayjs from 'dayjs';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { IPaymentInvoice } from 'app/entities/settlement/payment-invoice/payment-invoice.model';
import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';

export interface ICreditNote {
  id?: number;
  creditNumber?: string;
  creditNoteDate?: dayjs.Dayjs;
  creditAmount?: number;
  remarks?: string | null;
  purchaseOrders?: IPurchaseOrder[] | null;
  invoices?: IPaymentInvoice[] | null;
  paymentLabels?: IPaymentLabel[] | null;
  placeholders?: IPlaceholder[] | null;
  settlementCurrency?: ISettlementCurrency | null;
}

export class CreditNote implements ICreditNote {
  constructor(
    public id?: number,
    public creditNumber?: string,
    public creditNoteDate?: dayjs.Dayjs,
    public creditAmount?: number,
    public remarks?: string | null,
    public purchaseOrders?: IPurchaseOrder[] | null,
    public invoices?: IPaymentInvoice[] | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public placeholders?: IPlaceholder[] | null,
    public settlementCurrency?: ISettlementCurrency | null
  ) {}
}

export function getCreditNoteIdentifier(creditNote: ICreditNote): number | undefined {
  return creditNote.id;
}
