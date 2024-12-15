import {Component, inject, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {ClientService} from "../../../service/client.service";
import {MetaData} from "../../../model/MetaData";

@Component({
  selector: 'app-show-image-modal',
  standalone: true,
  imports: [
    MatButton,
    MatIconButton,
    MatIcon
  ],
  templateUrl: './show-image-modal.component.html',
  styleUrl: './show-image-modal.component.scss'
})
export class ShowImageModalComponent {
  private readonly clientService = inject(ClientService);

  constructor(
    public dialogRef: MatDialogRef<ShowImageModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { url: string, metaData: MetaData }
  ) {
  }

  previous() {
    this.dialogRef.close("previous")
  }

  next() {
    this.dialogRef.close("next")
  }

  download() {
    this.clientService.getMedia(this.data.metaData.id, "PUBLIC").subscribe(blob => {
      const link = document.createElement('a');
      const url = window.URL.createObjectURL(blob);
      link.href = url;
      link.download = this.data.metaData.name;
      link.click();
      window.URL.revokeObjectURL(url);
    })
  }
}
