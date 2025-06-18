import { Story, Stage, Character, PlayerConfig, Crime } from "@/types/creation";
import { useEffect, useState } from "react";
import { EditorContext } from "./editor-context";

const EditorProvider = ({ children }: { children: React.ReactNode }) => {
  const [story, setStory] = useState<Story>({
    id: "",
    title: "",
    shopDescription: "",
    bannerImage: null,
    rules: "",
    setting: "",
    briefing: "",
  });
  const [stages, setStages] = useState<Stage[]>([]);
  const [characters, setCharacters] = useState<Character[]>([]);
  const [activeCharacterId, setActiveCharacterId] = useState<string | null>(
    null
  );
  const [playerConfigs, setPlayerConfigs] = useState<PlayerConfig[]>([]);
  const [crime, setCrime] = useState<Crime>({
    criminalIds: [],
    description: "",
  });

  // Sync characters' stageInfo with global stages
  useEffect(() => {
    setCharacters((prevChars) =>
      prevChars.map((char) => {
        const newStageInfo = stages.map((gStage) => {
          const existingInfo = char.stageInfo.find(
            (si) => si.stageId === gStage.id
          );
          return (
            existingInfo || {
              stageId: gStage.id,
              order: gStage.order,
              objectivePrompt: "",
              events: [],
            }
          );
        });
        // Filter out info for deleted stages
        const filteredStageInfo = newStageInfo.filter((si) =>
          stages.some((gs) => gs.id === si.stageId)
        );
        return { ...char, stageInfo: filteredStageInfo };
      })
    );
  }, [stages, setCharacters]);

  const updateCharacter = (character: Character) => {
    setCharacters((prev) =>
      prev.map((c) => (c.id === character.id ? character : c))
    );
  };

  const deleteCharacter = (characterId: string) => {
    setCharacters((prev) => prev.filter((c) => c.id !== characterId));
  };

  return (
    <EditorContext.Provider
      value={{
        story,
        setStory,
        stages,
        setStages,
        characters,
        setCharacters,
        activeCharacterId,
        setActiveCharacterId,
        playerConfigs,
        setPlayerConfigs,
        crime,
        setCrime,
        updateCharacter,
        deleteCharacter,
      }}
    >
      {children}
    </EditorContext.Provider>
  );
};

export default EditorProvider;
