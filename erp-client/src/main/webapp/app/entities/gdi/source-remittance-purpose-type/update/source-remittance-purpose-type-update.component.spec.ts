jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SourceRemittancePurposeTypeService } from '../service/source-remittance-purpose-type.service';
import { ISourceRemittancePurposeType, SourceRemittancePurposeType } from '../source-remittance-purpose-type.model';

import { SourceRemittancePurposeTypeUpdateComponent } from './source-remittance-purpose-type-update.component';

describe('SourceRemittancePurposeType Management Update Component', () => {
  let comp: SourceRemittancePurposeTypeUpdateComponent;
  let fixture: ComponentFixture<SourceRemittancePurposeTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sourceRemittancePurposeTypeService: SourceRemittancePurposeTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SourceRemittancePurposeTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SourceRemittancePurposeTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SourceRemittancePurposeTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sourceRemittancePurposeTypeService = TestBed.inject(SourceRemittancePurposeTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sourceRemittancePurposeType: ISourceRemittancePurposeType = { id: 456 };

      activatedRoute.data = of({ sourceRemittancePurposeType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sourceRemittancePurposeType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SourceRemittancePurposeType>>();
      const sourceRemittancePurposeType = { id: 123 };
      jest.spyOn(sourceRemittancePurposeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sourceRemittancePurposeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sourceRemittancePurposeType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sourceRemittancePurposeTypeService.update).toHaveBeenCalledWith(sourceRemittancePurposeType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SourceRemittancePurposeType>>();
      const sourceRemittancePurposeType = new SourceRemittancePurposeType();
      jest.spyOn(sourceRemittancePurposeTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sourceRemittancePurposeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sourceRemittancePurposeType }));
      saveSubject.complete();

      // THEN
      expect(sourceRemittancePurposeTypeService.create).toHaveBeenCalledWith(sourceRemittancePurposeType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SourceRemittancePurposeType>>();
      const sourceRemittancePurposeType = { id: 123 };
      jest.spyOn(sourceRemittancePurposeTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sourceRemittancePurposeType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sourceRemittancePurposeTypeService.update).toHaveBeenCalledWith(sourceRemittancePurposeType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
