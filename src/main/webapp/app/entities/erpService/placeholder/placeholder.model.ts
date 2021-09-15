import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { IFileType } from 'app/entities/files/file-type/file-type.model';
import { IFileUpload } from 'app/entities/files/file-upload/file-upload.model';
import { IFixedAssetAcquisition } from 'app/entities/assets/fixed-asset-acquisition/fixed-asset-acquisition.model';
import { IFixedAssetDepreciation } from 'app/entities/assets/fixed-asset-depreciation/fixed-asset-depreciation.model';
import { IFixedAssetNetBookValue } from 'app/entities/assets/fixed-asset-net-book-value/fixed-asset-net-book-value.model';
import { IInvoice } from 'app/entities/payments/invoice/invoice.model';
import { IMessageToken } from 'app/entities/files/message-token/message-token.model';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { IPaymentRequisition } from 'app/entities/payments/payment-requisition/payment-requisition.model';
import { IPaymentCategory } from 'app/entities/payments/payment-category/payment-category.model';
import { ITaxReference } from 'app/entities/payments/tax-reference/tax-reference.model';
import { ITaxRule } from 'app/entities/payments/tax-rule/tax-rule.model';

export interface IPlaceholder {
  id?: number;
  description?: string;
  token?: string | null;
  containingPlaceholder?: IPlaceholder | null;
  dealers?: IDealer[] | null;
  fileTypes?: IFileType[] | null;
  fileUploads?: IFileUpload[] | null;
  fixedAssetAcquisitions?: IFixedAssetAcquisition[] | null;
  fixedAssetDepreciations?: IFixedAssetDepreciation[] | null;
  fixedAssetNetBookValues?: IFixedAssetNetBookValue[] | null;
  invoices?: IInvoice[] | null;
  messageTokens?: IMessageToken[] | null;
  payments?: IPayment[] | null;
  paymentCalculations?: IPaymentCalculation[] | null;
  paymentRequisitions?: IPaymentRequisition[] | null;
  paymentCategories?: IPaymentCategory[] | null;
  taxReferences?: ITaxReference[] | null;
  taxRules?: ITaxRule[] | null;
}

export class Placeholder implements IPlaceholder {
  constructor(
    public id?: number,
    public description?: string,
    public token?: string | null,
    public containingPlaceholder?: IPlaceholder | null,
    public dealers?: IDealer[] | null,
    public fileTypes?: IFileType[] | null,
    public fileUploads?: IFileUpload[] | null,
    public fixedAssetAcquisitions?: IFixedAssetAcquisition[] | null,
    public fixedAssetDepreciations?: IFixedAssetDepreciation[] | null,
    public fixedAssetNetBookValues?: IFixedAssetNetBookValue[] | null,
    public invoices?: IInvoice[] | null,
    public messageTokens?: IMessageToken[] | null,
    public payments?: IPayment[] | null,
    public paymentCalculations?: IPaymentCalculation[] | null,
    public paymentRequisitions?: IPaymentRequisition[] | null,
    public paymentCategories?: IPaymentCategory[] | null,
    public taxReferences?: ITaxReference[] | null,
    public taxRules?: ITaxRule[] | null
  ) {}
}

export function getPlaceholderIdentifier(placeholder: IPlaceholder): number | undefined {
  return placeholder.id;
}
