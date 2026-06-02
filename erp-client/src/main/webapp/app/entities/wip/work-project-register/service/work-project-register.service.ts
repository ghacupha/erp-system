import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWorkProjectRegister, getWorkProjectRegisterIdentifier } from '../work-project-register.model';

export type EntityResponseType = HttpResponse<IWorkProjectRegister>;
export type EntityArrayResponseType = HttpResponse<IWorkProjectRegister[]>;

@Injectable({ providedIn: 'root' })
export class WorkProjectRegisterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-project-registers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/work-project-registers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workProjectRegister: IWorkProjectRegister): Observable<EntityResponseType> {
    return this.http.post<IWorkProjectRegister>(this.resourceUrl, workProjectRegister, { observe: 'response' });
  }

  update(workProjectRegister: IWorkProjectRegister): Observable<EntityResponseType> {
    return this.http.put<IWorkProjectRegister>(
      `${this.resourceUrl}/${getWorkProjectRegisterIdentifier(workProjectRegister) as number}`,
      workProjectRegister,
      { observe: 'response' }
    );
  }

  partialUpdate(workProjectRegister: IWorkProjectRegister): Observable<EntityResponseType> {
    return this.http.patch<IWorkProjectRegister>(
      `${this.resourceUrl}/${getWorkProjectRegisterIdentifier(workProjectRegister) as number}`,
      workProjectRegister,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkProjectRegister>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkProjectRegister[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkProjectRegister[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addWorkProjectRegisterToCollectionIfMissing(
    workProjectRegisterCollection: IWorkProjectRegister[],
    ...workProjectRegistersToCheck: (IWorkProjectRegister | null | undefined)[]
  ): IWorkProjectRegister[] {
    const workProjectRegisters: IWorkProjectRegister[] = workProjectRegistersToCheck.filter(isPresent);
    if (workProjectRegisters.length > 0) {
      const workProjectRegisterCollectionIdentifiers = workProjectRegisterCollection.map(
        workProjectRegisterItem => getWorkProjectRegisterIdentifier(workProjectRegisterItem)!
      );
      const workProjectRegistersToAdd = workProjectRegisters.filter(workProjectRegisterItem => {
        const workProjectRegisterIdentifier = getWorkProjectRegisterIdentifier(workProjectRegisterItem);
        if (workProjectRegisterIdentifier == null || workProjectRegisterCollectionIdentifiers.includes(workProjectRegisterIdentifier)) {
          return false;
        }
        workProjectRegisterCollectionIdentifiers.push(workProjectRegisterIdentifier);
        return true;
      });
      return [...workProjectRegistersToAdd, ...workProjectRegisterCollection];
    }
    return workProjectRegisterCollection;
  }
}
