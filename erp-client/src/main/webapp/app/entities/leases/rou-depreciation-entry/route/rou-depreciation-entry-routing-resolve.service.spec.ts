jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouDepreciationEntry, RouDepreciationEntry } from '../rou-depreciation-entry.model';
import { RouDepreciationEntryService } from '../service/rou-depreciation-entry.service';

import { RouDepreciationEntryRoutingResolveService } from './rou-depreciation-entry-routing-resolve.service';

describe('RouDepreciationEntry routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouDepreciationEntryRoutingResolveService;
  let service: RouDepreciationEntryService;
  let resultRouDepreciationEntry: IRouDepreciationEntry | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouDepreciationEntryRoutingResolveService);
    service = TestBed.inject(RouDepreciationEntryService);
    resultRouDepreciationEntry = undefined;
  });

  describe('resolve', () => {
    it('should return IRouDepreciationEntry returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationEntry = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationEntry).toEqual({ id: 123 });
    });

    it('should return new IRouDepreciationEntry if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationEntry = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouDepreciationEntry).toEqual(new RouDepreciationEntry());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouDepreciationEntry })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationEntry = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationEntry).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
