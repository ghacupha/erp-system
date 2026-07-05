import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPaymentInvoice } from 'app/entities/settlement/payment-invoice/payment-invoice.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IPurchaseOrder } from 'app/entities/settlement/purchase-order/purchase-order.model';
import { IDeliveryNote } from 'app/entities/settlement/delivery-note/delivery-note.model';
import { IJobSheet } from 'app/entities/settlement/job-sheet/job-sheet.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { IAssetWarranty } from 'app/entities/assets/asset-warranty/asset-warranty.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IAssetAccessory } from 'app/entities/assets/asset-accessory/asset-accessory.model';

export interface IAssetRegistration {
  id?: number;
  assetNumber?: string;
  assetTag?: string;
  assetDetails?: string | null;
  assetCost?: number;
  commentsContentType?: string | null;
  comments?: string | null;
  modelNumber?: string | null;
  serialNumber?: string | null;
  remarks?: string | null;
  capitalizationDate?: dayjs.Dayjs;
  historicalCost?: number;
  registrationDate?: dayjs.Dayjs;
  placeholders?: IPlaceholder[] | null;
  paymentInvoices?: IPaymentInvoice[] | null;
  otherRelatedServiceOutlets?: IServiceOutlet[] | null;
  otherRelatedSettlements?: ISettlement[] | null;
  assetCategory?: IAssetCategory;
  purchaseOrders?: IPurchaseOrder[] | null;
  deliveryNotes?: IDeliveryNote[] | null;
  jobSheets?: IJobSheet[] | null;
  dealer?: IDealer;
  designatedUsers?: IDealer[] | null;
  settlementCurrency?: ISettlementCurrency | null;
  businessDocuments?: IBusinessDocument[] | null;
  assetWarranties?: IAssetWarranty[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  assetAccessories?: IAssetAccessory[] | null;
  mainServiceOutlet?: IServiceOutlet | null;
  acquiringTransaction?: ISettlement;
}

export class AssetRegistration implements IAssetRegistration {
  constructor(
    public id?: number,
    public assetNumber?: string,
    public assetTag?: string,
    public assetDetails?: string | null,
    public assetCost?: number,
    public commentsContentType?: string | null,
    public comments?: string | null,
    public modelNumber?: string | null,
    public serialNumber?: string | null,
    public remarks?: string | null,
    public capitalizationDate?: dayjs.Dayjs,
    public historicalCost?: number,
    public registrationDate?: dayjs.Dayjs,
    public placeholders?: IPlaceholder[] | null,
    public paymentInvoices?: IPaymentInvoice[] | null,
    public otherRelatedServiceOutlets?: IServiceOutlet[] | null,
    public otherRelatedSettlements?: ISettlement[] | null,
    public assetCategory?: IAssetCategory,
    public purchaseOrders?: IPurchaseOrder[] | null,
    public deliveryNotes?: IDeliveryNote[] | null,
    public jobSheets?: IJobSheet[] | null,
    public dealer?: IDealer,
    public designatedUsers?: IDealer[] | null,
    public settlementCurrency?: ISettlementCurrency | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public assetWarranties?: IAssetWarranty[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public assetAccessories?: IAssetAccessory[] | null,
    public mainServiceOutlet?: IServiceOutlet | null,
    public acquiringTransaction?: ISettlement
  ) {}
}

export function getAssetRegistrationIdentifier(assetRegistration: IAssetRegistration): number | undefined {
  return assetRegistration.id;
}
