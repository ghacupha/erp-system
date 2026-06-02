import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityCompilation, LeaseLiabilityCompilation } from '../lease-liability-compilation.model';
import { LeaseLiabilityCompilationService } from '../service/lease-liability-compilation.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityCompilationRoutingResolveService implements Resolve<ILeaseLiabilityCompilation> {
  constructor(protected service: LeaseLiabilityCompilationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityCompilation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityCompilation: HttpResponse<LeaseLiabilityCompilation>) => {
          if (leaseLiabilityCompilation.body) {
            return of(leaseLiabilityCompilation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityCompilation());
  }
}
