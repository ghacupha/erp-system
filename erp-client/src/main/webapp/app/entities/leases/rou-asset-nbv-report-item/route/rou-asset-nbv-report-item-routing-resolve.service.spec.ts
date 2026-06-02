jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouAssetNBVReportItem, RouAssetNBVReportItem } from '../rou-asset-nbv-report-item.model';
import { RouAssetNBVReportItemService } from '../service/rou-asset-nbv-report-item.service';

import { RouAssetNBVReportItemRoutingResolveService } from './rou-asset-nbv-report-item-routing-resolve.service';

describe('RouAssetNBVReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouAssetNBVReportItemRoutingResolveService;
  let service: RouAssetNBVReportItemService;
  let resultRouAssetNBVReportItem: IRouAssetNBVReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouAssetNBVReportItemRoutingResolveService);
    service = TestBed.inject(RouAssetNBVReportItemService);
    resultRouAssetNBVReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRouAssetNBVReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAssetNBVReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouAssetNBVReportItem).toEqual({ id: 123 });
    });

    it('should return new IRouAssetNBVReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAssetNBVReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouAssetNBVReportItem).toEqual(new RouAssetNBVReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouAssetNBVReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAssetNBVReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouAssetNBVReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
