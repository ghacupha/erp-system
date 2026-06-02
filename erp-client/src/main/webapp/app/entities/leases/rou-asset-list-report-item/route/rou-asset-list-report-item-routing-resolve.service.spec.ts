jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouAssetListReportItem, RouAssetListReportItem } from '../rou-asset-list-report-item.model';
import { RouAssetListReportItemService } from '../service/rou-asset-list-report-item.service';

import { RouAssetListReportItemRoutingResolveService } from './rou-asset-list-report-item-routing-resolve.service';

describe('RouAssetListReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouAssetListReportItemRoutingResolveService;
  let service: RouAssetListReportItemService;
  let resultRouAssetListReportItem: IRouAssetListReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouAssetListReportItemRoutingResolveService);
    service = TestBed.inject(RouAssetListReportItemService);
    resultRouAssetListReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRouAssetListReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAssetListReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouAssetListReportItem).toEqual({ id: 123 });
    });

    it('should return new IRouAssetListReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAssetListReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouAssetListReportItem).toEqual(new RouAssetListReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouAssetListReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAssetListReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouAssetListReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
