import React, { createContext, useContext } from "react";
import { Story, Stage, Character, PlayerConfig, Crime } from "@/types/creation";

export type EditorContextType = {
  story: Story;
  setStory: React.Dispatch<React.SetStateAction<Story>>;
  stages: Stage[];
  setStages: React.Dispatch<React.SetStateAction<Stage[]>>;
  characters: Character[];
  setCharacters: React.Dispatch<React.SetStateAction<Character[]>>;
  activeCharacterId: string | null;
  setActiveCharacterId: React.Dispatch<React.SetStateAction<string | null>>;
  playerConfigs: PlayerConfig[];
  setPlayerConfigs: React.Dispatch<React.SetStateAction<PlayerConfig[]>>;
  crime: Crime;
  setCrime: React.Dispatch<React.SetStateAction<Crime>>;
  updateCharacter: (character: Character) => void;
  deleteCharacter: (characterId: string) => void;
};

export const EditorContext = createContext<EditorContextType | undefined>(
  undefined
);

export const useEditorContext = () => {
  const ctx = useContext(EditorContext);
  if (!ctx)
    throw new Error("useEditorContext must be used within EditorProvider");
  return ctx;
};
