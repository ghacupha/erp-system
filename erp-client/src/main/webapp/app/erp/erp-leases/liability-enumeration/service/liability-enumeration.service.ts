import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  ILiabilityEnumeration,
  IPresentValueEnumeration,
  LiabilityEnumerationRequest,
  LiabilityEnumerationResponse,
} from '../liability-enumeration.model';

export type EntityResponseType = HttpResponse<ILiabilityEnumeration>;
export type EntityArrayResponseType = HttpResponse<ILiabilityEnumeration[]>;
export type PresentValueArrayResponseType = HttpResponse<IPresentValueEnumeration[]>;

@Injectable({ providedIn: 'root' })
export class LiabilityEnumerationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/liability-enumerations');
  protected presentValueUrl = this.applicationConfigService.getEndpointFor('api/leases/present-value-enumerations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  enumerate(request: LiabilityEnumerationRequest): Observable<HttpResponse<LiabilityEnumerationResponse>> {
    return this.http.post<LiabilityEnumerationResponse>(this.resourceUrl, request, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILiabilityEnumeration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILiabilityEnumeration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  presentValues(liabilityEnumerationId?: number, req?: any): Observable<PresentValueArrayResponseType> {
    let params = createRequestOption(req).set('liabilityEnumerationId', liabilityEnumerationId ?? '');
    if (!liabilityEnumerationId) {
      params = params.delete('liabilityEnumerationId');
    }
    return this.http.get<IPresentValueEnumeration[]>(this.presentValueUrl, { params, observe: 'response' });
  }
}
