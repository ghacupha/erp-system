export interface IContractStatus {
  id?: number;
  contractStatusCode?: string;
  contractStatusType?: string;
  contractStatusTypeDescription?: string | null;
}

export class ContractStatus implements IContractStatus {
  constructor(
    public id?: number,
    public contractStatusCode?: string,
    public contractStatusType?: string,
    public contractStatusTypeDescription?: string | null
  ) {}
}

export function getContractStatusIdentifier(contractStatus: IContractStatus): number | undefined {
  return contractStatus.id;
}
