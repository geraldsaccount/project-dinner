import { useState } from "react";
import { PageHeader } from "@/components";
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { sampleStorySummary } from "@/data/sample-story-summary";
import StoryCard from "./components/story-card";
import type { StoryConfigSummary, StorySummary } from "@/types";
import PlayerCountSlider from "./components/player-count-slider";
import GridLayout from "@/components/layout/grid-layout";
import CharacterCard from "./components/character-profile";

const EventCreationPage = () => {
  const secondSample = {
    ...sampleStorySummary,
    id: "second",
    storyId: "secondStory",
  };
  const thirdSample = {
    ...sampleStorySummary,
    id: "third",
    storyId: "thridStory",
  };
  const stories: StorySummary[] = [
    sampleStorySummary,
    secondSample,
    thirdSample,
  ];
  const [selectedStoryId, setSelectedStoryId] = useState<string | undefined>(
    undefined
  );

  const toggleSelection = (storyId: string) => {
    setSelectedStoryId(selectedStoryId === storyId ? undefined : storyId);
  };

  const selectedStory = stories.find((s) => s.storyId === selectedStoryId);
  const [selectedConfig, setSelectedConfig] = useState<StoryConfigSummary>();

  return (
    <div className="flex flex-col gap-4 items-baseline">
      <PageHeader title="Create New Event" />
      <div className="flex flex-col p-2 gap-2 w-full">
        <h3 className="text-2xl font-semibold text-muted-foreground">
          Step 1: Choose Story
        </h3>
        <Carousel className="w-full md:w-[90%] self-center">
          <CarouselContent>
            {stories.map((s) => (
              <CarouselItem
                key={s.storyId}
                className="basis-2/3 lg:basis-1/2 xl:basis-1/3"
              >
                <button
                  className={`transition-all cursor-pointer ${selectedStoryId === s.storyId ? "opacity-100 scale-100" : "opacity-40 scale-95"} hover:opacity-100`}
                  onClick={() => toggleSelection(s.storyId)}
                >
                  <StoryCard summary={s} />
                </button>
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious className="hidden md:flex" />
          <CarouselNext className="hidden md:flex" />
        </Carousel>
      </div>
      <div
        className={`${selectedStoryId === undefined ? "hidden" : "flex"} flex-col p-2 gap-2 w-full`}
      >
        <h3 className="text-2xl font-semibold text-muted-foreground">
          Step 2: Player Count
        </h3>
        {selectedStory && selectedStory.configs.length > 0 && (
          <PlayerCountSlider
            minCount={selectedStory.minPlayers}
            maxCount={selectedStory.maxPlayers}
            configurations={selectedStory.configs}
            selectedChanged={setSelectedConfig}
          />
        )}
        {/* Optionally show the currently selected player count */}
        {selectedStory && selectedConfig && (
          <div className=" text-center text-muted-foreground self-end">
            Selected player count:{" "}
            <span className="font-bold">{selectedConfig.playerCount}</span>
          </div>
        )}
        <h4 className="text-xl font-semibold">Suspects: </h4>
        <GridLayout
          className="pt-4 w-full"
          gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}
        >
          {selectedConfig?.characterIds.map((cid) => {
            const character = selectedStory?.characters.find(
              (c) => c.id === cid
            );
            return character ? (
              <CharacterCard key={cid} character={character} />
            ) : null;
          })}
        </GridLayout>
      </div>
    </div>
  );
};

export default EventCreationPage;
