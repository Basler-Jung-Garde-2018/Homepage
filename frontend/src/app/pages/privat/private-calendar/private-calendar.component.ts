import {Component, inject} from '@angular/core';
import {CalendarComponent} from "../../../core/calendar/calendar.component";
import {ClientService} from "../../../service/client.service";
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {AddEventModalComponent} from "./add-event-modal/add-event-modal.component";
import {filter, map, of, switchMap, take} from "rxjs";
import {Appointment} from "../../../model/appointments";

@Component({
  selector: 'app-private-calendar',
  standalone: true,
  imports: [
    CalendarComponent,
    MatButton
  ],
  templateUrl: './private-calendar.component.html',
  styleUrl: './private-calendar.component.scss'
})
export class PrivateCalendarComponent {
  private readonly clientService = inject(ClientService);

  constructor(private dialog: MatDialog) {
  }

  appointments = this.clientService.getPrivateAppointments();

  addEvent(): void {
    const dialogRef = this.dialog.open(AddEventModalComponent, {
      width: '300px',
    });

    dialogRef.afterClosed().pipe(
      filter(result => result && result !== "cancel"),
      switchMap((appointment: Appointment) => this.clientService.addAppointment(appointment)),
      switchMap((appointment) => this.appointments.pipe(
        take(1),
        map(appointments => [...appointments, appointment])
      ))
    ).subscribe(updatedAppointments => {
      this.appointments = of(updatedAppointments);
    });
  }
}
