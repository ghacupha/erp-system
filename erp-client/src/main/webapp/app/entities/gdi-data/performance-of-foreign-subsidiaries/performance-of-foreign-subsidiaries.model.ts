import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IIsoCountryCode } from 'app/entities/gdi/iso-country-code/iso-country-code.model';

export interface IPerformanceOfForeignSubsidiaries {
  id?: number;
  subsidiaryName?: string;
  reportingDate?: dayjs.Dayjs;
  subsidiaryId?: string;
  grossLoansAmount?: number;
  grossNPALoanAmount?: number;
  grossAssetsAmount?: number;
  grossDepositsAmount?: number;
  profitBeforeTax?: number;
  totalCapitalAdequacyRatio?: number;
  liquidityRatio?: number;
  generalProvisions?: number;
  specificProvisions?: number;
  interestInSuspenseAmount?: number;
  totalNumberOfStaff?: number;
  numberOfBranches?: number;
  bankCode?: IInstitutionCode;
  subsidiaryCountryCode?: IIsoCountryCode;
}

export class PerformanceOfForeignSubsidiaries implements IPerformanceOfForeignSubsidiaries {
  constructor(
    public id?: number,
    public subsidiaryName?: string,
    public reportingDate?: dayjs.Dayjs,
    public subsidiaryId?: string,
    public grossLoansAmount?: number,
    public grossNPALoanAmount?: number,
    public grossAssetsAmount?: number,
    public grossDepositsAmount?: number,
    public profitBeforeTax?: number,
    public totalCapitalAdequacyRatio?: number,
    public liquidityRatio?: number,
    public generalProvisions?: number,
    public specificProvisions?: number,
    public interestInSuspenseAmount?: number,
    public totalNumberOfStaff?: number,
    public numberOfBranches?: number,
    public bankCode?: IInstitutionCode,
    public subsidiaryCountryCode?: IIsoCountryCode
  ) {}
}

export function getPerformanceOfForeignSubsidiariesIdentifier(
  performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries
): number | undefined {
  return performanceOfForeignSubsidiaries.id;
}
