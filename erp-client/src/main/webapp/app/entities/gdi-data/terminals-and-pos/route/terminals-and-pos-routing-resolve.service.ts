import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITerminalsAndPOS, TerminalsAndPOS } from '../terminals-and-pos.model';
import { TerminalsAndPOSService } from '../service/terminals-and-pos.service';

@Injectable({ providedIn: 'root' })
export class TerminalsAndPOSRoutingResolveService implements Resolve<ITerminalsAndPOS> {
  constructor(protected service: TerminalsAndPOSService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITerminalsAndPOS> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((terminalsAndPOS: HttpResponse<TerminalsAndPOS>) => {
          if (terminalsAndPOS.body) {
            return of(terminalsAndPOS.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TerminalsAndPOS());
  }
}
