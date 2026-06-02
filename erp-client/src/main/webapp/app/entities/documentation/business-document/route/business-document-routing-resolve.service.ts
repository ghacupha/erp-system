import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessDocument, BusinessDocument } from '../business-document.model';
import { BusinessDocumentService } from '../service/business-document.service';

@Injectable({ providedIn: 'root' })
export class BusinessDocumentRoutingResolveService implements Resolve<IBusinessDocument> {
  constructor(protected service: BusinessDocumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusinessDocument> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((businessDocument: HttpResponse<BusinessDocument>) => {
          if (businessDocument.body) {
            return of(businessDocument.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusinessDocument());
  }
}
