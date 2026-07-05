import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstitutionCode, InstitutionCode } from '../institution-code.model';
import { InstitutionCodeService } from '../service/institution-code.service';

@Injectable({ providedIn: 'root' })
export class InstitutionCodeRoutingResolveService implements Resolve<IInstitutionCode> {
  constructor(protected service: InstitutionCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInstitutionCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((institutionCode: HttpResponse<InstitutionCode>) => {
          if (institutionCode.body) {
            return of(institutionCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InstitutionCode());
  }
}
