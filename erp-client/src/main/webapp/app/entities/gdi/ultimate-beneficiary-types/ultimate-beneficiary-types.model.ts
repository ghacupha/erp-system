export interface IUltimateBeneficiaryTypes {
  id?: number;
  ultimateBeneficiaryTypeCode?: string;
  ultimateBeneficiaryType?: string;
  ultimateBeneficiaryTypeDetails?: string | null;
}

export class UltimateBeneficiaryTypes implements IUltimateBeneficiaryTypes {
  constructor(
    public id?: number,
    public ultimateBeneficiaryTypeCode?: string,
    public ultimateBeneficiaryType?: string,
    public ultimateBeneficiaryTypeDetails?: string | null
  ) {}
}

export function getUltimateBeneficiaryTypesIdentifier(ultimateBeneficiaryTypes: IUltimateBeneficiaryTypes): number | undefined {
  return ultimateBeneficiaryTypes.id;
}
