import * as dayjs from 'dayjs';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IPaymentInvoice } from 'app/entities/settlement/payment-invoice/payment-invoice.model';
import { IDeliveryNote } from 'app/entities/settlement/delivery-note/delivery-note.model';
import { IJobSheet } from 'app/entities/settlement/job-sheet/job-sheet.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface ISettlementRequisition {
  id?: number;
  description?: string | null;
  serialNumber?: string;
  timeOfRequisition?: dayjs.Dayjs;
  requisitionNumber?: string;
  paymentAmount?: number;
  paymentStatus?: PaymentStatus;
  settlementCurrency?: ISettlementCurrency;
  currentOwner?: IApplicationUser;
  nativeOwner?: IApplicationUser;
  nativeDepartment?: IDealer;
  biller?: IDealer;
  paymentInvoices?: IPaymentInvoice[] | null;
  deliveryNotes?: IDeliveryNote[] | null;
  jobSheets?: IJobSheet[] | null;
  signatures?: IDealer[] | null;
  businessDocuments?: IBusinessDocument[] | null;
  applicationMappings?: IUniversallyUniqueMapping[] | null;
  placeholders?: IPlaceholder[] | null;
  settlements?: ISettlement[] | null;
}

export class SettlementRequisition implements ISettlementRequisition {
  constructor(
    public id?: number,
    public description?: string | null,
    public serialNumber?: string,
    public timeOfRequisition?: dayjs.Dayjs,
    public requisitionNumber?: string,
    public paymentAmount?: number,
    public paymentStatus?: PaymentStatus,
    public settlementCurrency?: ISettlementCurrency,
    public currentOwner?: IApplicationUser,
    public nativeOwner?: IApplicationUser,
    public nativeDepartment?: IDealer,
    public biller?: IDealer,
    public paymentInvoices?: IPaymentInvoice[] | null,
    public deliveryNotes?: IDeliveryNote[] | null,
    public jobSheets?: IJobSheet[] | null,
    public signatures?: IDealer[] | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public applicationMappings?: IUniversallyUniqueMapping[] | null,
    public placeholders?: IPlaceholder[] | null,
    public settlements?: ISettlement[] | null
  ) {}
}

export function getSettlementRequisitionIdentifier(settlementRequisition: ISettlementRequisition): number | undefined {
  return settlementRequisition.id;
}
