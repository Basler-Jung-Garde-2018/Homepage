import {Component, inject} from '@angular/core';
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
  templateUrl: './public-calendar.component.html',
  styleUrl: './public-calendar.component.scss'
})
export class PublicCalendarComponent {
  private readonly clientService = inject(ClientService);

  appointments = this.clientService.getPublicAppointments();

  openLink(url: string): void {
    window.open(url, "_blank");
  }
}
