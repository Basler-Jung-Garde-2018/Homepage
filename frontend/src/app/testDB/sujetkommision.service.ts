import { Injectable } from '@angular/core';


interface TreeNode {
  expanded: boolean;
  type: 'person' | 'group';
  data: {
    name: string;
    title: string;
  };
  children?: TreeNode[];
}

@Injectable({
  providedIn: 'root'
})
export class SujetkommisionService {

  private sujetData: TreeNode[] = [
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Stephanie Knupp',
        title: 'Sujet Kommision'
      },
      children: [
        {
          expanded: true,
          type: 'person',
          data: {
            name: '1-2 Aktive',
            title: ''
          }
        }
      ]
    }
  ];

  getSujet(): TreeNode[] {
    return this.sujetData;
  }
}
