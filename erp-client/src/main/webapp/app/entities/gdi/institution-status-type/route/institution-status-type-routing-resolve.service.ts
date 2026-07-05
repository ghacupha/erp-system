import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstitutionStatusType, InstitutionStatusType } from '../institution-status-type.model';
import { InstitutionStatusTypeService } from '../service/institution-status-type.service';

@Injectable({ providedIn: 'root' })
export class InstitutionStatusTypeRoutingResolveService implements Resolve<IInstitutionStatusType> {
  constructor(protected service: InstitutionStatusTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInstitutionStatusType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((institutionStatusType: HttpResponse<InstitutionStatusType>) => {
          if (institutionStatusType.body) {
            return of(institutionStatusType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InstitutionStatusType());
  }
}
