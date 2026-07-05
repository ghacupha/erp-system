import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoryOfSecurity, CategoryOfSecurity } from '../category-of-security.model';
import { CategoryOfSecurityService } from '../service/category-of-security.service';

@Injectable({ providedIn: 'root' })
export class CategoryOfSecurityRoutingResolveService implements Resolve<ICategoryOfSecurity> {
  constructor(protected service: CategoryOfSecurityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoryOfSecurity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoryOfSecurity: HttpResponse<CategoryOfSecurity>) => {
          if (categoryOfSecurity.body) {
            return of(categoryOfSecurity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CategoryOfSecurity());
  }
}
