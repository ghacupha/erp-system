jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbRecordFileTypeService } from '../service/crb-record-file-type.service';
import { ICrbRecordFileType, CrbRecordFileType } from '../crb-record-file-type.model';

import { CrbRecordFileTypeUpdateComponent } from './crb-record-file-type-update.component';

describe('CrbRecordFileType Management Update Component', () => {
  let comp: CrbRecordFileTypeUpdateComponent;
  let fixture: ComponentFixture<CrbRecordFileTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbRecordFileTypeService: CrbRecordFileTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbRecordFileTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbRecordFileTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbRecordFileTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbRecordFileTypeService = TestBed.inject(CrbRecordFileTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbRecordFileType: ICrbRecordFileType = { id: 456 };

      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbRecordFileType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbRecordFileType>>();
      const crbRecordFileType = { id: 123 };
      jest.spyOn(crbRecordFileTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbRecordFileType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbRecordFileTypeService.update).toHaveBeenCalledWith(crbRecordFileType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbRecordFileType>>();
      const crbRecordFileType = new CrbRecordFileType();
      jest.spyOn(crbRecordFileTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbRecordFileType }));
      saveSubject.complete();

      // THEN
      expect(crbRecordFileTypeService.create).toHaveBeenCalledWith(crbRecordFileType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbRecordFileType>>();
      const crbRecordFileType = { id: 123 };
      jest.spyOn(crbRecordFileTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbRecordFileType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbRecordFileTypeService.update).toHaveBeenCalledWith(crbRecordFileType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
