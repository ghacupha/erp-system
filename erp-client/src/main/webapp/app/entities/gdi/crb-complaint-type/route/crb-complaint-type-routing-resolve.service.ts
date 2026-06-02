import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbComplaintType, CrbComplaintType } from '../crb-complaint-type.model';
import { CrbComplaintTypeService } from '../service/crb-complaint-type.service';

@Injectable({ providedIn: 'root' })
export class CrbComplaintTypeRoutingResolveService implements Resolve<ICrbComplaintType> {
  constructor(protected service: CrbComplaintTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbComplaintType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbComplaintType: HttpResponse<CrbComplaintType>) => {
          if (crbComplaintType.body) {
            return of(crbComplaintType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbComplaintType());
  }
}
