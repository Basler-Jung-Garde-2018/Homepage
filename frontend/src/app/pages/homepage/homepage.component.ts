import { Component } from '@angular/core';
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';

import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [
    MatCard,
    MatCardHeader,
    MatIcon,
    MatCardContent,
    MatCardActions,
    MatCardTitle,
    MatButton,
    RouterLink,
    MatGridList,
    MatGridTile,
    AsyncPipe,
  ],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

  onVolumeChange(event: Event, audioPlayer: HTMLAudioElement): void {
    const target = event.target as HTMLInputElement;
    audioPlayer.volume = +target.value;
  }
}
