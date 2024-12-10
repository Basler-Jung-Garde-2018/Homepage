import { Component } from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {MatListItem, MatNavList} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";
import {MatLine} from "@angular/material/core";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-media-page',
  standalone: true,
  imports: [
    MatSidenavContainer,
    MatSidenavContent,
    MatNavList,
    MatListItem,
    MatIcon,
    MatSidenav,
    MatLine,
    NgForOf
  ],
  templateUrl: './media-page.component.html',
  styleUrl: './media-page.component.scss'
})
export class MediaPageComponent {
  folders = [
    { name: 'Audio', icon: 'audiotrack' },
    { name: 'Noten', icon: 'note' },
    { name: 'Protokoll', icon: 'description' },
    { name: 'Form', icon: 'assignment' },
    { name: 'Regulations', icon: 'gavel' }
  ];

  selectedFolder = 'Bitte wählen Sie einen Ordner';

  onFolderClick(folder: { name: string; icon: string }): void {
    this.selectedFolder = folder.name;
    console.log(`Folder selected: ${folder.name}`);
    // Hier könnte weitere Logik folgen, z. B. Laden von Dateien
  }
}
