import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIsoCountryCode, IsoCountryCode } from '../iso-country-code.model';
import { IsoCountryCodeService } from '../service/iso-country-code.service';

@Injectable({ providedIn: 'root' })
export class IsoCountryCodeRoutingResolveService implements Resolve<IIsoCountryCode> {
  constructor(protected service: IsoCountryCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIsoCountryCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((isoCountryCode: HttpResponse<IsoCountryCode>) => {
          if (isoCountryCode.body) {
            return of(isoCountryCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IsoCountryCode());
  }
}
