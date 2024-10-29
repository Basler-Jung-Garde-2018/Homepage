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
export class VortrabService {
  private vortrabData: TreeNode[] = [
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Kei ahnig wär',
        title: 'Zugchef'
      },
    },
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Tanja Mischler',
        title: 'Fix Begleiter'
      },
    },
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Fabio Claude',
        title: 'Fix Begleiter'
      },
    },
    {
      expanded: true,
      type: 'person',
      data: {
        name: 'Jasmin Malsbender',
        title: 'Fix Begleiter'
      },
    }
    
  ];

  getVorstand(): TreeNode[] {
    return this.vortrabData;
  }

}
