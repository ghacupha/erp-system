import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILiabilityEnumeration {
  id?: number;
  interestRate?: number;
  interestRateText?: string;
  timeGranularity?: string;
  active?: boolean;
  requestDateTime?: string;
  leaseContractId?: number;
  leaseContract?: IIFRS16LeaseContract;
  leasePaymentUploadId?: number;
}

export interface IPresentValueEnumeration {
  id?: number;
  sequenceNumber?: number;
  paymentDate?: string;
  paymentAmount?: number;
  discountRate?: number;
  presentValue?: number;
  leaseContractId?: number;
  liabilityEnumerationId?: number;
}

export interface LiabilityEnumerationRequest {
  leaseContractId: number;
  leasePaymentUploadId: number;
  interestRate: string;
  timeGranularity: string;
  active?: boolean;
}

export interface LiabilityEnumerationResponse {
  liabilityEnumerationId?: number;
  leaseAmortizationCalculationId?: number;
  numberOfPeriods?: number;
  periodicity?: string;
  totalPresentValue?: number;
  discountRatePerPeriod?: number;
}
