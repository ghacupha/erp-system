import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';
import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';

@Injectable({ providedIn: 'root' })
export class CrbSourceOfInformationTypeRoutingResolveService implements Resolve<ICrbSourceOfInformationType> {
  constructor(protected service: CrbSourceOfInformationTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbSourceOfInformationType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbSourceOfInformationType: HttpResponse<CrbSourceOfInformationType>) => {
          if (crbSourceOfInformationType.body) {
            return of(crbSourceOfInformationType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbSourceOfInformationType());
  }
}
