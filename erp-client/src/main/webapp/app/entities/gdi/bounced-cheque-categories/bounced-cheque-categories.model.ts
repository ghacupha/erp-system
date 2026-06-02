export interface IBouncedChequeCategories {
  id?: number;
  bouncedChequeCategoryTypeCode?: string;
  bouncedChequeCategoryType?: string;
}

export class BouncedChequeCategories implements IBouncedChequeCategories {
  constructor(public id?: number, public bouncedChequeCategoryTypeCode?: string, public bouncedChequeCategoryType?: string) {}
}

export function getBouncedChequeCategoriesIdentifier(bouncedChequeCategories: IBouncedChequeCategories): number | undefined {
  return bouncedChequeCategories.id;
}
