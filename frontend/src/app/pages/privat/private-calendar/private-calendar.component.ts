import {Component, inject} from '@angular/core';
import {PublicCalenderComponent} from "../../public-calendar/public-calender.component";
import {CalendarComponent} from "../../../core/calendar/calendar.component";
import {ClientService} from "../../../service/client.service";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-private-calendar',
  standalone: true,
  imports: [
    PublicCalenderComponent,
    CalendarComponent,
    MatButton
  ],
  templateUrl: './private-calendar.component.html',
  styleUrl: './private-calendar.component.scss'
})
export class PrivateCalendarComponent {
  private readonly clientService = inject(ClientService);
  appointments = this.clientService.getPrivateAppointments();

  // todo: add button to add appointments
}
