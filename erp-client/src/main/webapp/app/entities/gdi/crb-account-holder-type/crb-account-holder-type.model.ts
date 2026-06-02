export interface ICrbAccountHolderType {
  id?: number;
  accountHolderCategoryTypeCode?: string;
  accountHolderCategoryType?: string;
}

export class CrbAccountHolderType implements ICrbAccountHolderType {
  constructor(public id?: number, public accountHolderCategoryTypeCode?: string, public accountHolderCategoryType?: string) {}
}

export function getCrbAccountHolderTypeIdentifier(crbAccountHolderType: ICrbAccountHolderType): number | undefined {
  return crbAccountHolderType.id;
}
