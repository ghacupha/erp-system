import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionBase, QuestionBase } from '../question-base.model';
import { QuestionBaseService } from '../service/question-base.service';

@Injectable({ providedIn: 'root' })
export class QuestionBaseRoutingResolveService implements Resolve<IQuestionBase> {
  constructor(protected service: QuestionBaseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionBase> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionBase: HttpResponse<QuestionBase>) => {
          if (questionBase.body) {
            return of(questionBase.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionBase());
  }
}
