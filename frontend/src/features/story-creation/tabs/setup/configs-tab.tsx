import SectionHeader from "@/components/shared/section-header";
import { useEffect } from "react";
import { useEditorContext } from "../../context/editor-context";
import { AddPlayerConfigForm } from "./add-player-config-form";
import ConfigEditor from "./config-editor";

const ConfigsTab = () => {
  const { characters, setCharacters, playerConfigs, setPlayerConfigs, stages } =
    useEditorContext();

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

  const deletePlayerConfig = (id: string) => {
    if (window.confirm("Are you sure?"))
      setPlayerConfigs((prev) => prev.filter((c) => c.id !== id));
  };

  const toggleCharacterInConfig = (configId: string, charId: string) => {
    console.log(configId);
    setPlayerConfigs((prev) =>
      prev.map((config) => {
        if (config.id === configId) {
          const newIds = config.characterIds.includes(charId)
            ? config.characterIds.filter((id) => id !== charId)
            : [...config.characterIds, charId];
          return { ...config, characterIds: newIds };
        }
        return config;
      })
    );
  };

  const addConfig = (count: number) => {
    const newConfig = {
      id: `config_${Date.now()}`,
      playerCount: count,
      characterIds: characters.filter((c) => c.isPrimary).map((c) => c.id),
    };
    setPlayerConfigs((prev) =>
      [...prev, newConfig].sort((a, b) => a.playerCount - b.playerCount)
    );
  };

  return (
    <div className="space-y-4">
      <SectionHeader title="Player Setup" />
      <AddPlayerConfigForm
        minPlayers={characters.filter((c) => c.isPrimary).length}
        maxPlayers={characters.length}
        existingCounts={playerConfigs.map((c) => c.playerCount)}
        onAdd={addConfig}
      />
      <div className="space-y-6">
        {characters.length === 0 ? (
          <div className="text-center text-slate-500 py-8">
            <p>Create characters before defining player setups.</p>
          </div>
        ) : playerConfigs.length === 0 ? (
          <div className="text-center text-slate-500 py-8">
            <p>No player configurations defined yet.</p>
          </div>
        ) : (
          playerConfigs.map((config) => {
            const selectedCount = config.characterIds.length;
            const countColor =
              selectedCount === config.playerCount
                ? "text-green-600"
                : "text-red-600";
            return (
              <ConfigEditor
                key={config.id}
                config={config}
                characters={characters}
                countColor={countColor}
                selectedCount={selectedCount}
                deletePlayerConfig={deletePlayerConfig}
                toggleCharacterInConfig={toggleCharacterInConfig}
              />
            );
          })
        )}
      </div>
    </div>
  );
};

export default ConfigsTab;
