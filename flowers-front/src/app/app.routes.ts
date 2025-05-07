import { Routes } from '@angular/router';
import {FlowListComponent} from './flows/flow-list/flow-list.component';
import {MessageListComponent} from './messages/message-list/message-list.component';
import {FlowEditComponent} from './flows/flow-edit/flow-edit.component';


export const routes: Routes = [
  { path: 'flows', component: FlowListComponent },
  { path: 'flows/edit/:id', component: FlowEditComponent},
  { path: 'messages', component: MessageListComponent },
  { path: '**', redirectTo: 'flows' }
];
