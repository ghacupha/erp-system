jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFileUpload, FileUpload } from '../file-upload.model';
import { FileUploadService } from '../service/file-upload.service';

import { FileUploadRoutingResolveService } from './file-upload-routing-resolve.service';

describe('FileUpload routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FileUploadRoutingResolveService;
  let service: FileUploadService;
  let resultFileUpload: IFileUpload | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FileUploadRoutingResolveService);
    service = TestBed.inject(FileUploadService);
    resultFileUpload = undefined;
  });

  describe('resolve', () => {
    it('should return IFileUpload returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFileUpload = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFileUpload).toEqual({ id: 123 });
    });

    it('should return new IFileUpload if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFileUpload = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFileUpload).toEqual(new FileUpload());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FileUpload })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFileUpload = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFileUpload).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
