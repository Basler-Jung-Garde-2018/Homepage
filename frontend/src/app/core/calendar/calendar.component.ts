import {Component, Inject, Input, PLATFORM_ID, ViewChild} from '@angular/core';
import {CalendarOptions} from "@fullcalendar/core";
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import {FullCalendarComponent, FullCalendarModule} from "@fullcalendar/angular";
import {isPlatformBrowser, NgIf} from "@angular/common";
import {Appointment} from "../../model/appointments";
import {Observable} from "rxjs";
import {AppointmentType} from "../../model/AppointmentType";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [
    FullCalendarModule,
    NgIf,
    MatButton
  ],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.scss'
})
export class CalendarComponent {
  @Input() set appointments(appointments$: Observable<Appointment[]>) {
    appointments$.subscribe(appointments => {
      this.calendarOptions.events = appointments.map(appointment => {
        return {
          start: appointment.start,
          end: appointment.end,
          title: appointment.name + "\n" + appointment.location,
          color: this.getColorByType(appointment.type),
        }
      })
    })
  }

  @ViewChild('calendar')
  calendarComponent: FullCalendarComponent | undefined;

  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, timeGridPlugin],
    initialView: 'dayGridMonth',
    firstDay: 1,
    weekends: true,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek'
    },
    eventTimeFormat: {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    }
  };

  getColorByType(type: AppointmentType): string {
    switch (type) {
      case AppointmentType.FASNACHT: {
        return "turquoise"
      }
      default:
        return "blue"
    }
  }

  download() {
    if (!this.calendarComponent) return;
    const calendarApi = this.calendarComponent?.getApi();
    const visibleEvents = calendarApi?.getEvents().filter(event => {
      const s = calendarApi?.view.activeStart, e = calendarApi?.view.activeEnd;
      if (event.start! > e || event.end! < s) return false;
      return true
    });
    const appointmentsToDownload: Partial<Appointment>[] = visibleEvents?.map((event): Partial<Appointment> => {
      const split = event.title.split("\n");
      const name = split[0]
      const location = split[1]
      return {
        start: event.start!,
        end: event.end!,
        name,
        location: location
      }
    })
    this.downloadICS(appointmentsToDownload);
  }

  // todo: add download
  downloadICS(appointments: Partial<Appointment>[]): void {
    const formatDate = (date: Date): string => date.toISOString().replace(/-|:|\.\d{3}/g, '');

    const icsContent = [
      'BEGIN:VCALENDAR',
      'VERSION:2.0'
    ];

    appointments.forEach(appointment => {
      const startDate = appointment.start!;
      const endDate = appointment.end!;
      icsContent.push(
        'BEGIN:VEVENT',
        `UID:${appointment.id}`,
        `DTSTAMP:${formatDate(new Date())}`,
        `DTSTART:${formatDate(startDate)}`,
        `DTEND:${formatDate(endDate)}`,
        `SUMMARY:${appointment.name}`,
        `LOCATION:${appointment.location}`,
        'END:VEVENT'
      );
    });

    icsContent.push('END:VCALENDAR');

    const blob = new Blob([icsContent.join('\r\n')], {type: 'text/calendar'});
    const url = window.URL.createObjectURL(blob);
    const downloadAnchor = document.createElement('a');
    downloadAnchor.href = url;
    downloadAnchor.download = `appointments.ics`;
    document.body.appendChild(downloadAnchor);
    downloadAnchor.click();
    document.body.removeChild(downloadAnchor);
    window.URL.revokeObjectURL(url);
  }

  // check if browser to remove error out of dev console.
  isBrowser: boolean

  constructor(@Inject(PLATFORM_ID)
              private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  private getTextColorByType(type: AppointmentType): string {
    switch (type) {
      case AppointmentType.FASNACHT: {
        return "turquoise"
      }
      default:
        return "white"
    }
  }
}
