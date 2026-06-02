import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractMetadata, ContractMetadata } from '../contract-metadata.model';
import { ContractMetadataService } from '../service/contract-metadata.service';

@Injectable({ providedIn: 'root' })
export class ContractMetadataRoutingResolveService implements Resolve<IContractMetadata> {
  constructor(protected service: ContractMetadataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContractMetadata> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contractMetadata: HttpResponse<ContractMetadata>) => {
          if (contractMetadata.body) {
            return of(contractMetadata.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContractMetadata());
  }
}
