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
export class MukoService {

  private mukodata: TreeNode[] = [
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Dominik Guthauser',
        title: 'Musikalischer Leiter'
      },
      children: [
        {
          expanded: true,
          type: 'person',
          data: {
            name: 'Rafael Claude',
            title: 'Obmaa / Major'
          },
          children: [
            {
              expanded: false,
              type: 'person',
              data: {
                name: 'Zoi Messmer',
                title: 'Ansprechpartnerin / Übersicht'
              },
              children: [
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Emily Kapp',
                    title: 'Registerleitung Trompeten'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Enea Knupp',
                    title: 'STV Trompeten'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Dominic Guthauser',
                    title: 'Erwachsener'
                  }
                }
              ]
            },
            {
              expanded: false,
              type: 'person',
              data: {
                name: 'Björn Breitenstein',
                title: 'Registerleitung Posaunen'
              },
              children: [
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Leonie Liers',
                    title: 'STV Posaunen'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Vakant',
                    title: 'Erwachsener'
                  }
                }
              ]
            },
            {
              expanded: false,
              type: 'person',
              data: {
                name: 'Nevio Knupp',
                title: 'Registerleitung Bass'
              },
              children: [
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Max Häfelfinger',
                    title: 'STV Bass'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener'
                  }
                }
              ]
            },
            {
              expanded: false,
              type: 'person',
              data: {
                name: 'Bryan Büffing',
                title: 'Registerleitung Schlag'
              },
              children: [
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Enea Stöckli',
                    title: 'STV Schlag'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Rafael Claude',
                    title: 'Erwachsener'
                  }
                }
              ]
            },
            {
              expanded: false,
              type: 'person',
              data: {
                name: 'Muko Helfer',
                title: 'Helfer'
              },
              children: [
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Tobi Pfluger',
                    title: 'Schlaginstruktor'
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  ];

  getMuko(): TreeNode[] {
    return this.mukodata;
  }
}
