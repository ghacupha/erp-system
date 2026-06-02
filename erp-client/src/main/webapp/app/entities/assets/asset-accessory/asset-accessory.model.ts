import { IAssetWarranty } from 'app/entities/assets/asset-warranty/asset-warranty.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPaymentInvoice } from 'app/entities/settlement/payment-invoice/payment-invoice.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { IDeliveryNote } from 'app/entities/settlement/delivery-note/delivery-note.model';
import { IJobSheet } from 'app/entities/settlement/job-sheet/job-sheet.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';

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
