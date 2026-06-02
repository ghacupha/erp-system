import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IUniversallyUniqueMapping {
  id?: number;
  universalKey?: string;
  mappedValue?: string | null;
  parentMapping?: IUniversallyUniqueMapping | null;
  placeholders?: IPlaceholder[] | null;
}

export class UniversallyUniqueMapping implements IUniversallyUniqueMapping {
  constructor(
    public id?: number,
    public universalKey?: string,
    public mappedValue?: string | null,
    public parentMapping?: IUniversallyUniqueMapping | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getUniversallyUniqueMappingIdentifier(universallyUniqueMapping: IUniversallyUniqueMapping): number | undefined {
  return universallyUniqueMapping.id;
}
