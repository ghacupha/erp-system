import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISourcesOfFundsTypeCode, SourcesOfFundsTypeCode } from '../sources-of-funds-type-code.model';
import { SourcesOfFundsTypeCodeService } from '../service/sources-of-funds-type-code.service';

@Injectable({ providedIn: 'root' })
export class SourcesOfFundsTypeCodeRoutingResolveService implements Resolve<ISourcesOfFundsTypeCode> {
  constructor(protected service: SourcesOfFundsTypeCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISourcesOfFundsTypeCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sourcesOfFundsTypeCode: HttpResponse<SourcesOfFundsTypeCode>) => {
          if (sourcesOfFundsTypeCode.body) {
            return of(sourcesOfFundsTypeCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SourcesOfFundsTypeCode());
  }
}
