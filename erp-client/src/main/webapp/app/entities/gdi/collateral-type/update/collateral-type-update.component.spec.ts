jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CollateralTypeService } from '../service/collateral-type.service';
import { ICollateralType, CollateralType } from '../collateral-type.model';

import { CollateralTypeUpdateComponent } from './collateral-type-update.component';

describe('CollateralType Management Update Component', () => {
  let comp: CollateralTypeUpdateComponent;
  let fixture: ComponentFixture<CollateralTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collateralTypeService: CollateralTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CollateralTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CollateralTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollateralTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collateralTypeService = TestBed.inject(CollateralTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const collateralType: ICollateralType = { id: 456 };

      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(collateralType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralType>>();
      const collateralType = { id: 123 };
      jest.spyOn(collateralTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collateralType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(collateralTypeService.update).toHaveBeenCalledWith(collateralType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralType>>();
      const collateralType = new CollateralType();
      jest.spyOn(collateralTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collateralType }));
      saveSubject.complete();

      // THEN
      expect(collateralTypeService.create).toHaveBeenCalledWith(collateralType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CollateralType>>();
      const collateralType = { id: 123 };
      jest.spyOn(collateralTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collateralType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collateralTypeService.update).toHaveBeenCalledWith(collateralType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
