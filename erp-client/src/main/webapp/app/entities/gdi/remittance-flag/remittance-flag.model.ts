import { RemittanceTypeFlag } from 'app/entities/enumerations/remittance-type-flag.model';
import { RemittanceType } from 'app/entities/enumerations/remittance-type.model';

export interface IRemittanceFlag {
  id?: number;
  remittanceTypeFlag?: RemittanceTypeFlag;
  remittanceType?: RemittanceType;
  remittanceTypeDetails?: string | null;
}

export class RemittanceFlag implements IRemittanceFlag {
  constructor(
    public id?: number,
    public remittanceTypeFlag?: RemittanceTypeFlag,
    public remittanceType?: RemittanceType,
    public remittanceTypeDetails?: string | null
  ) {}
}

export function getRemittanceFlagIdentifier(remittanceFlag: IRemittanceFlag): number | undefined {
  return remittanceFlag.id;
}
