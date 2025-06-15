export interface Mystery {
  story: Story;
  characters: Character[];
  stages: Stage[];
  setups: PlayerConfig[];
  crime: Crime;
}

export interface Story {
  id: string;
  title: string;
  shopDescription: string;
  bannerImage: string | null;
  rules: string;
  setting: string;
  briefing: string;
}

export interface Character {
  id: string;
  name: string;
  role: string;
  age: number;
  isPrimary: boolean;
  gender: "Female" | "Male" | "Other";
  shopDescription: string;
  privateDescription: string;
  avatarUrl: string | null;
  relationships: Record<string, string>;
  stageInfo: CharacterStageInfo[];
}

export interface CharacterStageInfo {
  stageId: string;
  objectivePrompt: string;
  events: StageEvent[];
}

export interface StageEvent {
  id: string;
  time: string;
  title: string;
  description: string;
}

export interface Stage {
  id: string;
  title: string;
  hostPrompt: string;
}

export interface PlayerConfig {
  id: string;
  playerCount: number;
  characterIds: string[];
}

export interface Crime {
  criminalIds: string[];
  description: string;
}
