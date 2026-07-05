jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AnticipatedMaturityPerioodService } from '../service/anticipated-maturity-periood.service';

import { AnticipatedMaturityPerioodDeleteDialogComponent } from './anticipated-maturity-periood-delete-dialog.component';

describe('AnticipatedMaturityPeriood Management Delete Component', () => {
  let comp: AnticipatedMaturityPerioodDeleteDialogComponent;
  let fixture: ComponentFixture<AnticipatedMaturityPerioodDeleteDialogComponent>;
  let service: AnticipatedMaturityPerioodService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AnticipatedMaturityPerioodDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(AnticipatedMaturityPerioodDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AnticipatedMaturityPerioodDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AnticipatedMaturityPerioodService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
