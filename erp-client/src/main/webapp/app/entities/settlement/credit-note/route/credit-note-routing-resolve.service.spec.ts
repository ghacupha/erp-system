jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICreditNote, CreditNote } from '../credit-note.model';
import { CreditNoteService } from '../service/credit-note.service';

import { CreditNoteRoutingResolveService } from './credit-note-routing-resolve.service';

describe('CreditNote routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CreditNoteRoutingResolveService;
  let service: CreditNoteService;
  let resultCreditNote: ICreditNote | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CreditNoteRoutingResolveService);
    service = TestBed.inject(CreditNoteService);
    resultCreditNote = undefined;
  });

  describe('resolve', () => {
    it('should return ICreditNote returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditNote = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditNote).toEqual({ id: 123 });
    });

    it('should return new ICreditNote if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditNote = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCreditNote).toEqual(new CreditNote());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CreditNote })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditNote = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditNote).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
