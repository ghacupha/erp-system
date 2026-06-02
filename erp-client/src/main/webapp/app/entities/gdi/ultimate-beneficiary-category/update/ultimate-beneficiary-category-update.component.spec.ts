jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UltimateBeneficiaryCategoryService } from '../service/ultimate-beneficiary-category.service';
import { IUltimateBeneficiaryCategory, UltimateBeneficiaryCategory } from '../ultimate-beneficiary-category.model';

import { UltimateBeneficiaryCategoryUpdateComponent } from './ultimate-beneficiary-category-update.component';

describe('UltimateBeneficiaryCategory Management Update Component', () => {
  let comp: UltimateBeneficiaryCategoryUpdateComponent;
  let fixture: ComponentFixture<UltimateBeneficiaryCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ultimateBeneficiaryCategoryService: UltimateBeneficiaryCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [UltimateBeneficiaryCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(UltimateBeneficiaryCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UltimateBeneficiaryCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ultimateBeneficiaryCategoryService = TestBed.inject(UltimateBeneficiaryCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ultimateBeneficiaryCategory: IUltimateBeneficiaryCategory = { id: 456 };

      activatedRoute.data = of({ ultimateBeneficiaryCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ultimateBeneficiaryCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UltimateBeneficiaryCategory>>();
      const ultimateBeneficiaryCategory = { id: 123 };
      jest.spyOn(ultimateBeneficiaryCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ultimateBeneficiaryCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ultimateBeneficiaryCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ultimateBeneficiaryCategoryService.update).toHaveBeenCalledWith(ultimateBeneficiaryCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UltimateBeneficiaryCategory>>();
      const ultimateBeneficiaryCategory = new UltimateBeneficiaryCategory();
      jest.spyOn(ultimateBeneficiaryCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ultimateBeneficiaryCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ultimateBeneficiaryCategory }));
      saveSubject.complete();

      // THEN
      expect(ultimateBeneficiaryCategoryService.create).toHaveBeenCalledWith(ultimateBeneficiaryCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UltimateBeneficiaryCategory>>();
      const ultimateBeneficiaryCategory = { id: 123 };
      jest.spyOn(ultimateBeneficiaryCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ultimateBeneficiaryCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ultimateBeneficiaryCategoryService.update).toHaveBeenCalledWith(ultimateBeneficiaryCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
