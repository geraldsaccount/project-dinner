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

export type StorySummary = {
  id: string;
  storyId: string;
  title: string;
  thumbnailDescription: string;
  minPlayers: number;
  maxPlayers: number;
  configs: StoryConfigSummary[];
  characters: CharacterSummary[];
};

export type StoryConfigSummary = {
  id: string;
  playerCount: number;
  genderCounts: Record<string, number>;
  characterIds: string[];
};

export type CharacterSummary = {
  id: string;
  name: string;
  characterDescription: string;
  gender: "MALE" | "FEMALE" | "OTHER";
};
