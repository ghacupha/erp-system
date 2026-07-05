jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFileType, FileType } from '../file-type.model';
import { FileTypeService } from '../service/file-type.service';

import { FileTypeRoutingResolveService } from './file-type-routing-resolve.service';

describe('FileType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FileTypeRoutingResolveService;
  let service: FileTypeService;
  let resultFileType: IFileType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FileTypeRoutingResolveService);
    service = TestBed.inject(FileTypeService);
    resultFileType = undefined;
  });

  describe('resolve', () => {
    it('should return IFileType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFileType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFileType).toEqual({ id: 123 });
    });

    it('should return new IFileType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFileType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFileType).toEqual(new FileType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FileType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFileType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFileType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
