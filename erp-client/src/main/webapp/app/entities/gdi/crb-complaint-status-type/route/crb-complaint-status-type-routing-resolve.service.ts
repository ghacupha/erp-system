import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbComplaintStatusType, CrbComplaintStatusType } from '../crb-complaint-status-type.model';
import { CrbComplaintStatusTypeService } from '../service/crb-complaint-status-type.service';

@Injectable({ providedIn: 'root' })
export class CrbComplaintStatusTypeRoutingResolveService implements Resolve<ICrbComplaintStatusType> {
  constructor(protected service: CrbComplaintStatusTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbComplaintStatusType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbComplaintStatusType: HttpResponse<CrbComplaintStatusType>) => {
          if (crbComplaintStatusType.body) {
            return of(crbComplaintStatusType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbComplaintStatusType());
  }
}
