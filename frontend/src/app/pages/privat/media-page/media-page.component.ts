import {Component, inject} from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from "@angular/material/sidenav";
import {MatListItem, MatNavList} from "@angular/material/list";
import {MatIcon} from "@angular/material/icon";
import {MatLine, MatOption} from "@angular/material/core";
import {NgForOf} from "@angular/common";
import {FileListComponent} from "./file-list/file-list.component";
import {ClientService} from "../../../service/client.service";
import {MatFormField, MatLabel, MatSelect} from "@angular/material/select";
import {MatButton} from "@angular/material/button";
import {ToastService} from "../../../core/toast.service";

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
    NgForOf,
    FileListComponent,
    MatSelect,
    MatOption,
    MatLabel,
    MatFormField,
    MatButton
  ],
  templateUrl: './media-page.component.html',
  styleUrl: './media-page.component.scss'
})
export class MediaPageComponent {
  private readonly clientService = inject(ClientService);
  private readonly toastService = inject(ToastService);

  folders = [
    {name: 'Noten', icon: 'note'},
    {name: 'Audio', icon: 'audiotrack'},
    {name: 'Protokolle', icon: 'description'},
    {name: 'Formulare', icon: 'assignment'},
    {name: 'Regelungen', icon: 'gavel'},
    {name: 'Hochladen', icon: 'upload'}
  ];
  selectedFolder = 'Noten';

  files: File[] = [];
  type = "";
  allowedTypes = [
    "image",
    "application/pdf",
    "application/vnd.ms-excel",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    "audio/mpeg"
  ];

  onFolderClick(folder: string): void {
    this.selectedFolder = folder;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      Array.from(input.files).forEach(file => {
        console.log(file.type)
        if (this.allowedTypes.some(type => file.type.includes(type))) {
          this.files.push(file);
        } else {
          console.log(file.type + " not allowed") // todo add user feedback
        }
      });
    }
  }

  removeFile(name: string) {
    this.files = this.files.filter(file => file.name !== name);
  }

  upload() {
    if (this.type === "" || this.files.length == 0) {
      this.toastService.openWarnToast("Bitte überprüfen Sie Ihre Eingaben.")
      return;
    }
    this.clientService.addMedia(this.files, this.type).subscribe({
      next: () => {
        this.toastService.openSuccessToast("Dateien Erfolgreich hochgeladen.");
        this.files = [];
      }, error: err => {
        console.error("Error occurred: ", err);
        this.toastService.openErrorToast("Es gab ein Fehler beim hochladen der Dateien. Bitte versuchen Sie es später erneut.")
      }
    })
  }
}
