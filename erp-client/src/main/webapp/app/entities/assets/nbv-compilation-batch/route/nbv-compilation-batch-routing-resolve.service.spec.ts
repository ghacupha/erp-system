jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INbvCompilationBatch, NbvCompilationBatch } from '../nbv-compilation-batch.model';
import { NbvCompilationBatchService } from '../service/nbv-compilation-batch.service';

import { NbvCompilationBatchRoutingResolveService } from './nbv-compilation-batch-routing-resolve.service';

describe('NbvCompilationBatch routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NbvCompilationBatchRoutingResolveService;
  let service: NbvCompilationBatchService;
  let resultNbvCompilationBatch: INbvCompilationBatch | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(NbvCompilationBatchRoutingResolveService);
    service = TestBed.inject(NbvCompilationBatchService);
    resultNbvCompilationBatch = undefined;
  });

  describe('resolve', () => {
    it('should return INbvCompilationBatch returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNbvCompilationBatch = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNbvCompilationBatch).toEqual({ id: 123 });
    });

    it('should return new INbvCompilationBatch if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNbvCompilationBatch = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNbvCompilationBatch).toEqual(new NbvCompilationBatch());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NbvCompilationBatch })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNbvCompilationBatch = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNbvCompilationBatch).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
