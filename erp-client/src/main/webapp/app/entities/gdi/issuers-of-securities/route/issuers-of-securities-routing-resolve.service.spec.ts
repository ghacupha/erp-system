jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIssuersOfSecurities, IssuersOfSecurities } from '../issuers-of-securities.model';
import { IssuersOfSecuritiesService } from '../service/issuers-of-securities.service';

import { IssuersOfSecuritiesRoutingResolveService } from './issuers-of-securities-routing-resolve.service';

describe('IssuersOfSecurities routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IssuersOfSecuritiesRoutingResolveService;
  let service: IssuersOfSecuritiesService;
  let resultIssuersOfSecurities: IIssuersOfSecurities | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(IssuersOfSecuritiesRoutingResolveService);
    service = TestBed.inject(IssuersOfSecuritiesService);
    resultIssuersOfSecurities = undefined;
  });

  describe('resolve', () => {
    it('should return IIssuersOfSecurities returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIssuersOfSecurities = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIssuersOfSecurities).toEqual({ id: 123 });
    });

    it('should return new IIssuersOfSecurities if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIssuersOfSecurities = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIssuersOfSecurities).toEqual(new IssuersOfSecurities());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IssuersOfSecurities })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIssuersOfSecurities = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIssuersOfSecurities).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
