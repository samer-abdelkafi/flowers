import { Component } from '@angular/core';
import {MATERIAL_IMPORTS} from './shared/material/material.imports';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';



@Component({
  selector: 'app-root',
  imports: [MATERIAL_IMPORTS, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'flowers';
}
