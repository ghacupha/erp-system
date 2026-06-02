import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubCountyCode, SubCountyCode } from '../sub-county-code.model';
import { SubCountyCodeService } from '../service/sub-county-code.service';

@Injectable({ providedIn: 'root' })
export class SubCountyCodeRoutingResolveService implements Resolve<ISubCountyCode> {
  constructor(protected service: SubCountyCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubCountyCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subCountyCode: HttpResponse<SubCountyCode>) => {
          if (subCountyCode.body) {
            return of(subCountyCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubCountyCode());
  }
}
