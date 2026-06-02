import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ICustomerIDDocumentType {
  id?: number;
  documentCode?: string;
  documentType?: string;
  documentTypeDescription?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class CustomerIDDocumentType implements ICustomerIDDocumentType {
  constructor(
    public id?: number,
    public documentCode?: string,
    public documentType?: string,
    public documentTypeDescription?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getCustomerIDDocumentTypeIdentifier(customerIDDocumentType: ICustomerIDDocumentType): number | undefined {
  return customerIDDocumentType.id;
}
