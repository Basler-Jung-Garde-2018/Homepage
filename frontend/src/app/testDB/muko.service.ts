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
                name: 'Nevio Knupp',
                title: 'Registerleitung Bass'
              },
              children: [
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
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
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
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
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
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
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
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
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
                  }
                },
                {
                  expanded: true,
                  type: 'person',
                  data: {
                    name: 'Andy Kübli',
                    title: 'Erwachsener?'
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
