jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetAdditionsReport, AssetAdditionsReport } from '../asset-additions-report.model';
import { AssetAdditionsReportService } from '../service/asset-additions-report.service';

import { AssetAdditionsReportRoutingResolveService } from './asset-additions-report-routing-resolve.service';

describe('AssetAdditionsReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetAdditionsReportRoutingResolveService;
  let service: AssetAdditionsReportService;
  let resultAssetAdditionsReport: IAssetAdditionsReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetAdditionsReportRoutingResolveService);
    service = TestBed.inject(AssetAdditionsReportService);
    resultAssetAdditionsReport = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetAdditionsReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAdditionsReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetAdditionsReport).toEqual({ id: 123 });
    });

    it('should return new IAssetAdditionsReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAdditionsReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetAdditionsReport).toEqual(new AssetAdditionsReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetAdditionsReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAdditionsReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetAdditionsReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
