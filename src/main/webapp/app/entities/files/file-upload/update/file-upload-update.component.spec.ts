jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FileUploadService } from '../service/file-upload.service';
import { IFileUpload, FileUpload } from '../file-upload.model';

import { FileUploadUpdateComponent } from './file-upload-update.component';

describe('Component Tests', () => {
  describe('FileUpload Management Update Component', () => {
    let comp: FileUploadUpdateComponent;
    let fixture: ComponentFixture<FileUploadUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fileUploadService: FileUploadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FileUploadUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FileUploadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileUploadUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fileUploadService = TestBed.inject(FileUploadService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fileUpload: IFileUpload = { id: 456 };

        activatedRoute.data = of({ fileUpload });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fileUpload));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileUpload>>();
        const fileUpload = { id: 123 };
        jest.spyOn(fileUploadService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileUpload });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileUpload }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fileUploadService.update).toHaveBeenCalledWith(fileUpload);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileUpload>>();
        const fileUpload = new FileUpload();
        jest.spyOn(fileUploadService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileUpload });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileUpload }));
        saveSubject.complete();

        // THEN
        expect(fileUploadService.create).toHaveBeenCalledWith(fileUpload);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileUpload>>();
        const fileUpload = { id: 123 };
        jest.spyOn(fileUploadService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileUpload });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fileUploadService.update).toHaveBeenCalledWith(fileUpload);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
