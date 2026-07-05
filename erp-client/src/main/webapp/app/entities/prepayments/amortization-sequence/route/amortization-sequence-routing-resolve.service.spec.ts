jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAmortizationSequence, AmortizationSequence } from '../amortization-sequence.model';
import { AmortizationSequenceService } from '../service/amortization-sequence.service';

import { AmortizationSequenceRoutingResolveService } from './amortization-sequence-routing-resolve.service';

describe('AmortizationSequence routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AmortizationSequenceRoutingResolveService;
  let service: AmortizationSequenceService;
  let resultAmortizationSequence: IAmortizationSequence | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AmortizationSequenceRoutingResolveService);
    service = TestBed.inject(AmortizationSequenceService);
    resultAmortizationSequence = undefined;
  });

  describe('resolve', () => {
    it('should return IAmortizationSequence returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmortizationSequence = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAmortizationSequence).toEqual({ id: 123 });
    });

    it('should return new IAmortizationSequence if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmortizationSequence = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAmortizationSequence).toEqual(new AmortizationSequence());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AmortizationSequence })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmortizationSequence = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAmortizationSequence).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
