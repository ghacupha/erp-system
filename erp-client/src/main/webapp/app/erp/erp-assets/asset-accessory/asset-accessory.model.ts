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

import { IAssetCategory } from '../asset-category/asset-category.model';
import { IJobSheet } from '../../erp-settlements/job-sheet/job-sheet.model';
import { IDeliveryNote } from '../../erp-settlements/delivery-note/delivery-note.model';
import { IPaymentInvoice } from '../../erp-settlements/payment-invoice/payment-invoice.model';
import { IAssetWarranty } from '../asset-warranty/asset-warranty.model';
import { IServiceOutlet } from '../../erp-granular/service-outlet/service-outlet.model';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IBusinessDocument } from '../../erp-pages/business-document/business-document.model';
import { IPurchaseOrder } from '../../erp-settlements/purchase-order/purchase-order.model';
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

export interface IAssetAccessory {
  id?: number;
  assetTag?: string | null;
  assetDetails?: string | null;
  commentsContentType?: string | null;
  comments?: string | null;
  modelNumber?: string | null;
  serialNumber?: string | null;
  assetWarranties?: IAssetWarranty[] | null;
  placeholders?: IPlaceholder[] | null;
  paymentInvoices?: IPaymentInvoice[] | null;
  serviceOutlet?: IServiceOutlet;
  settlements?: ISettlement[];
  assetCategory?: IAssetCategory;
  purchaseOrders?: IPurchaseOrder[] | null;
  deliveryNotes?: IDeliveryNote[] | null;
  jobSheets?: IJobSheet[] | null;
  dealer?: IDealer;
  designatedUsers?: IDealer[] | null;
  businessDocuments?: IBusinessDocument[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
}

export class AssetAccessory implements IAssetAccessory {
  constructor(
    public id?: number,
    public assetTag?: string | null,
    public assetDetails?: string | null,
    public commentsContentType?: string | null,
    public comments?: string | null,
    public modelNumber?: string | null,
    public serialNumber?: string | null,
    public assetWarranties?: IAssetWarranty[] | null,
    public placeholders?: IPlaceholder[] | null,
    public paymentInvoices?: IPaymentInvoice[] | null,
    public serviceOutlet?: IServiceOutlet,
    public settlements?: ISettlement[],
    public assetCategory?: IAssetCategory,
    public purchaseOrders?: IPurchaseOrder[] | null,
    public deliveryNotes?: IDeliveryNote[] | null,
    public jobSheets?: IJobSheet[] | null,
    public dealer?: IDealer,
    public designatedUsers?: IDealer[] | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getAssetAccessoryIdentifier(assetAccessory: IAssetAccessory): number | undefined {
  return assetAccessory.id;
}
