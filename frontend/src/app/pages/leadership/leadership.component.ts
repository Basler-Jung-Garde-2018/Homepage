import { Component } from '@angular/core';
import {OrganizationChartModule} from "primeng/organizationchart";
import {TreeNode} from "primeng/api";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {VorstandService} from "../../testDB/vorstand.service";
import {MukoService} from "../../testDB/muko.service";
import {SujetkommisionService} from "../../testDB/sujetkommision.service";

@Component({
  selector: 'app-leadership',
  standalone: true,
  imports: [
    OrganizationChartModule,
    MatGridList,
    MatGridTile
  ],
  templateUrl: './leadership.component.html',
  styleUrl: './leadership.component.scss'
})
export class LeadershipComponent {
  selectedNodes!: TreeNode[];
  vorstand: TreeNode[] = [];
  muko: TreeNode[] = [];
  sujet: TreeNode[] = [];

  constructor(private vorstandService: VorstandService, private mukoService: MukoService, private sujetService: SujetkommisionService) {
    this.vorstand = this.vorstandService.getVorstand()
    this.muko = this.mukoService.getMuko()
    this.sujet = this.sujetService.getSujet()
  }
}
