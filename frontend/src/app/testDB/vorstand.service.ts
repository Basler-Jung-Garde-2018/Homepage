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
export class VorstandService {
  private vorstandData: TreeNode[] = [
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Rafael Clause',
        title: 'Obmaa / Major'
      },
      children: [
        {
          expanded: true,
          type: 'person',
          data: {
            name: 'Pascal Abt',
            title: 'Vize'
          },
          children: [
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Mirijam Hagmann',
                title: 'Kassier'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Mirijam Hagmann',
                title: 'Kassier'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Mirijam Hagmann',
                title: 'Kassier'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Mirijam Hagmann',
                title: 'Kassier'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Mirijam Hagmann',
                title: 'Kassier'
              }
            }
          ]
        }
      ]
    }
  ];

  getVorstand(): TreeNode[] {
    return this.vorstandData;
  }

}
