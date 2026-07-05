jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICreditCardFacility, CreditCardFacility } from '../credit-card-facility.model';
import { CreditCardFacilityService } from '../service/credit-card-facility.service';

import { CreditCardFacilityRoutingResolveService } from './credit-card-facility-routing-resolve.service';

describe('CreditCardFacility routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CreditCardFacilityRoutingResolveService;
  let service: CreditCardFacilityService;
  let resultCreditCardFacility: ICreditCardFacility | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CreditCardFacilityRoutingResolveService);
    service = TestBed.inject(CreditCardFacilityService);
    resultCreditCardFacility = undefined;
  });

  describe('resolve', () => {
    it('should return ICreditCardFacility returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditCardFacility = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditCardFacility).toEqual({ id: 123 });
    });

    it('should return new ICreditCardFacility if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditCardFacility = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCreditCardFacility).toEqual(new CreditCardFacility());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CreditCardFacility })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditCardFacility = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditCardFacility).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
