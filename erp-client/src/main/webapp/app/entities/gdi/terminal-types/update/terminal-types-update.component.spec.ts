jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TerminalTypesService } from '../service/terminal-types.service';
import { ITerminalTypes, TerminalTypes } from '../terminal-types.model';

import { TerminalTypesUpdateComponent } from './terminal-types-update.component';

describe('TerminalTypes Management Update Component', () => {
  let comp: TerminalTypesUpdateComponent;
  let fixture: ComponentFixture<TerminalTypesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let terminalTypesService: TerminalTypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TerminalTypesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TerminalTypesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TerminalTypesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    terminalTypesService = TestBed.inject(TerminalTypesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const terminalTypes: ITerminalTypes = { id: 456 };

      activatedRoute.data = of({ terminalTypes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(terminalTypes));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalTypes>>();
      const terminalTypes = { id: 123 };
      jest.spyOn(terminalTypesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminalTypes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(terminalTypesService.update).toHaveBeenCalledWith(terminalTypes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalTypes>>();
      const terminalTypes = new TerminalTypes();
      jest.spyOn(terminalTypesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminalTypes }));
      saveSubject.complete();

      // THEN
      expect(terminalTypesService.create).toHaveBeenCalledWith(terminalTypes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalTypes>>();
      const terminalTypes = { id: 123 };
      jest.spyOn(terminalTypesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalTypes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(terminalTypesService.update).toHaveBeenCalledWith(terminalTypes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
