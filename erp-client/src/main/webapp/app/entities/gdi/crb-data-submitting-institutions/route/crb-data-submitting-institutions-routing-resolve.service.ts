import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbDataSubmittingInstitutions, CrbDataSubmittingInstitutions } from '../crb-data-submitting-institutions.model';
import { CrbDataSubmittingInstitutionsService } from '../service/crb-data-submitting-institutions.service';

@Injectable({ providedIn: 'root' })
export class CrbDataSubmittingInstitutionsRoutingResolveService implements Resolve<ICrbDataSubmittingInstitutions> {
  constructor(protected service: CrbDataSubmittingInstitutionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbDataSubmittingInstitutions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbDataSubmittingInstitutions: HttpResponse<CrbDataSubmittingInstitutions>) => {
          if (crbDataSubmittingInstitutions.body) {
            return of(crbDataSubmittingInstitutions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbDataSubmittingInstitutions());
  }
}
