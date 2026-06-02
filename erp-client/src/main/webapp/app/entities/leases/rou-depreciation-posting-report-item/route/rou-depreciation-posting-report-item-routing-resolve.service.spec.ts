jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouDepreciationPostingReportItem, RouDepreciationPostingReportItem } from '../rou-depreciation-posting-report-item.model';
import { RouDepreciationPostingReportItemService } from '../service/rou-depreciation-posting-report-item.service';

import { RouDepreciationPostingReportItemRoutingResolveService } from './rou-depreciation-posting-report-item-routing-resolve.service';

describe('RouDepreciationPostingReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouDepreciationPostingReportItemRoutingResolveService;
  let service: RouDepreciationPostingReportItemService;
  let resultRouDepreciationPostingReportItem: IRouDepreciationPostingReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouDepreciationPostingReportItemRoutingResolveService);
    service = TestBed.inject(RouDepreciationPostingReportItemService);
    resultRouDepreciationPostingReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRouDepreciationPostingReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationPostingReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationPostingReportItem).toEqual({ id: 123 });
    });

    it('should return new IRouDepreciationPostingReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationPostingReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouDepreciationPostingReportItem).toEqual(new RouDepreciationPostingReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouDepreciationPostingReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationPostingReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationPostingReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
