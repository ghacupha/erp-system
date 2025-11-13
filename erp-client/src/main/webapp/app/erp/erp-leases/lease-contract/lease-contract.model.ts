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

import * as dayjs from 'dayjs';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IBusinessDocument } from '../../erp-pages/business-document/business-document.model';
import { IContractMetadata } from '../../erp-pages/contract-metadata/contract-metadata.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

export interface ILeaseContract {
  id?: number;
  bookingId?: string;
  leaseTitle?: string;
  identifier?: string;
  description?: string | null;
  commencementDate?: dayjs.Dayjs;
  terminalDate?: dayjs.Dayjs;
  placeholders?: IPlaceholder[] | null;
  systemMappings?: IUniversallyUniqueMapping[] | null;
  businessDocuments?: IBusinessDocument[] | null;
  contractMetadata?: IContractMetadata[] | null;
}

export class LeaseContract implements ILeaseContract {
  constructor(
    public id?: number,
    public bookingId?: string,
    public leaseTitle?: string,
    public identifier?: string,
    public description?: string | null,
    public commencementDate?: dayjs.Dayjs,
    public terminalDate?: dayjs.Dayjs,
    public placeholders?: IPlaceholder[] | null,
    public systemMappings?: IUniversallyUniqueMapping[] | null,
    public businessDocuments?: IBusinessDocument[] | null,
    public contractMetadata?: IContractMetadata[] | null
  ) {}
}

export function getLeaseContractIdentifier(leaseContract: ILeaseContract): number | undefined {
  return leaseContract.id;
}
