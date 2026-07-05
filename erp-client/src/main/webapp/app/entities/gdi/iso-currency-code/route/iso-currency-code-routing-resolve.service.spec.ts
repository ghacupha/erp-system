jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIsoCurrencyCode, IsoCurrencyCode } from '../iso-currency-code.model';
import { IsoCurrencyCodeService } from '../service/iso-currency-code.service';

import { IsoCurrencyCodeRoutingResolveService } from './iso-currency-code-routing-resolve.service';

describe('IsoCurrencyCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IsoCurrencyCodeRoutingResolveService;
  let service: IsoCurrencyCodeService;
  let resultIsoCurrencyCode: IIsoCurrencyCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(IsoCurrencyCodeRoutingResolveService);
    service = TestBed.inject(IsoCurrencyCodeService);
    resultIsoCurrencyCode = undefined;
  });

  describe('resolve', () => {
    it('should return IIsoCurrencyCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsoCurrencyCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIsoCurrencyCode).toEqual({ id: 123 });
    });

    it('should return new IIsoCurrencyCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsoCurrencyCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIsoCurrencyCode).toEqual(new IsoCurrencyCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IsoCurrencyCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsoCurrencyCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIsoCurrencyCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
