jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbRecordFileType, CrbRecordFileType } from '../crb-record-file-type.model';
import { CrbRecordFileTypeService } from '../service/crb-record-file-type.service';

import { CrbRecordFileTypeRoutingResolveService } from './crb-record-file-type-routing-resolve.service';

describe('CrbRecordFileType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbRecordFileTypeRoutingResolveService;
  let service: CrbRecordFileTypeService;
  let resultCrbRecordFileType: ICrbRecordFileType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbRecordFileTypeRoutingResolveService);
    service = TestBed.inject(CrbRecordFileTypeService);
    resultCrbRecordFileType = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbRecordFileType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbRecordFileType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbRecordFileType).toEqual({ id: 123 });
    });

    it('should return new ICrbRecordFileType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbRecordFileType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbRecordFileType).toEqual(new CrbRecordFileType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbRecordFileType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbRecordFileType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbRecordFileType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
