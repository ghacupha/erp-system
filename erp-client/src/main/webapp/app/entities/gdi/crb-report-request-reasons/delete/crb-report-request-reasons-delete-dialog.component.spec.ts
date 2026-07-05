jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CrbReportRequestReasonsService } from '../service/crb-report-request-reasons.service';

import { CrbReportRequestReasonsDeleteDialogComponent } from './crb-report-request-reasons-delete-dialog.component';

describe('CrbReportRequestReasons Management Delete Component', () => {
  let comp: CrbReportRequestReasonsDeleteDialogComponent;
  let fixture: ComponentFixture<CrbReportRequestReasonsDeleteDialogComponent>;
  let service: CrbReportRequestReasonsService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbReportRequestReasonsDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CrbReportRequestReasonsDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbReportRequestReasonsDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CrbReportRequestReasonsService);
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
