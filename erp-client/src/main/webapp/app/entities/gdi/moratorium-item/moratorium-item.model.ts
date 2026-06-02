export interface IMoratoriumItem {
  id?: number;
  moratoriumItemTypeCode?: string;
  moratoriumItemType?: string;
  moratoriumTypeDetails?: string | null;
}

export class MoratoriumItem implements IMoratoriumItem {
  constructor(
    public id?: number,
    public moratoriumItemTypeCode?: string,
    public moratoriumItemType?: string,
    public moratoriumTypeDetails?: string | null
  ) {}
}

export function getMoratoriumItemIdentifier(moratoriumItem: IMoratoriumItem): number | undefined {
  return moratoriumItem.id;
}
