import {Injectable} from "@angular/core";
import {Observable, of, Subject} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class BespokeNavigationService {

  paymentsMenuCollapsed = new Subject<boolean>();

  fixedAssetsMenuCollapsed = new Subject<boolean>();

  constructor() {
    this.fixedAssetsMenuCollapsed.next(true);
    this.paymentsMenuCollapsed.next(true);
  }

  paymentsMenuCollapseState(): Observable<boolean> {
    return this.paymentsMenuCollapsed;
  }

  fixedAssetsMenuCollapseState(): Observable<boolean> {
    return this.fixedAssetsMenuCollapsed;
  }

  showPaymentsMenu(): void {
    this.paymentsMenuCollapsed.next(false);
  }

  showFixedAssetsMenu(): void {
    this.fixedAssetsMenuCollapsed.next(false);
  }

  collapsePaymentsMenu(): void {
    this.paymentsMenuCollapsed.next(true);
  }

  collapseFixedAssetsMenu(): void {
    this.fixedAssetsMenuCollapsed.next(true);
  }
}
