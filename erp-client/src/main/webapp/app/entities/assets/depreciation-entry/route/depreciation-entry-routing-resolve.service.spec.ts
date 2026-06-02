jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepreciationEntry, DepreciationEntry } from '../depreciation-entry.model';
import { DepreciationEntryService } from '../service/depreciation-entry.service';

import { DepreciationEntryRoutingResolveService } from './depreciation-entry-routing-resolve.service';

describe('DepreciationEntry routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepreciationEntryRoutingResolveService;
  let service: DepreciationEntryService;
  let resultDepreciationEntry: IDepreciationEntry | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepreciationEntryRoutingResolveService);
    service = TestBed.inject(DepreciationEntryService);
    resultDepreciationEntry = undefined;
  });

  describe('resolve', () => {
    it('should return IDepreciationEntry returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationEntry = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationEntry).toEqual({ id: 123 });
    });

    it('should return new IDepreciationEntry if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationEntry = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepreciationEntry).toEqual(new DepreciationEntry());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepreciationEntry })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationEntry = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationEntry).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
