import { Slider } from "@/components/ui/slider";
import type { StoryConfigSummary } from "@/types";
import React, { useEffect, useState } from "react";

type Props = {
  minCount: number;
  maxCount: number;
  configurations: StoryConfigSummary[];
  selectedChanged: (selected: StoryConfigSummary) => void;
};

const PlayerCountSlider = ({
  minCount,
  maxCount,
  configurations,
  selectedChanged,
}: Props) => {
  const [selectedConfigIdx, setSelectedConfigIdx] = useState(0);
  const [selectedConfig, setSelectedConfig] = useState(configurations[0]);

  const sortedConfigs = configurations
    .slice()
    .sort((a, b) => a.playerCount - b.playerCount);

  useEffect(() => {
    setSelectedConfig(sortedConfigs[selectedConfigIdx]);
  }, [selectedConfigIdx, sortedConfigs]);

  useEffect(() => {
    if (selectedConfig) {
      selectedChanged(selectedConfig);
    }
  }, [selectedConfig, selectedChanged]);

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
        onValueChange={([idx]) => setSelectedConfigIdx(idx)}
        className="flex-1"
      />
      <span className="text-lg font-semibold min-w-[2.5rem] text-center">
        {maxCount}
      </span>
    </div>
  );
};

export default PlayerCountSlider;
