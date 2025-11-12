///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IWorkProjectRegister } from 'app/entities/wip/work-project-register/work-project-register.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IAssetAccessory } from 'app/entities/assets/asset-accessory/asset-accessory.model';
import { IAssetWarranty } from 'app/entities/assets/asset-warranty/asset-warranty.model';
import { IPaymentInvoice } from 'app/entities/settlement/payment-invoice/payment-invoice.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { IDeliveryNote } from 'app/entities/settlement/delivery-note/delivery-note.model';
import { IJobSheet } from 'app/entities/settlement/job-sheet/job-sheet.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';

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
