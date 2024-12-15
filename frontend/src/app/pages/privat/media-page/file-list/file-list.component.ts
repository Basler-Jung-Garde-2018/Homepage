import {Component, inject, Input} from '@angular/core';
import {MetaData} from "../../../../model/MetaData";
import {ClientService} from "../../../../service/client.service";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {SizePipe} from "../../../../core/size.pipe";
import {ToastService} from "../../../../core/toast.service";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-file-list',
  standalone: true,
  imports: [
    MatTable,
    MatHeaderCell,
    MatCell,
    MatColumnDef,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef,
    MatCellDef,
    MatHeaderCellDef,
    SizePipe,
    MatButton,
    MatIcon
  ],
  templateUrl: './file-list.component.html',
  styleUrl: './file-list.component.scss'
})
export class FileListComponent {
  private readonly clientService = inject(ClientService);
  private readonly toastService = inject(ToastService);

  metaData = new MatTableDataSource<MetaData>;
  displayedColumns: string[] = ['name', 'size', "download"];

  type: string = "";

  @Input()
  set folder(folder: string) {
    this.type = this.mapToFileType(folder);
    this.clientService.getMetaDataOfMedia(this.type).subscribe({
      next: metaData => {
        this.metaData.data = metaData;
      },
      error: err => {
        console.error("Error occurred: ", err);
        this.toastService.openErrorToast("Es gab ein Fehler beim Laden der Dateien. Bitte versuchen Sie es später erneut.")
      }
    });
  }

  private mapToFileType(type: string) {
    switch (type) {
      case "Audio":
        return type.toUpperCase()
      case "Noten":
        return "NOTES"
      case "Protokolle":
        return "PROTOCOL"
      case "Formulare":
        return "FORM"
      case "Regelungen":
        return "REGULATIONS"
      default:
        return ""
    }
  }

  downloadFile(element: MetaData) {
    this.clientService.getMedia(element.id, this.type).subscribe(blob => {
      const link = document.createElement('a');
      const url = window.URL.createObjectURL(blob);
      link.href = url;
      link.download = element.name;
      link.click();
      window.URL.revokeObjectURL(url);
    })
  }
}
