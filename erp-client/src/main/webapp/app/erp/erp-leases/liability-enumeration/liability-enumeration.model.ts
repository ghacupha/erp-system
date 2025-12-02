export interface ILiabilityEnumeration {
  id?: number;
  interestRate?: number;
  interestRateText?: string;
  timeGranularity?: string;
  active?: boolean;
  leaseContractId?: number;
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
