jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepreciationBatchSequence, DepreciationBatchSequence } from '../depreciation-batch-sequence.model';
import { DepreciationBatchSequenceService } from '../service/depreciation-batch-sequence.service';

import { DepreciationBatchSequenceRoutingResolveService } from './depreciation-batch-sequence-routing-resolve.service';

describe('DepreciationBatchSequence routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepreciationBatchSequenceRoutingResolveService;
  let service: DepreciationBatchSequenceService;
  let resultDepreciationBatchSequence: IDepreciationBatchSequence | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepreciationBatchSequenceRoutingResolveService);
    service = TestBed.inject(DepreciationBatchSequenceService);
    resultDepreciationBatchSequence = undefined;
  });

  describe('resolve', () => {
    it('should return IDepreciationBatchSequence returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationBatchSequence = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationBatchSequence).toEqual({ id: 123 });
    });

    it('should return new IDepreciationBatchSequence if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationBatchSequence = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepreciationBatchSequence).toEqual(new DepreciationBatchSequence());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepreciationBatchSequence })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationBatchSequence = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationBatchSequence).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
