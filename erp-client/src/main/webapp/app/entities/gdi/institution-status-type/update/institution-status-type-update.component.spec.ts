jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InstitutionStatusTypeService } from '../service/institution-status-type.service';
import { IInstitutionStatusType, InstitutionStatusType } from '../institution-status-type.model';

import { InstitutionStatusTypeUpdateComponent } from './institution-status-type-update.component';

describe('InstitutionStatusType Management Update Component', () => {
  let comp: InstitutionStatusTypeUpdateComponent;
  let fixture: ComponentFixture<InstitutionStatusTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let institutionStatusTypeService: InstitutionStatusTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InstitutionStatusTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InstitutionStatusTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InstitutionStatusTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    institutionStatusTypeService = TestBed.inject(InstitutionStatusTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const institutionStatusType: IInstitutionStatusType = { id: 456 };

      activatedRoute.data = of({ institutionStatusType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(institutionStatusType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InstitutionStatusType>>();
      const institutionStatusType = { id: 123 };
      jest.spyOn(institutionStatusTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institutionStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: institutionStatusType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(institutionStatusTypeService.update).toHaveBeenCalledWith(institutionStatusType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InstitutionStatusType>>();
      const institutionStatusType = new InstitutionStatusType();
      jest.spyOn(institutionStatusTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institutionStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: institutionStatusType }));
      saveSubject.complete();

      // THEN
      expect(institutionStatusTypeService.create).toHaveBeenCalledWith(institutionStatusType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InstitutionStatusType>>();
      const institutionStatusType = { id: 123 };
      jest.spyOn(institutionStatusTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ institutionStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(institutionStatusTypeService.update).toHaveBeenCalledWith(institutionStatusType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
