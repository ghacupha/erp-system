import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISourceRemittancePurposeType, SourceRemittancePurposeType } from '../source-remittance-purpose-type.model';
import { SourceRemittancePurposeTypeService } from '../service/source-remittance-purpose-type.service';

@Injectable({ providedIn: 'root' })
export class SourceRemittancePurposeTypeRoutingResolveService implements Resolve<ISourceRemittancePurposeType> {
  constructor(protected service: SourceRemittancePurposeTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISourceRemittancePurposeType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sourceRemittancePurposeType: HttpResponse<SourceRemittancePurposeType>) => {
          if (sourceRemittancePurposeType.body) {
            return of(sourceRemittancePurposeType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SourceRemittancePurposeType());
  }
}
