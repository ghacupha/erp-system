import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouModelMetadata, RouModelMetadata } from '../rou-model-metadata.model';
import { RouModelMetadataService } from '../service/rou-model-metadata.service';

@Injectable({ providedIn: 'root' })
export class RouModelMetadataRoutingResolveService implements Resolve<IRouModelMetadata> {
  constructor(protected service: RouModelMetadataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouModelMetadata> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouModelMetadata: HttpResponse<RouModelMetadata>) => {
          if (rouModelMetadata.body) {
            return of(rouModelMetadata.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouModelMetadata());
  }
}
