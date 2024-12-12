import {Component} from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardImage,
  MatCardSubtitle,
  MatCardTitle
} from "@angular/material/card";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {ClientService} from "../../service/client.service";
import {DatePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {CalendarComponent} from "../../core/calendar/calendar.component";

@Component({
  selector: 'app-public-calendar',
  standalone: true,
  imports: [
    MatGridList,
    MatGridTile,
    MatCardHeader,
    MatCard,
    MatCardContent,
    MatCardActions,
    MatCardImage,
    MatButton,
    MatCardTitle,
    MatCardSubtitle,
    MatIcon,
    NgForOf,
    DatePipe,
    NgClass,
    NgIf,
    CalendarComponent
  ],
  templateUrl: './public-calender.component.html',
  styleUrl: './public-calender.component.scss'
})
export class PublicCalenderComponent {
  appointments = this.clientService.getPublicAppointments();

  constructor(private clientService: ClientService) {
  }

  /*
  scrollToNextAppointment(): void {
    if (this.appointments?.length) {
      const nextAppointmentIndex = this.getNextAppointmentIndex();
      if (nextAppointmentIndex !== -1) {
        const nextAppointmentElement = document.querySelectorAll('.timeline-item')[nextAppointmentIndex];
        if (nextAppointmentElement) {
          nextAppointmentElement.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
      }
    }
  }

  getNextAppointmentIndex(): number {
    if (!this.appointments) return -1;
    const currentDate = new Date();
    for (let i = 0; i < this.appointments.length; i++) {
      if (new Date(this.appointments[i].date) > currentDate) {
        return i;
      }
    }
    return -1;
  }

  isNextAppointment(index: number): boolean {
    const nextIndex = this.getNextAppointmentIndex();
    return nextIndex === index;
  }
*/
  openLink(url: string): void {
    window.open(url, "_blank");
  }

  /*
    downloadICS(appointment: Appointment): void {
      const startDate = new Date(appointment.date);
      const endDate = new Date(startDate);
      endDate.setHours(endDate.getHours() + 1);

      const formatDate = (date: Date): string => date.toISOString().replace(/-|:|\.\d{3}/g, '');

      const icsContent = [
        'BEGIN:VCALENDAR',
        'VERSION:2.0',
        'BEGIN:VEVENT',
        `UID:${appointment.id}`,
        `DTSTAMP:${formatDate(new Date())}`,
        `DTSTART:${formatDate(startDate)}`,
        `DTEND:${formatDate(endDate)}`,
        `SUMMARY:${appointment.name}`,
        `LOCATION:${appointment.location}`,
        `DESCRIPTION:Type - ${appointment.type}`,
        'END:VEVENT',
        'END:VCALENDAR'
      ].join('\r\n');

      const blob = new Blob([icsContent], {type: 'text/calendar'});
      const url = window.URL.createObjectURL(blob);
      const downloadAnchor = document.createElement('a');
      downloadAnchor.href = url;
      downloadAnchor.download = `appointment_${appointment.id}.ics`;
      document.body.appendChild(downloadAnchor);
      downloadAnchor.click();
      document.body.removeChild(downloadAnchor);
      window.URL.revokeObjectURL(url);
    }*/
}
