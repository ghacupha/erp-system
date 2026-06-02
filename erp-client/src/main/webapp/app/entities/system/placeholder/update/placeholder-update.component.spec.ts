jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlaceholderService } from '../service/placeholder.service';
import { IPlaceholder, Placeholder } from '../placeholder.model';

import { PlaceholderUpdateComponent } from './placeholder-update.component';

describe('Placeholder Management Update Component', () => {
  let comp: PlaceholderUpdateComponent;
  let fixture: ComponentFixture<PlaceholderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PlaceholderUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PlaceholderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaceholderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const placeholder: IPlaceholder = { id: 456 };
      const containingPlaceholder: IPlaceholder = { id: 63180 };
      placeholder.containingPlaceholder = containingPlaceholder;

      const placeholderCollection: IPlaceholder[] = [{ id: 24098 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [containingPlaceholder];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const placeholder: IPlaceholder = { id: 456 };
      const containingPlaceholder: IPlaceholder = { id: 2871 };
      placeholder.containingPlaceholder = containingPlaceholder;

      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(placeholder));
      expect(comp.placeholdersSharedCollection).toContain(containingPlaceholder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Placeholder>>();
      const placeholder = { id: 123 };
      jest.spyOn(placeholderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: placeholder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(placeholderService.update).toHaveBeenCalledWith(placeholder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Placeholder>>();
      const placeholder = new Placeholder();
      jest.spyOn(placeholderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: placeholder }));
      saveSubject.complete();

      // THEN
      expect(placeholderService.create).toHaveBeenCalledWith(placeholder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Placeholder>>();
      const placeholder = { id: 123 };
      jest.spyOn(placeholderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ placeholder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(placeholderService.update).toHaveBeenCalledWith(placeholder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
