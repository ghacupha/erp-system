import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISecurityClassificationType, SecurityClassificationType } from '../security-classification-type.model';
import { SecurityClassificationTypeService } from '../service/security-classification-type.service';

@Injectable({ providedIn: 'root' })
export class SecurityClassificationTypeRoutingResolveService implements Resolve<ISecurityClassificationType> {
  constructor(protected service: SecurityClassificationTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISecurityClassificationType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((securityClassificationType: HttpResponse<SecurityClassificationType>) => {
          if (securityClassificationType.body) {
            return of(securityClassificationType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SecurityClassificationType());
  }
}
