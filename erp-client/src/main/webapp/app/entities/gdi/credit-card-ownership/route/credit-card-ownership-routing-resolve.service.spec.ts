jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICreditCardOwnership, CreditCardOwnership } from '../credit-card-ownership.model';
import { CreditCardOwnershipService } from '../service/credit-card-ownership.service';

import { CreditCardOwnershipRoutingResolveService } from './credit-card-ownership-routing-resolve.service';

describe('CreditCardOwnership routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CreditCardOwnershipRoutingResolveService;
  let service: CreditCardOwnershipService;
  let resultCreditCardOwnership: ICreditCardOwnership | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CreditCardOwnershipRoutingResolveService);
    service = TestBed.inject(CreditCardOwnershipService);
    resultCreditCardOwnership = undefined;
  });

  describe('resolve', () => {
    it('should return ICreditCardOwnership returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditCardOwnership = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditCardOwnership).toEqual({ id: 123 });
    });

    it('should return new ICreditCardOwnership if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditCardOwnership = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCreditCardOwnership).toEqual(new CreditCardOwnership());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CreditCardOwnership })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCreditCardOwnership = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCreditCardOwnership).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
