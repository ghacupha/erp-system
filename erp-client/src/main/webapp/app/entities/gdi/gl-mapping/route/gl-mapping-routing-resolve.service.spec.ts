jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGlMapping, GlMapping } from '../gl-mapping.model';
import { GlMappingService } from '../service/gl-mapping.service';

import { GlMappingRoutingResolveService } from './gl-mapping-routing-resolve.service';

describe('GlMapping routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GlMappingRoutingResolveService;
  let service: GlMappingService;
  let resultGlMapping: IGlMapping | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(GlMappingRoutingResolveService);
    service = TestBed.inject(GlMappingService);
    resultGlMapping = undefined;
  });

  describe('resolve', () => {
    it('should return IGlMapping returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGlMapping = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGlMapping).toEqual({ id: 123 });
    });

    it('should return new IGlMapping if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGlMapping = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGlMapping).toEqual(new GlMapping());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GlMapping })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGlMapping = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGlMapping).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
