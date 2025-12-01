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

import dayjs from 'dayjs';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeasePaymentUploadRequest {
  leaseContractId: number;
  launchBatchImmediately?: boolean;
}

export interface ILeasePaymentUploadResponse {
  uploadId?: number;
  csvFileId?: number;
  storedFileName?: string;
  uploadStatus?: string;
}

export interface ICsvFileUploadSummary {
  id?: number;
  storedFileName?: string | null;
  processed?: boolean | null;
}

export interface ILeasePaymentUploadRecord {
  id?: number;
  uploadStatus?: string | null;
  createdAt?: dayjs.Dayjs | null;
  active?: boolean | null;
  csvFileUpload?: ICsvFileUploadSummary | null;
  leaseContract?: IIFRS16LeaseContract | null;
}
