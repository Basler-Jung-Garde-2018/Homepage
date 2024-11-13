import {Component, OnInit} from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardImage, MatCardSubtitle,
  MatCardTitle
} from "@angular/material/card";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {ClientService} from "../../service/client.service";
import {Appointment} from "../../model/appointments";
import {DatePipe, NgForOf} from "@angular/common";

@Component({
  selector: 'app-calendar',
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
    DatePipe
  ],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.scss'
})
export class CalendarComponent implements OnInit{
  appointments: Appointment[] | null = null;

  constructor(private clientService: ClientService) {
  }

  openLink(url: string) {
    window.open(url, "_blank")
  }

  ngOnInit(): void {
    this.reloadAppointments()
  }
  private loadAppointments(): void {
    this.clientService.getAppointments().subscribe({
      next: (data) => {
        this.appointments = data;
      },
      error: (error) => {
        console.error('Error fetching appointments:', error);
      }
    });
  }

  public reloadAppointments(): void {
   this.loadAppointments();
  }

  downloadICS(appointment: Appointment): void {
    const startDate = new Date(appointment.date);
    const endDate = new Date(startDate);
    endDate.setHours(endDate.getHours() + 1); // Termin dauert 1 Stunde

    const formatDate = (date: Date): string => {
      return date.toISOString().replace(/-|:|\.\d{3}/g, '');
    };

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

    const blob = new Blob([icsContent], { type: 'text/calendar' });
    const url = window.URL.createObjectURL(blob);
    const downloadAnchor = document.createElement('a');
    downloadAnchor.href = url;
    downloadAnchor.download = `appointment_${appointment.id}.ics`;
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    document.body.removeChild(downloadAnchor);
    window.URL.revokeObjectURL(url);
  }
}
