jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductTypeService } from '../service/product-type.service';
import { IProductType, ProductType } from '../product-type.model';

import { ProductTypeUpdateComponent } from './product-type-update.component';

describe('ProductType Management Update Component', () => {
  let comp: ProductTypeUpdateComponent;
  let fixture: ComponentFixture<ProductTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productTypeService: ProductTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ProductTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productTypeService = TestBed.inject(ProductTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productType: IProductType = { id: 456 };

      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(productType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductType>>();
      const productType = { id: 123 };
      jest.spyOn(productTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productTypeService.update).toHaveBeenCalledWith(productType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductType>>();
      const productType = new ProductType();
      jest.spyOn(productTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productType }));
      saveSubject.complete();

      // THEN
      expect(productTypeService.create).toHaveBeenCalledWith(productType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProductType>>();
      const productType = { id: 123 };
      jest.spyOn(productTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productTypeService.update).toHaveBeenCalledWith(productType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
