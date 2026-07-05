jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISystemModule, SystemModule } from '../system-module.model';
import { SystemModuleService } from '../service/system-module.service';

import { SystemModuleRoutingResolveService } from './system-module-routing-resolve.service';

describe('SystemModule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SystemModuleRoutingResolveService;
  let service: SystemModuleService;
  let resultSystemModule: ISystemModule | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SystemModuleRoutingResolveService);
    service = TestBed.inject(SystemModuleService);
    resultSystemModule = undefined;
  });

  describe('resolve', () => {
    it('should return ISystemModule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemModule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSystemModule).toEqual({ id: 123 });
    });

    it('should return new ISystemModule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemModule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSystemModule).toEqual(new SystemModule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SystemModule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSystemModule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSystemModule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
