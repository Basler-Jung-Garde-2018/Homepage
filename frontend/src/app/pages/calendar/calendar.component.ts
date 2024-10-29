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
import {NgForOf} from "@angular/common";

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
    NgForOf
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
}
