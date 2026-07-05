import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseModelMetadata, LeaseModelMetadata } from '../lease-model-metadata.model';
import { LeaseModelMetadataService } from '../service/lease-model-metadata.service';

@Injectable({ providedIn: 'root' })
export class LeaseModelMetadataRoutingResolveService implements Resolve<ILeaseModelMetadata> {
  constructor(protected service: LeaseModelMetadataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseModelMetadata> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseModelMetadata: HttpResponse<LeaseModelMetadata>) => {
          if (leaseModelMetadata.body) {
            return of(leaseModelMetadata.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseModelMetadata());
  }
}
