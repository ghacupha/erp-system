import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfessionalQualification, ProfessionalQualification } from '../professional-qualification.model';
import { ProfessionalQualificationService } from '../service/professional-qualification.service';

@Injectable({ providedIn: 'root' })
export class ProfessionalQualificationRoutingResolveService implements Resolve<IProfessionalQualification> {
  constructor(protected service: ProfessionalQualificationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfessionalQualification> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((professionalQualification: HttpResponse<ProfessionalQualification>) => {
          if (professionalQualification.body) {
            return of(professionalQualification.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProfessionalQualification());
  }
}
