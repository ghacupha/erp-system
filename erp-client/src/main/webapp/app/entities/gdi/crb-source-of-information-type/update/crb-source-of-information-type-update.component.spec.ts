jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';
import { ICrbSourceOfInformationType, CrbSourceOfInformationType } from '../crb-source-of-information-type.model';

import { CrbSourceOfInformationTypeUpdateComponent } from './crb-source-of-information-type-update.component';

describe('CrbSourceOfInformationType Management Update Component', () => {
  let comp: CrbSourceOfInformationTypeUpdateComponent;
  let fixture: ComponentFixture<CrbSourceOfInformationTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbSourceOfInformationTypeService: CrbSourceOfInformationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbSourceOfInformationTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbSourceOfInformationTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbSourceOfInformationTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbSourceOfInformationTypeService = TestBed.inject(CrbSourceOfInformationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbSourceOfInformationType: ICrbSourceOfInformationType = { id: 456 };

      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbSourceOfInformationType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSourceOfInformationType>>();
      const crbSourceOfInformationType = { id: 123 };
      jest.spyOn(crbSourceOfInformationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSourceOfInformationType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbSourceOfInformationTypeService.update).toHaveBeenCalledWith(crbSourceOfInformationType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSourceOfInformationType>>();
      const crbSourceOfInformationType = new CrbSourceOfInformationType();
      jest.spyOn(crbSourceOfInformationTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSourceOfInformationType }));
      saveSubject.complete();

      // THEN
      expect(crbSourceOfInformationTypeService.create).toHaveBeenCalledWith(crbSourceOfInformationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSourceOfInformationType>>();
      const crbSourceOfInformationType = { id: 123 };
      jest.spyOn(crbSourceOfInformationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSourceOfInformationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbSourceOfInformationTypeService.update).toHaveBeenCalledWith(crbSourceOfInformationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
