export interface IFxCustomerType {
  id?: number;
  foreignExchangeCustomerTypeCode?: string;
  foreignCustomerType?: string;
}

export class FxCustomerType implements IFxCustomerType {
  constructor(public id?: number, public foreignExchangeCustomerTypeCode?: string, public foreignCustomerType?: string) {}
}

export function getFxCustomerTypeIdentifier(fxCustomerType: IFxCustomerType): number | undefined {
  return fxCustomerType.id;
}
