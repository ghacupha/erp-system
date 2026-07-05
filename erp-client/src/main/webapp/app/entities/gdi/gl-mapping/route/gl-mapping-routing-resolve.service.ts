import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGlMapping, GlMapping } from '../gl-mapping.model';
import { GlMappingService } from '../service/gl-mapping.service';

@Injectable({ providedIn: 'root' })
export class GlMappingRoutingResolveService implements Resolve<IGlMapping> {
  constructor(protected service: GlMappingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGlMapping> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((glMapping: HttpResponse<GlMapping>) => {
          if (glMapping.body) {
            return of(glMapping.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GlMapping());
  }
}
