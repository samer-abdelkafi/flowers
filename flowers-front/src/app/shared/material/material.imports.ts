import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {
  MatTable,
  MatCell,
  MatColumnDef,
  MatHeaderCell,
  MatCellDef,
  MatHeaderCellDef,
  MatHeaderRowDef,
  MatRowDef,
  MatHeaderRow,
  MatRow
} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import {MatOption} from '@angular/material/core';
import {MatCheckbox} from '@angular/material/checkbox';
import { MatSelect } from '@angular/material/select';
import {MatInput} from '@angular/material/input';



export const MATERIAL_IMPORTS = [
  MatButtonModule,
  MatCardModule,
  MatIcon,
  MatToolbar,
  MatTable,
  MatCell,
  MatColumnDef,
  MatHeaderCell,
  MatCellDef,
  MatHeaderCellDef,
  MatHeaderRowDef,
  MatRowDef,
  MatHeaderRow,
  MatRow,
  MatPaginator,
  MatLabel,
  MatOption,
  MatFormField,
  MatSelect,
  MatCheckbox,
  MatInput,
  MatError
];
