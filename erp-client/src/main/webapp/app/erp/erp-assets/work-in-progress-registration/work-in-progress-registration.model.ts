///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { IJobSheet } from '../../erp-settlements/job-sheet/job-sheet.model';
import { IDeliveryNote } from '../../erp-settlements/delivery-note/delivery-note.model';
import { IServiceOutlet } from '../../erp-granular/service-outlet/service-outlet.model';
import { IPaymentInvoice } from '../../erp-settlements/payment-invoice/payment-invoice.model';
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';
import { IPurchaseOrder } from '../../erp-settlements/purchase-order/purchase-order.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IBusinessDocument } from '../../erp-pages/business-document/business-document.model';
import { IAssetAccessory } from '../asset-accessory/asset-accessory.model';
import { IAssetWarranty } from '../asset-warranty/asset-warranty.model';
import { ISettlementCurrency } from '../../erp-settlements/settlement-currency/settlement-currency.model';
import { IWorkProjectRegister } from '../work-project-register/work-project-register.model';
import * as dayjs from 'dayjs';

export interface IWorkInProgressRegistration {
  id?: number;
  sequenceNumber?: string;
  particulars?: string | null;
  instalmentDate?: dayjs.Dayjs | null;
  instalmentAmount?: number | null;
  commentsContentType?: string | null;
  comments?: string | null;
  levelOfCompletion?: number | null;
  completed?: boolean | null;
  placeholders?: IPlaceholder[] | null;
  workInProgressGroup?: IWorkInProgressRegistration | null;
  settlementCurrency?: ISettlementCurrency | null;
  workProjectRegister?: IWorkProjectRegister | null;
  businessDocuments?: IBusinessDocument[] | null;
  assetAccessories?: IAssetAccessory[] | null;
  assetWarranties?: IAssetWarranty[] | null;
  invoice?: IPaymentInvoice | null;
  outletCode?: IServiceOutlet | null;
  settlementTransaction?: ISettlement | null;
  purchaseOrder?: IPurchaseOrder | null;
  deliveryNote?: IDeliveryNote | null;
  jobSheet?: IJobSheet | null;
  dealer?: IDealer | null;
}

export class WorkInProgressRegistration implements IWorkInProgressRegistration {
  constructor(
    public id?: number,
    public sequenceNumber?: string,
    public particulars?: string | null,
    public instalmentDate?: dayjs.Dayjs | null,
    public instalmentAmount?: number | null,
    public commentsContentType?: string | null,
    public comments?: string | null,
    public levelOfCompletion?: number | null,
    public completed?: boolean | null,
    public placeholders?: IPlaceholder[] | null,
    public workInProgressGroup?: IWorkInProgressRegistration | null,
    public settlementCurrency?: ISettlementCurrency | null,
    public workProjectRegister?: IWorkProjectRegister | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public assetAccessories?: IAssetAccessory[] | null,
    public assetWarranties?: IAssetWarranty[] | null,
    public invoice?: IPaymentInvoice | null,
    public outletCode?: IServiceOutlet | null,
    public settlementTransaction?: ISettlement | null,
    public purchaseOrder?: IPurchaseOrder | null,
    public deliveryNote?: IDeliveryNote | null,
    public jobSheet?: IJobSheet | null,
    public dealer?: IDealer | null
  ) {
    this.completed = this.completed ?? false;
  }
}

export function getWorkInProgressRegistrationIdentifier(workInProgressRegistration: IWorkInProgressRegistration): number | undefined {
  return workInProgressRegistration.id;
}
