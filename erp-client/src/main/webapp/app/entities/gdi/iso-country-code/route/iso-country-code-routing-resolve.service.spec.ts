jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIsoCountryCode, IsoCountryCode } from '../iso-country-code.model';
import { IsoCountryCodeService } from '../service/iso-country-code.service';

import { IsoCountryCodeRoutingResolveService } from './iso-country-code-routing-resolve.service';

describe('IsoCountryCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IsoCountryCodeRoutingResolveService;
  let service: IsoCountryCodeService;
  let resultIsoCountryCode: IIsoCountryCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(IsoCountryCodeRoutingResolveService);
    service = TestBed.inject(IsoCountryCodeService);
    resultIsoCountryCode = undefined;
  });

  describe('resolve', () => {
    it('should return IIsoCountryCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsoCountryCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIsoCountryCode).toEqual({ id: 123 });
    });

    it('should return new IIsoCountryCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsoCountryCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIsoCountryCode).toEqual(new IsoCountryCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IsoCountryCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIsoCountryCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIsoCountryCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
