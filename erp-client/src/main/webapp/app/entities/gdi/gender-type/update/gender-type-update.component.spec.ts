jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GenderTypeService } from '../service/gender-type.service';
import { IGenderType, GenderType } from '../gender-type.model';

import { GenderTypeUpdateComponent } from './gender-type-update.component';

describe('GenderType Management Update Component', () => {
  let comp: GenderTypeUpdateComponent;
  let fixture: ComponentFixture<GenderTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let genderTypeService: GenderTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GenderTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(GenderTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GenderTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    genderTypeService = TestBed.inject(GenderTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const genderType: IGenderType = { id: 456 };

      activatedRoute.data = of({ genderType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(genderType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GenderType>>();
      const genderType = { id: 123 };
      jest.spyOn(genderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: genderType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(genderTypeService.update).toHaveBeenCalledWith(genderType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GenderType>>();
      const genderType = new GenderType();
      jest.spyOn(genderTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: genderType }));
      saveSubject.complete();

      // THEN
      expect(genderTypeService.create).toHaveBeenCalledWith(genderType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GenderType>>();
      const genderType = { id: 123 };
      jest.spyOn(genderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(genderTypeService.update).toHaveBeenCalledWith(genderType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
