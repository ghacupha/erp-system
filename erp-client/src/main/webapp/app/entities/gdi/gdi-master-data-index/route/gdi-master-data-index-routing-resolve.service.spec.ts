jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGdiMasterDataIndex, GdiMasterDataIndex } from '../gdi-master-data-index.model';
import { GdiMasterDataIndexService } from '../service/gdi-master-data-index.service';

import { GdiMasterDataIndexRoutingResolveService } from './gdi-master-data-index-routing-resolve.service';

describe('GdiMasterDataIndex routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GdiMasterDataIndexRoutingResolveService;
  let service: GdiMasterDataIndexService;
  let resultGdiMasterDataIndex: IGdiMasterDataIndex | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(GdiMasterDataIndexRoutingResolveService);
    service = TestBed.inject(GdiMasterDataIndexService);
    resultGdiMasterDataIndex = undefined;
  });

  describe('resolve', () => {
    it('should return IGdiMasterDataIndex returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGdiMasterDataIndex = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGdiMasterDataIndex).toEqual({ id: 123 });
    });

    it('should return new IGdiMasterDataIndex if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGdiMasterDataIndex = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGdiMasterDataIndex).toEqual(new GdiMasterDataIndex());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as GdiMasterDataIndex })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGdiMasterDataIndex = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGdiMasterDataIndex).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
