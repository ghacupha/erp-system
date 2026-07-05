jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReasonsForBouncedCheque, ReasonsForBouncedCheque } from '../reasons-for-bounced-cheque.model';
import { ReasonsForBouncedChequeService } from '../service/reasons-for-bounced-cheque.service';

import { ReasonsForBouncedChequeRoutingResolveService } from './reasons-for-bounced-cheque-routing-resolve.service';

describe('ReasonsForBouncedCheque routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReasonsForBouncedChequeRoutingResolveService;
  let service: ReasonsForBouncedChequeService;
  let resultReasonsForBouncedCheque: IReasonsForBouncedCheque | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ReasonsForBouncedChequeRoutingResolveService);
    service = TestBed.inject(ReasonsForBouncedChequeService);
    resultReasonsForBouncedCheque = undefined;
  });

  describe('resolve', () => {
    it('should return IReasonsForBouncedCheque returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReasonsForBouncedCheque = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReasonsForBouncedCheque).toEqual({ id: 123 });
    });

    it('should return new IReasonsForBouncedCheque if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReasonsForBouncedCheque = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReasonsForBouncedCheque).toEqual(new ReasonsForBouncedCheque());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReasonsForBouncedCheque })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReasonsForBouncedCheque = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReasonsForBouncedCheque).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
