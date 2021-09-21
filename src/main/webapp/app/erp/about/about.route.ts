import { Route } from '@angular/router';
import {AboutComponent} from "./about.component";

export const ABOUT_ROUTE: Route = {
  path: 'about/erp',
  component: AboutComponent,
  data: {
    pageTitle: 'ERP | About',
  },
};
