import { Route } from '@angular/router';

import {ERPNavbarComponent} from "./erp-navbar.component";

export const ErpNavbarRoute: Route = {
  path: '',
  component: ERPNavbarComponent,
  outlet: 'erp-navbar',
};
