import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatCheckbox} from "@angular/material/checkbox";
import {MatButton} from "@angular/material/button";
import {MatOption, MatSelect} from "@angular/material/select";
import {AppointmentType} from "../../../model/AppointmentType";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-event-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatCheckbox,
    MatButton,
    MatLabel,
    MatSelect,
    MatOption,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepicker,
    MatHint
  ],
  templateUrl: './add-event-modal.component.html',
  styleUrl: './add-event-modal.component.scss'
})
export class AddEventModalComponent {
  appointment: FormGroup;

  constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<AddEventModalComponent>) {
    this.appointment = this.fb.group({
      start: [],
      end: [],
      location: [],
      name: [],
      type: [AppointmentType.FASNACHT],
      published: [false]
    });
  }

  onSubmit(): void {
    this.dialogRef.close(this.appointment.value)
  }

  protected readonly AppointmentType = AppointmentType;
}
