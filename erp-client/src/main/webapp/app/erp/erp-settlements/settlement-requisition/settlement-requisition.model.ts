///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import * as dayjs from 'dayjs';
import { ISettlementCurrency } from '../settlement-currency/settlement-currency.model';
import { IJobSheet } from '../job-sheet/job-sheet.model';
import { IBusinessDocument } from '../../erp-pages/business-document/business-document.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IDeliveryNote } from '../delivery-note/delivery-note.model';
import { IPaymentInvoice } from '../payment-invoice/payment-invoice.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { PaymentStatus } from '../../erp-common/enumerations/payment-status.model';
import { ISettlement } from '../settlement/settlement.model';

export interface ISettlementRequisition {
  id?: number;
  description?: string | null;
  serialNumber?: string;
  timeOfRequisition?: dayjs.Dayjs;
  requisitionNumber?: string;
  paymentAmount?: number;
  paymentStatus?: PaymentStatus;
  transactionId?: string | null;
  transactionDate?: dayjs.Dayjs | null;
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
    public transactionId?: string | null,
    public transactionDate?: dayjs.Dayjs | null,
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
