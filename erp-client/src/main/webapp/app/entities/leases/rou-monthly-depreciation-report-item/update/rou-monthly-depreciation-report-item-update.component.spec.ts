jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';
import { IRouMonthlyDepreciationReportItem, RouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';

import { RouMonthlyDepreciationReportItemUpdateComponent } from './rou-monthly-depreciation-report-item-update.component';

describe('RouMonthlyDepreciationReportItem Management Update Component', () => {
  let comp: RouMonthlyDepreciationReportItemUpdateComponent;
  let fixture: ComponentFixture<RouMonthlyDepreciationReportItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouMonthlyDepreciationReportItemService: RouMonthlyDepreciationReportItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouMonthlyDepreciationReportItemUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouMonthlyDepreciationReportItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouMonthlyDepreciationReportItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouMonthlyDepreciationReportItemService = TestBed.inject(RouMonthlyDepreciationReportItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem = { id: 456 };

      activatedRoute.data = of({ rouMonthlyDepreciationReportItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouMonthlyDepreciationReportItem));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouMonthlyDepreciationReportItem>>();
      const rouMonthlyDepreciationReportItem = { id: 123 };
      jest.spyOn(rouMonthlyDepreciationReportItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouMonthlyDepreciationReportItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouMonthlyDepreciationReportItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouMonthlyDepreciationReportItemService.update).toHaveBeenCalledWith(rouMonthlyDepreciationReportItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouMonthlyDepreciationReportItem>>();
      const rouMonthlyDepreciationReportItem = new RouMonthlyDepreciationReportItem();
      jest.spyOn(rouMonthlyDepreciationReportItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouMonthlyDepreciationReportItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouMonthlyDepreciationReportItem }));
      saveSubject.complete();

      // THEN
      expect(rouMonthlyDepreciationReportItemService.create).toHaveBeenCalledWith(rouMonthlyDepreciationReportItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouMonthlyDepreciationReportItem>>();
      const rouMonthlyDepreciationReportItem = { id: 123 };
      jest.spyOn(rouMonthlyDepreciationReportItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouMonthlyDepreciationReportItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouMonthlyDepreciationReportItemService.update).toHaveBeenCalledWith(rouMonthlyDepreciationReportItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
