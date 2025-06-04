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
import type { StorySummary } from "@/types";

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

  return (
    <div className="flex flex-col gap-4 items-baseline">
      <PageHeader title="Create New Event" />
      <div className="flex flex-col p-2 gap-2 w-full">
        <h3 className="text-2xl font-semibold text-muted-foreground">
          Step 1: Choose Story
        </h3>
        <Carousel className="w-full">
          <CarouselContent>
            {stories.map((s) => {
              console.log(s.storyId);
              return (
                <CarouselItem
                  key={s.storyId}
                  className="basis-2/3 lg:basis-1/2 xl:basis-1/3"
                >
                  <button
                    className={`transition-all cursor-pointer ${selectedStoryId === s.storyId ? "opacity-100 scale-100" : "opacity-40 scale-95"}`}
                    onClick={() => toggleSelection(s.storyId)}
                  >
                    <StoryCard summary={s} />
                  </button>
                </CarouselItem>
              );
            })}
          </CarouselContent>
          <CarouselPrevious className="hidden md:flex" />
          <CarouselNext className="hidden md:flex" />
        </Carousel>
      </div>
    </div>
  );
};

export default EventCreationPage;
