import { Component, OnInit } from '@angular/core';
import { ClientService } from "../../../service/client.service";
import { FunctionType, Member } from "../../../model/members";
import {NgForOf, NgIf, TitleCasePipe} from "@angular/common";

@Component({
  selector: 'app-members',
  standalone: true,
  imports: [NgForOf, TitleCasePipe, NgIf],
  templateUrl: './members.component.html',
  styleUrl: './members.component.scss'
})
export class MembersComponent implements OnInit {
  members: Member[] = [];

  functionOrder: FunctionType[] = [
    FunctionType.VORTRAB,
    FunctionType.MAJOR,
    FunctionType.SNARE,
    FunctionType.SCHLAGZEUG,
    FunctionType.PAUKE,
    FunctionType.TROMPETE,
    FunctionType.EUPHONIUM,
    FunctionType.POSAUNE,
    FunctionType.SOUSAPHONE,
  ];

  constructor(private clientService: ClientService) { }

  ngOnInit(): void {
    this.reloadAppointments();
  }

  private loadAppointments(): void {
    this.clientService.getMembers().subscribe({
      next: (data) => {
        this.members = data;
      },
      error: (error) => {
        console.error('Error fetching appointments:', error);
      }
    });
  }

  public reloadAppointments(): void {
    this.loadAppointments();
  }

  getMembersByFunction(functionType: FunctionType): Member[] {
    return this.members.filter(person => person.function === functionType);
  }

  getImageForFunction(functionType: FunctionType): string {
    switch (functionType) {
      case FunctionType.SNARE: return 'assets/instrumente/Snare.png';
      case FunctionType.SCHLAGZEUG: return 'assets/instrumente/Schlagzyg.png';
      case FunctionType.PAUKE: return 'assets/instrumente/Pauke.png';
      case FunctionType.TROMPETE: return 'assets/instrumente/Drumpete.png';
      case FunctionType.EUPHONIUM: return 'assets/instrumente/Euphonium.png';
      case FunctionType.POSAUNE: return 'assets/instrumente/Phosuune.png';
      case FunctionType.SOUSAPHONE: return 'assets/instrumente/Sousaphon.png';
      case FunctionType.VORTRAB:
      case FunctionType.MAJOR:
        return '';
      default: return '';
    }
  }
}
