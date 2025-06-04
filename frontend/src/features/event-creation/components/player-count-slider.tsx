import { Slider } from "@/components/ui/slider";
import type { StoryConfigSummary } from "@/types";
import { useEffect, useState } from "react";

type Props = {
  minCount: number;
  maxCount: number;
  configurations: StoryConfigSummary[];
  selectedChanged: (selected: StoryConfigSummary) => void;
  selectedConfigId?: string;
};

const PlayerCountSlider = ({
  minCount,
  maxCount,
  configurations,
  selectedChanged,
  selectedConfigId: controlledId,
}: Props) => {
  const sortedConfigs = configurations
    .slice()
    .sort((a, b) => a.playerCount - b.playerCount);

  // Find the index of the selected config by id
  const controlledIdx = controlledId
    ? sortedConfigs.findIndex((cfg) => cfg.id === controlledId)
    : undefined;
  const [uncontrolledIdx, setUncontrolledIdx] = useState(0);
  const selectedConfigIdx =
    controlledIdx !== undefined && controlledIdx !== -1
      ? controlledIdx
      : uncontrolledIdx;
  const selectedConfig = sortedConfigs[selectedConfigIdx];

  useEffect(() => {
    if (selectedConfig) {
      selectedChanged(selectedConfig);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedConfig]);

  return (
    <div className="flex items-center w-full h-8 gap-4">
      <span className="text-lg font-semibold min-w-[2.5rem] text-center">
        {minCount}
      </span>
      <Slider
        min={0}
        max={sortedConfigs.length - 1}
        step={1}
        value={[selectedConfigIdx]}
        onValueChange={([idx]) => {
          if (controlledId !== undefined) {
            selectedChanged(sortedConfigs[idx]);
          } else {
            setUncontrolledIdx(idx);
          }
        }}
        className="flex-1"
      />
      <span className="text-lg font-semibold min-w-[2.5rem] text-center">
        {maxCount}
      </span>
    </div>
  );
};

export default PlayerCountSlider;
