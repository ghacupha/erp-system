jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FileTypeService } from '../service/file-type.service';
import { IFileType, FileType } from '../file-type.model';

import { FileTypeUpdateComponent } from './file-type-update.component';

describe('Component Tests', () => {
  describe('FileType Management Update Component', () => {
    let comp: FileTypeUpdateComponent;
    let fixture: ComponentFixture<FileTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fileTypeService: FileTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FileTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FileTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fileTypeService = TestBed.inject(FileTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fileType: IFileType = { id: 456 };

        activatedRoute.data = of({ fileType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fileType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileType>>();
        const fileType = { id: 123 };
        jest.spyOn(fileTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fileTypeService.update).toHaveBeenCalledWith(fileType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileType>>();
        const fileType = new FileType();
        jest.spyOn(fileTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileType }));
        saveSubject.complete();

        // THEN
        expect(fileTypeService.create).toHaveBeenCalledWith(fileType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileType>>();
        const fileType = { id: 123 };
        jest.spyOn(fileTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fileTypeService.update).toHaveBeenCalledWith(fileType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
