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

import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IMessageToken {
  id?: number;
  description?: string | null;
  timeSent?: number;
  tokenValue?: string;
  received?: boolean | null;
  actioned?: boolean | null;
  contentFullyEnqueued?: boolean | null;
  placeholders?: IPlaceholder[] | null;
}

export class MessageToken implements IMessageToken {
  constructor(
    public id?: number,
    public description?: string | null,
    public timeSent?: number,
    public tokenValue?: string,
    public received?: boolean | null,
    public actioned?: boolean | null,
    public contentFullyEnqueued?: boolean | null,
    public placeholders?: IPlaceholder[] | null
  ) {
    this.received = this.received ?? false;
    this.actioned = this.actioned ?? false;
    this.contentFullyEnqueued = this.contentFullyEnqueued ?? false;
  }
}

export function getMessageTokenIdentifier(messageToken: IMessageToken): number | undefined {
  return messageToken.id;
}
