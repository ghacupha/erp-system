import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbRecordFileType, CrbRecordFileType } from '../crb-record-file-type.model';
import { CrbRecordFileTypeService } from '../service/crb-record-file-type.service';

@Injectable({ providedIn: 'root' })
export class CrbRecordFileTypeRoutingResolveService implements Resolve<ICrbRecordFileType> {
  constructor(protected service: CrbRecordFileTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbRecordFileType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbRecordFileType: HttpResponse<CrbRecordFileType>) => {
          if (crbRecordFileType.body) {
            return of(crbRecordFileType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbRecordFileType());
  }
}
