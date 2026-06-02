import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationBatchSequence, DepreciationBatchSequence } from '../depreciation-batch-sequence.model';
import { DepreciationBatchSequenceService } from '../service/depreciation-batch-sequence.service';

@Injectable({ providedIn: 'root' })
export class DepreciationBatchSequenceRoutingResolveService implements Resolve<IDepreciationBatchSequence> {
  constructor(protected service: DepreciationBatchSequenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationBatchSequence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationBatchSequence: HttpResponse<DepreciationBatchSequence>) => {
          if (depreciationBatchSequence.body) {
            return of(depreciationBatchSequence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationBatchSequence());
  }
}
