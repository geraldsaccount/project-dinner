export interface NavItem {
  title: string;
  to: string;
  disabled?: boolean;
}

export interface MobileNavProps {
  items: NavItem[];
  activeItem: string;
  setActiveItem: (to: string) => void;
}
