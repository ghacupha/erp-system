jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouDepreciationEntryReportItem, RouDepreciationEntryReportItem } from '../rou-depreciation-entry-report-item.model';
import { RouDepreciationEntryReportItemService } from '../service/rou-depreciation-entry-report-item.service';

import { RouDepreciationEntryReportItemRoutingResolveService } from './rou-depreciation-entry-report-item-routing-resolve.service';

describe('RouDepreciationEntryReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouDepreciationEntryReportItemRoutingResolveService;
  let service: RouDepreciationEntryReportItemService;
  let resultRouDepreciationEntryReportItem: IRouDepreciationEntryReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouDepreciationEntryReportItemRoutingResolveService);
    service = TestBed.inject(RouDepreciationEntryReportItemService);
    resultRouDepreciationEntryReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRouDepreciationEntryReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationEntryReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationEntryReportItem).toEqual({ id: 123 });
    });

    it('should return new IRouDepreciationEntryReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationEntryReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouDepreciationEntryReportItem).toEqual(new RouDepreciationEntryReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouDepreciationEntryReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationEntryReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationEntryReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
