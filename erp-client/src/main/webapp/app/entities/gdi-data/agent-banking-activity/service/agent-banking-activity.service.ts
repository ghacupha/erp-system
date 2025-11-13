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

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAgentBankingActivity, getAgentBankingActivityIdentifier } from '../agent-banking-activity.model';

export type EntityResponseType = HttpResponse<IAgentBankingActivity>;
export type EntityArrayResponseType = HttpResponse<IAgentBankingActivity[]>;

@Injectable({ providedIn: 'root' })
export class AgentBankingActivityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agent-banking-activities');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/agent-banking-activities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agentBankingActivity: IAgentBankingActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agentBankingActivity);
    return this.http
      .post<IAgentBankingActivity>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agentBankingActivity: IAgentBankingActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agentBankingActivity);
    return this.http
      .put<IAgentBankingActivity>(`${this.resourceUrl}/${getAgentBankingActivityIdentifier(agentBankingActivity) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(agentBankingActivity: IAgentBankingActivity): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agentBankingActivity);
    return this.http
      .patch<IAgentBankingActivity>(`${this.resourceUrl}/${getAgentBankingActivityIdentifier(agentBankingActivity) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgentBankingActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgentBankingActivity[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgentBankingActivity[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAgentBankingActivityToCollectionIfMissing(
    agentBankingActivityCollection: IAgentBankingActivity[],
    ...agentBankingActivitiesToCheck: (IAgentBankingActivity | null | undefined)[]
  ): IAgentBankingActivity[] {
    const agentBankingActivities: IAgentBankingActivity[] = agentBankingActivitiesToCheck.filter(isPresent);
    if (agentBankingActivities.length > 0) {
      const agentBankingActivityCollectionIdentifiers = agentBankingActivityCollection.map(
        agentBankingActivityItem => getAgentBankingActivityIdentifier(agentBankingActivityItem)!
      );
      const agentBankingActivitiesToAdd = agentBankingActivities.filter(agentBankingActivityItem => {
        const agentBankingActivityIdentifier = getAgentBankingActivityIdentifier(agentBankingActivityItem);
        if (agentBankingActivityIdentifier == null || agentBankingActivityCollectionIdentifiers.includes(agentBankingActivityIdentifier)) {
          return false;
        }
        agentBankingActivityCollectionIdentifiers.push(agentBankingActivityIdentifier);
        return true;
      });
      return [...agentBankingActivitiesToAdd, ...agentBankingActivityCollection];
    }
    return agentBankingActivityCollection;
  }

  protected convertDateFromClient(agentBankingActivity: IAgentBankingActivity): IAgentBankingActivity {
    return Object.assign({}, agentBankingActivity, {
      reportingDate: agentBankingActivity.reportingDate?.isValid() ? agentBankingActivity.reportingDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((agentBankingActivity: IAgentBankingActivity) => {
        agentBankingActivity.reportingDate = agentBankingActivity.reportingDate ? dayjs(agentBankingActivity.reportingDate) : undefined;
      });
    }
    return res;
  }
}
