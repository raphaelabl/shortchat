import { Routes } from '@angular/router';
import {JoinComponent} from "./components/join/join.component";

export const routes: Routes = [
  { path: '**', component: JoinComponent },
];
