///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import dayjs from 'dayjs';

export interface ILiabilityEnumeration {
  id?: number;
  interestRate?: number;
  interestRateText?: string;
  timeGranularity?: string;
  active?: boolean;
  requestDateTime?: dayjs.Dayjs;
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
