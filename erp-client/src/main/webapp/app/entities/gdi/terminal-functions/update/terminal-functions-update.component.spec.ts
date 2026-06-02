jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TerminalFunctionsService } from '../service/terminal-functions.service';
import { ITerminalFunctions, TerminalFunctions } from '../terminal-functions.model';

import { TerminalFunctionsUpdateComponent } from './terminal-functions-update.component';

describe('TerminalFunctions Management Update Component', () => {
  let comp: TerminalFunctionsUpdateComponent;
  let fixture: ComponentFixture<TerminalFunctionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let terminalFunctionsService: TerminalFunctionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TerminalFunctionsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TerminalFunctionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TerminalFunctionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    terminalFunctionsService = TestBed.inject(TerminalFunctionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const terminalFunctions: ITerminalFunctions = { id: 456 };

      activatedRoute.data = of({ terminalFunctions });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(terminalFunctions));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalFunctions>>();
      const terminalFunctions = { id: 123 };
      jest.spyOn(terminalFunctionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalFunctions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminalFunctions }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(terminalFunctionsService.update).toHaveBeenCalledWith(terminalFunctions);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalFunctions>>();
      const terminalFunctions = new TerminalFunctions();
      jest.spyOn(terminalFunctionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalFunctions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminalFunctions }));
      saveSubject.complete();

      // THEN
      expect(terminalFunctionsService.create).toHaveBeenCalledWith(terminalFunctions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TerminalFunctions>>();
      const terminalFunctions = { id: 123 };
      jest.spyOn(terminalFunctionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminalFunctions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(terminalFunctionsService.update).toHaveBeenCalledWith(terminalFunctions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
