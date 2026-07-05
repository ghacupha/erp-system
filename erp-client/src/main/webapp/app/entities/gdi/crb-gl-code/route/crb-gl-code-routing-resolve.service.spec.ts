jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbGlCode, CrbGlCode } from '../crb-gl-code.model';
import { CrbGlCodeService } from '../service/crb-gl-code.service';

import { CrbGlCodeRoutingResolveService } from './crb-gl-code-routing-resolve.service';

describe('CrbGlCode routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbGlCodeRoutingResolveService;
  let service: CrbGlCodeService;
  let resultCrbGlCode: ICrbGlCode | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbGlCodeRoutingResolveService);
    service = TestBed.inject(CrbGlCodeService);
    resultCrbGlCode = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbGlCode returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbGlCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbGlCode).toEqual({ id: 123 });
    });

    it('should return new ICrbGlCode if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbGlCode = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbGlCode).toEqual(new CrbGlCode());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbGlCode })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbGlCode = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbGlCode).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
