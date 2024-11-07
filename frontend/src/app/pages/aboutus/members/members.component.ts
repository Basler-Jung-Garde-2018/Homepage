import { Component } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { VortrabService } from '../../../testDB/mitglieder_vortrab.service';
import { MatGridList, MatGridTile } from '@angular/material/grid-list';
import { OrganizationChartModule } from 'primeng/organizationchart';

@Component({
  selector: 'app-members',
  standalone: true,
  imports: [
    OrganizationChartModule,
    MatGridList,
    MatGridTile],
  templateUrl: './members.component.html',
  styleUrl: './members.component.scss'
})
export class MembersComponent {
  selectedNodes!: TreeNode[];
  vortrab: TreeNode[] = [];


  constructor(private vortrabService: VortrabService) {
    this.vortrab = this.vortrabService.getVorstand()
  }
}

