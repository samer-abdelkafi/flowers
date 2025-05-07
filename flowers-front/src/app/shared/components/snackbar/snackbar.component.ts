import {Component, inject} from '@angular/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBarLabel,
} from '@angular/material/snack-bar';

@Component({
  selector: 'app-snackbar',
  imports: [MatFormFieldModule, FormsModule, MatInputModule, MatButtonModule, MatSnackBarLabel],
  templateUrl: './snackbar.component.html',
  styleUrl: './snackbar.component.scss'
})
export class SnackbarComponent {

  data = inject(MAT_SNACK_BAR_DATA);

}
