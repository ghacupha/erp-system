import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUnallocatedPrepaymentAccountReport } from '../unallocated-prepayment-account-report.model';

export type EntityArrayResponseType = HttpResponse<IUnallocatedPrepaymentAccountReport[]>;

@Injectable({ providedIn: 'root' })
export class UnallocatedPrepaymentAccountReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayments/unallocated-prepayment-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUnallocatedPrepaymentAccountReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }
}
