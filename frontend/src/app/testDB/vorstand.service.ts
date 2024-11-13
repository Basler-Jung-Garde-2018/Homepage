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
        name: 'Rafael Claude',
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
                name: 'Mirjam Hagmann',
                title: 'Kassier'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Vakant',
                title: 'Aktuar'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Stephanie Knupp',
                title: 'Sujet Obfrau'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Tanja Mäder',
                title: 'Materialverwaltung'
              }
            },
            {
              expanded: true,
              type: 'person',
              data: {
                name: 'Salima Robles',
                title: 'Fasnachts OK'
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
