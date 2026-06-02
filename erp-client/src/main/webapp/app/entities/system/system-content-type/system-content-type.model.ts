import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { SystemContentTypeAvailability } from 'app/entities/enumerations/system-content-type-availability.model';

export interface ISystemContentType {
  id?: number;
  contentTypeName?: string;
  contentTypeHeader?: string;
  comments?: string | null;
  availability?: SystemContentTypeAvailability;
  placeholders?: IPlaceholder[] | null;
  sysMaps?: IUniversallyUniqueMapping[] | null;
}

export class SystemContentType implements ISystemContentType {
  constructor(
    public id?: number,
    public contentTypeName?: string,
    public contentTypeHeader?: string,
    public comments?: string | null,
    public availability?: SystemContentTypeAvailability,
    public placeholders?: IPlaceholder[] | null,
    public sysMaps?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getSystemContentTypeIdentifier(systemContentType: ISystemContentType): number | undefined {
  return systemContentType.id;
}
