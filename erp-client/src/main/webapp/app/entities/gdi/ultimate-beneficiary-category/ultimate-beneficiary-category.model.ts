export interface IUltimateBeneficiaryCategory {
  id?: number;
  ultimateBeneficiaryCategoryTypeCode?: string;
  ultimateBeneficiaryType?: string;
  ultimateBeneficiaryCategoryTypeDetails?: string | null;
}

export class UltimateBeneficiaryCategory implements IUltimateBeneficiaryCategory {
  constructor(
    public id?: number,
    public ultimateBeneficiaryCategoryTypeCode?: string,
    public ultimateBeneficiaryType?: string,
    public ultimateBeneficiaryCategoryTypeDetails?: string | null
  ) {}
}

export function getUltimateBeneficiaryCategoryIdentifier(ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory): number | undefined {
  return ultimateBeneficiaryCategory.id;
}
